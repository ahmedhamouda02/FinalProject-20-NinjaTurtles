package com.example.ecommerce.user.services;

import com.example.ecommerce.user.factory.UserFactory;
import com.example.ecommerce.user.factory.UserType;
import com.example.ecommerce.user.models.User;
import com.example.ecommerce.user.repositories.UserRepository;
import com.example.ecommerce.user.session.SessionManager;
import com.jayway.jsonpath.spi.cache.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;
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

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("BUYER");
        }

        return userRepository.save(user);
    }

    @Cacheable(key = "#id")
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserType> getUserType(Long userId) {
        return userRepository.findById(userId).map(user -> UserFactory.createUserType(user.getRole()));
    }

    @CachePut(key = "#result.id")
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        });
    }

    @CacheEvict(key = "#id")
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Login
    @CachePut(key = "#result.id", unless = "#result == null")
    public User login(String email, String password) {
        User u = userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(null);
        if (u != null) {
            SessionManager.getInstance().loginUser(u.getId());
        }
        return u;
    }

    @CacheEvict(key = "#userId")
    public boolean logout(Long userId) {
        SessionManager.getInstance().logoutUser(userId);
        return true;
    }

    public boolean isUserLoggedIn(Long userId) {
        org.springframework.cache.Cache cache = cacheManager.getCache("user_cache");
        if (cache != null) {
            return cache.get(userId) != null;
        }
        SessionManager.getInstance().isUserLoggedIn(userId);
        return false;
    }

    @CacheEvict(key = "#id")
    public boolean resetPassword(Long id, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password cannot be empty");
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
}
