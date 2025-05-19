package com.example.ecommerce.user.services;

import com.example.ecommerce.user.factory.UserFactory;
import com.example.ecommerce.user.factory.UserType;
import com.example.ecommerce.user.models.User;
import com.example.ecommerce.user.repositories.UserRepository;
import com.example.ecommerce.user.session.SessionManager;
import com.example.ecommerce.user.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "user_cache")
public class UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private CacheManager cacheManager;

  @Autowired
  private JwtUtil jwtUtil;

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public User register(User user) {
    // Email format validation
    if (!isValidEmail(user.getEmail())) {
      throw new IllegalArgumentException("Invalid email format.");
    }

    // Password strength validation
    if (!isValidPassword(user.getPassword())) {
      throw new IllegalArgumentException(
          "Password must be at least 8 characters, include upper and lower case letters, and contain at least one digit.");
    }

    // Check for email uniqueness
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new IllegalArgumentException("Email already exists.");
    }

    // Normalize and validate role
    String role = (user.getRole() == null || user.getRole().isBlank()) ? "buyer" : user.getRole().toLowerCase();
    if (!isValidRole(role)) {
      throw new IllegalArgumentException("Role must be one of: admin, seller, buyer.");
    }
    user.setRole(role); // Save the normalized role

    // Hash the password
    String hashedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(hashedPassword);

    return userRepository.save(user);
  }

  // @Cacheable(cacheNames = "users", key = "#id", unless = "#result == null")
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Optional<UserType> getUserType(Long userId) {
    return userRepository.findById(userId).map(user -> UserFactory.createUserType(user.getRole()));
  }

  // @CachePut(key = "#result.id")
  public Optional<User> updateUser(Long id, User updatedUser) {
    // Validate email
    if (!isValidEmail(updatedUser.getEmail())) {
      throw new IllegalArgumentException("Invalid email format.");
    }

    // Validate password
    if (!isValidPassword(updatedUser.getPassword())) {
      throw new IllegalArgumentException(
          "Password must be at least 8 characters, include upper and lower case letters, and contain at least one digit.");
    }

    // Optional: validate role
    if (!isValidRole(updatedUser.getRole())) {
      throw new IllegalArgumentException("Role must be one of: admin, seller, buyer.");
    }

    return userRepository.findById(id).map(user -> {
      user.setName(updatedUser.getName());
      user.setEmail(updatedUser.getEmail());
      user.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // hash new password
      user.setPhoneNumber(updatedUser.getPhoneNumber());
      user.setRole(updatedUser.getRole().toLowerCase());
      return userRepository.save(user);
    });
  }

  // @CacheEvict(key = "#id")
  public boolean deleteUser(Long id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }

  @CachePut(cacheNames = "sessions", key = "#result")
  public String login(String email, String password) {
    if (!isValidEmail(email)) {
      throw new IllegalArgumentException("Invalid email format.");
    }

    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("Password cannot be empty.");
    }

    User user = userRepository.findByEmail(email)
        .filter(u -> passwordEncoder.matches(password, u.getPassword()))
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

    Long userId = user.getId(); // needed for @CachePut
    SessionManager.getInstance().loginUser(userId);

    return jwtUtil.generateToken(userId);
  }

  @CacheEvict(cacheNames = "sessions", key = "#token")
  public boolean logout(String token) {
    Long userId = jwtUtil.extractUserId(token);

    if (!SessionManager.getInstance().isUserLoggedIn(userId)) {
      throw new IllegalStateException("User is not logged in.");
    }

    SessionManager.getInstance().logoutUser(userId);
    return true;
  }

  public boolean isUserLoggedIn(Long userId) {
    return SessionManager.getInstance().isUserLoggedIn(userId);
  }

  @CacheEvict(key = "#id")
  public boolean resetPassword(Long id, String newPassword) {
    // Validate user is logged in
    if (!SessionManager.getInstance().isUserLoggedIn(id)) {
      throw new IllegalStateException("User is not logged in.");
    }
    if (newPassword == null || newPassword.isBlank()) {
      throw new IllegalArgumentException("New password cannot be empty");
    }

    if (!isValidPassword(newPassword)) {
      throw new IllegalArgumentException(
          "Password must be at least 8 characters, include upper and lower case letters, and contain at least one digit.");
    }

    return userRepository.findById(id)
        .map(user -> {
          user.setPassword(passwordEncoder.encode(newPassword));
          userRepository.save(user);
          SessionManager.getInstance().logoutUser(id);

          return true;
        })
        .orElse(false);
  }

  private boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email != null && email.matches(emailRegex);
  }

  private boolean isValidPassword(String password) {
    // Minimum 8 characters, at least one uppercase, one lowercase, one digit
    String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    return password != null && password.matches(passwordRegex);
  }

  private boolean isValidRole(String role) {
    return role != null && List.of("admin", "seller", "buyer").contains(role.toLowerCase());
  }

}
