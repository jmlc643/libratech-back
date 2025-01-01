package com.upao.pe.libratech.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.upao.pe.libratech.dtos.auth.AuthRequestDTO;
import com.upao.pe.libratech.dtos.auth.AuthResponseDTO;
import com.upao.pe.libratech.repos.UserRepository;
import com.upao.pe.libratech.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private JwtDetailsService userDetailsService;

    public AuthResponseDTO login(AuthRequestDTO request){
        Authentication authentication = authenticate(request.getUsername(), request.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateToken(authentication);

        DecodedJWT decodedJWT = jwtUtils.validateJWT(accessToken);

        String role = jwtUtils.extractSpecificClaim(decodedJWT, "authorities").asString();

        return new AuthResponseDTO(request.getUsername(), role, accessToken);
    }

    private Authentication authenticate(String username, String password){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid Username or Password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

}
