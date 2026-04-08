package com.ionidea.mothramxbe.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ionidea.mothramxbe.dto.UserRequestDTO;
import com.ionidea.mothramxbe.entity.User;
import com.ionidea.mothramxbe.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Only ADMIN can create users
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public User createUser(@RequestBody UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    // ✅ ADMIN and LEAD can view all users
    @PreAuthorize("hasAnyAuthority('ADMIN','LEAD')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ Only ADMIN can view leads
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/leads")
    public List<User> getLeads() {
        return userService.getLeads();
    }

    // ✅ Only ADMIN can update users
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @RequestBody UserRequestDTO dto) {
        return userService.updateUser(id, dto);
    }

    // ✅ Only ADMIN can delete users
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
}