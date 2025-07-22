// src/main/java/com/example/demo/service/UserService.java
package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Add this field declaration
    private boolean loadedFromDatabase = false;

    @Cacheable(value = "users", key = "'allUsers'")
    public List<User> getAllUsers() {
        // Set a flag that this method was executed (meaning data came from database)
        System.setProperty("cache.method.executed", "true");

        // Also set our instance variable
        loadedFromDatabase = true;

        System.out.println("Fetching users from database...");
        return userRepository.findAll();
    }

    public boolean wasLoadedFromDatabase() {
        boolean result = loadedFromDatabase;
        // Reset after checking
        loadedFromDatabase = false;
        return result;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
