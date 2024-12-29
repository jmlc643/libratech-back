package com.upao.pe.libratech.services;

import com.upao.pe.libratech.dtos.role.RoleDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
import com.upao.pe.libratech.exceptions.ResourceNotExistsException;
import com.upao.pe.libratech.models.Role;
import com.upao.pe.libratech.models.enums.ERole;
import com.upao.pe.libratech.repos.RoleRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // READ
    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream().map(this::returnRoleDTO).toList();
    }

    // CREATE
    public RoleDTO createRole(RoleDTO roleDTO) {
        ERole enumRole = transformStringtoERole(roleDTO.getRoleName());
        if(roleRepository.existsByRoleName(enumRole)) {
            throw new ResourceAlreadyExistsException("El rol ya existe");
        }
        Role role = new Role(null, enumRole, new ArrayList<>());
        roleRepository.save(role);
        return returnRoleDTO(role);
    }

    // DELETE
    public List<RoleDTO> deleteRole(String roleName) {
        Role role = getRole(roleName);
        roleRepository.delete(role);
        return findAll();
    }

    private Role getRole(String roleName) {
        ERole enumRole = transformStringtoERole(roleName);
        Optional<Role> role = roleRepository.findByRoleName(enumRole);
        if(role.isEmpty()) {
            throw new ResourceNotExistsException("El rol no existe");
        }
        return role.get();
    }

    private ERole transformStringtoERole(String roleName) {
        try {
            return ERole.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotExistsException("El rol no existe");
        }
    }

    private RoleDTO returnRoleDTO(Role role) {
        return new RoleDTO(WordUtils.capitalizeFully(role.getRoleName().toString()));
    }
}
