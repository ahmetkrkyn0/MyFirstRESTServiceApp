package net.javaguides.springboot_first_app.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("testuser".equals(username)) {
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode("password"))
                    .roles("USER") // Temel kullanıcı rolü
                    .authorities("ROLE_USER", "READ", "WRITE") // Ek yetkiler
                    .build();
        }
        throw new UsernameNotFoundException("Kullanıcı bulunamadı: " + username);
    }
}
