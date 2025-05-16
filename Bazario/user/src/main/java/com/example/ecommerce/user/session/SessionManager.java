package com.example.ecommerce.user.session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
public class SessionManager {

    private static volatile SessionManager instance;
    private final ConcurrentMap<Long, Boolean> activeSessions = new ConcurrentHashMap<>();

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public void loginUser(Long userId) {
        activeSessions.put(userId, true);
    }

    public void logoutUser(Long userId) {
        activeSessions.remove(userId);
    }

    public boolean isUserLoggedIn(Long userId) {
        return activeSessions.containsKey(userId);
    }
}
