// src/main/java/com/example/demo/dto/UserListResponse.java
package com.example.demo.dto;

import com.example.demo.model.User;

import java.util.List;

public class UserListResponse {
    private List<User> users;
    private String dataSource;

    public UserListResponse(List<User> users, String dataSource) {
        this.users = users;
        this.dataSource = dataSource;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getDataSource() {
        return dataSource;
    }
}
