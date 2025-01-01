package com.upao.pe.libratech.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String user;
    private String role;
    private String token;
}
