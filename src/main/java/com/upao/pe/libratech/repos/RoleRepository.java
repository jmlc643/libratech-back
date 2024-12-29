package com.upao.pe.libratech.repos;

import com.upao.pe.libratech.models.Role;
import com.upao.pe.libratech.models.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByRoleName(ERole roleName);

    Optional<Role> findByRoleName(ERole roleName);
}
