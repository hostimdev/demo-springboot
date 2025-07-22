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
import com.example.demo.aspect.CacheSourceAspect;

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
        // Fetch users - this will use cache if available
        List<User> users = userService.getAllUsers();

        // Check if data came from database
        boolean dataFromDatabase = userService.wasLoadedFromDatabase();
        String dataSource = dataFromDatabase ? "PostgreSQL Database" : "Redis Cache";

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
