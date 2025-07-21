package net.javaguides.springboot_first_app.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.springboot_first_app.Util.JwtUtil;
import net.javaguides.springboot_first_app.bean.Kullanici;
import net.javaguides.springboot_first_app.model.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    private final JwtUtil jwtUtil;

    @PostMapping("/getToken")
    public ResponseEntity<AuthenticationResponse> getToken(@RequestBody Kullanici kullanici) {

        String user = kullanici.getKullaniciAdi() + ":" + kullanici.getSifre();
        String jwt = jwtUtil.generateToken(user, kullanici.getRol());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}