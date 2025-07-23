package net.javaguides.springboot_first_app.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.springboot_first_app.Util.JwtUtil;
import net.javaguides.springboot_first_app.bean.Kullanici;
import net.javaguides.springboot_first_app.model.AuthenticationResponse;
import net.javaguides.springboot_first_app.model.ErrorResponse;
import net.javaguides.springboot_first_app.repository.KullaniciRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    private final JwtUtil jwtUtil;
    private final KullaniciRepository kullaniciRepository;

    @PostMapping("/getToken")
    public ResponseEntity<?> getToken(@RequestBody Kullanici kullanici) {
        Kullanici dbUser = kullaniciRepository.findByKullaniciAdi(kullanici.getKullaniciAdi())
                .orElse(null);

        if (dbUser == null || !dbUser.getSifre().equals(kullanici.getSifre())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Geçersiz kullanıcı adı veya şifre"));
        }

        String jwt = jwtUtil.generateToken(kullanici.getKullaniciAdi(), dbUser.getRol());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Token eksik veya geçersiz"));
        }

        String jwt = token.substring(7);

        // Token geçerliliğini kontrol et
        if (!jwtUtil.validateToken(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Geçersiz veya süresi dolmuş token"));
        }

        // Token'dan kullanıcı doğrulama
        boolean isValidUser = jwtUtil.validateUserFromToken(jwt);
        if (!isValidUser) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Token, geçersiz kullanıcı bilgileri içeriyor"));
        }

        return ResponseEntity.ok("Valid token and user!");
    }
}