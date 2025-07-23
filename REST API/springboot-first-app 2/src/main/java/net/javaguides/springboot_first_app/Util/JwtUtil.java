package net.javaguides.springboot_first_app.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.javaguides.springboot_first_app.bean.Kullanici;
import net.javaguides.springboot_first_app.bean.KullaniciRol;
import net.javaguides.springboot_first_app.repository.KullaniciRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    private KullaniciRepository kullaniciRepository;

    public String generateToken(String username, KullaniciRol rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol.name());
        return createToken(claims, username);
    }

    public KullaniciRol extractRol(String token){
        final Claims claims = extractAllClaims(token);
        return KullaniciRol.valueOf(claims.get("rol", String.class));
    }

    public boolean validateUserFromToken(String token) {
        final String usernameAndPassword = extractUsername(token);

        String[] userDetails = usernameAndPassword.split(":");
        if (userDetails.length != 2) {
            return false;
        }

        String username = userDetails[0];
        String password = userDetails[1];

        // Veritabanında kullanıcıyı arayıp doğruladım
        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(username)
                .orElse(null);

        return kullanici != null && kullanici.getSifre().equals(password);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 saat
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}