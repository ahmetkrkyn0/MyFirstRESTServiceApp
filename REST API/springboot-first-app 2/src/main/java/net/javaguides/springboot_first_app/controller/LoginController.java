package net.javaguides.springboot_first_app.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.springboot_first_app.model.AuthenticationRequest;
import net.javaguides.springboot_first_app.model.AuthenticationResponse;
import net.javaguides.springboot_first_app.model.ErrorResponse;
import net.javaguides.springboot_first_app.Util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody @Valid AuthenticationRequest authenticationRequest) {
        try {
            // Kullanıcı kimlik doğrulaması
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
                )
            );

            // Token oluşturma
            final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails.getUsername());

            // Başarılı yanıt
            return ResponseEntity.ok(new AuthenticationResponse(jwt));

        } catch (BadCredentialsException e) {
            // Hatalı kimlik bilgileri durumu
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Geçersiz kullanıcı adı veya şifre"));
        } catch (Exception e) {
            // Diğer hatalar
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Kimlik doğrulama sırasında bir hata oluştu"));
        }
    }

    // Test endpoint'i (isteğe bağlı)
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("JWT doğrulaması başarılı!");
    }
}