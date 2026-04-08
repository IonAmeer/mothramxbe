package com.ionidea.mothramxbe.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ionidea.mothramxbe.dto.RoleRequestDTO;
import com.ionidea.mothramxbe.entity.Role;
import com.ionidea.mothramxbe.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // ✅ Only ADMIN can create roles
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public Role createRole(@RequestBody RoleRequestDTO dto) {
        return roleService.createRole(dto);
    }

    // ✅ Only ADMIN can view roles
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    // ✅ Only ADMIN can update roles
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id,
                           @RequestBody RoleRequestDTO dto) {
        return roleService.updateRole(id, dto);
    }

    // ✅ Only ADMIN can delete roles
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "Role deleted successfully";
    }
}