package com.upao.pe.libratech.services;

import com.upao.pe.libratech.models.User;
import com.upao.pe.libratech.repos.UserRepository;
import com.upao.pe.libratech.utils.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario "+username + " no fue encontrado"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+user.getRole().getRoleName().name());
        return new UserSecurity(user.getUsername(), user.getPassword(), Collections.singletonList(authority), user);
    }
}
