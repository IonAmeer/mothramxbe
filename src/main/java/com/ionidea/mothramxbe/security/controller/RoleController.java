package com.ionidea.mothramxbe.security.controller;

import com.ionidea.mothramxbe.security.dto.RoleDTO;
import com.ionidea.mothramxbe.security.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('AUTH_ADMIN')")
public class RoleController { // all controllers must return dto

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public com.ionidea.mothramxbe.security.model.Role createRole(@RequestBody RoleDTO dto) {
        return roleService.createRole(dto);
    }

    @GetMapping
    public List<com.ionidea.mothramxbe.security.model.Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/{id}")
    public com.ionidea.mothramxbe.security.model.Role updateRole(@PathVariable Long id, @RequestBody RoleDTO dto) {
        return roleService.updateRole(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "RoleDTO deleted successfully";
    }

}