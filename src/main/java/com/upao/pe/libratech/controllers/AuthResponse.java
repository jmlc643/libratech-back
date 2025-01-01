package com.upao.pe.libratech.controllers;

import com.upao.pe.libratech.dtos.auth.AuthRequestDTO;
import com.upao.pe.libratech.dtos.auth.AuthResponseDTO;
import com.upao.pe.libratech.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthResponse {

    @Autowired
    private AuthService authService;

    @PostMapping("/login/")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }
}
