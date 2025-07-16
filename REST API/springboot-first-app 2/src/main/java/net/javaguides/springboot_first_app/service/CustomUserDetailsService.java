package net.javaguides.springboot_first_app.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Burada gerçek kullanıcı veritabanı sorgusu yapılmalı
        // Bu sadece test amaçlı basit bir örnektir
        if ("testuser".equals(username)) {
            return new User(username, 
                          passwordEncoder.encode("password"),
                          new ArrayList<>());
        }
        throw new UsernameNotFoundException("Kullanıcı bulunamadı: " + username);
    }
}