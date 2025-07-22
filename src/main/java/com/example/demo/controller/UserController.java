// src/main/java/com/example/demo/controller/UserController.java
package com.example.demo.controller;

import com.example.demo.dto.UserListResponse;
import com.example.demo.model.User;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/users")
    public String listUsers(Model model) {
        // Log before fetching
        System.out.println("About to fetch users...");

        // Fetch users - this will use cache if available
        List<User> users = userService.getAllUsers();

        // Check if data came from cache by examining the logs
        // or checking Redis directly via cacheManager
        boolean dataInCache = cacheManager.getCache("users") != null &&
                              cacheManager.getCache("users").get("allUsers") != null;

        String dataSource = dataInCache ? "Redis Cache" : "PostgreSQL Database";

        model.addAttribute("users", users);
        model.addAttribute("title", "User Management");
        model.addAttribute("dataSource", dataSource);

        return "users";
    }

    @PostMapping("/users")
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("email") String email,
                          @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        if (avatar != null && !avatar.isEmpty()) {
            String fileName = fileStorageService.storeFile(avatar);
            user.setAvatarPath("/uploads/" + fileName);
        }

        userService.saveUser(user);
        return "redirect:/users";
    }
}
