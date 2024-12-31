package com.upao.pe.libratech.unit.services;

import com.upao.pe.libratech.dtos.role.RoleDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
import com.upao.pe.libratech.exceptions.ResourceNotExistsException;
import com.upao.pe.libratech.models.Role;
import com.upao.pe.libratech.models.enums.ERole;
import com.upao.pe.libratech.repos.RoleRepository;
import com.upao.pe.libratech.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private List<Role> roles;
    private List<Role> rolesAfterDelete;
    private List<RoleDTO> roleDTOsAfterDelete;

    @BeforeEach
    void setUp() {
        roles = List.of(
                new Role(1, ERole.ADMIN, null),
                new Role(2, ERole.TEACHER, null)
        );
        rolesAfterDelete = List.of(
                new Role(1, ERole.ADMIN, null)
        );
        roleDTOsAfterDelete = List.of(
                new RoleDTO("Admin")
        );
    }

    @Test
    void testFindAll() {
        // When
        when(roleRepository.findAll()).thenReturn(roles);
        List<RoleDTO> result = roleService.findAll();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getRoleName()).isEqualTo("Admin");
        assertThat(result.getLast().getRoleName()).isEqualTo("Teacher");
        verify(roleRepository).findAll();
    }

    @Test
    void testCreateRole() {
        // Given
        RoleDTO role = new RoleDTO("Student");

        // When
        RoleDTO result = roleService.createRole(role);

        // Then
        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(roleArgumentCaptor.capture());
        assertEquals(ERole.STUDENT, roleArgumentCaptor.getValue().getRoleName());

        assertThat(result).isNotNull();
        assertThat(result.getRoleName()).isEqualTo("Student");
    }

    @Test
    void testCreateRoleWhenRoleExists() {
        // Given
        RoleDTO roleDTO = new RoleDTO("Admin");
        ERole eRole = ERole.ADMIN;

        // When
        when(roleRepository.existsByRoleName(eRole)).thenReturn(true);
        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> roleService.createRole(roleDTO));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El rol ya existe");
    }

    @Test
    void testDeleteRoleTest() {
        // Given
        String roleName = "Teacher";
        Role role = roles.getLast();

        // When
        when(roleRepository.findByRoleName(ERole.TEACHER)).thenReturn(Optional.of(role));
        doNothing().when(roleRepository).delete(role);
        when(roleRepository.findAll()).thenReturn(rolesAfterDelete);

        List<RoleDTO> result = roleService.deleteRole(roleName);

        // Then
        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).delete(roleArgumentCaptor.capture());
        assertEquals(ERole.TEACHER, roleArgumentCaptor.getValue().getRoleName());

        assertThat(result).hasSize(roleDTOsAfterDelete.size());
        assertThat(result).containsExactlyInAnyOrderElementsOf(roleDTOsAfterDelete);
    }

    @Test
    void testDeleteRoleTestWhenRoleNotExists() {
        // Given
        String roleName = "NON_EXISTENT_ROLE";

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> roleService.deleteRole(roleName));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El rol no existe");
    }
}
