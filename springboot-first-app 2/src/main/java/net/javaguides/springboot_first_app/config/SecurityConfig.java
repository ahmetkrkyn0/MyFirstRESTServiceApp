package net.javaguides.springboot_first_app.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
        UserDetails admin = User.withUsername("admin")
                                .password(passwordEncoder.encode("12345"))
                                .roles("ADMIN").build();

        UserDetails user = User.withUsername("user")
                               .password(passwordEncoder.encode("123"))
                               .roles("USER").build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // REST API'ler için genellikle devre dışı bırakılır
                .cors(cors -> {
                }) // CORS'u etkinleştirir

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/students").hasRole("ADMIN")

                        .requestMatchers("/api/students/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "USER")

                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {
                }); 

        return http.build();
    }
}
