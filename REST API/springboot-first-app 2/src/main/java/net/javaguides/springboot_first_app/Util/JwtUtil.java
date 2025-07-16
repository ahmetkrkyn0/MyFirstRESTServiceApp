package net.javaguides.springboot_first_app.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders; // Base64 kodlu anahtarları çözmek için
import io.jsonwebtoken.security.Keys; // Güvenli anahtar oluşturma için
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key; // java.security paketindeki Key sınıfı
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component // Bu sınıfın Spring tarafından yönetilen bir bean olduğunu belirtiriz
public class JwtUtil {

    // JWT gizli anahtarımızı (secret key) application.properties'dan okuyacağız
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // JWT'nin geçerlilik süresini (milisaniye cinsinden) application.properties'dan okuyacağız
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME; // Örneğin 3600000 milisaniye = 1 saat

    // --- JWT Oluşturma Metotları ---

    /**
     * Kullanıcı adı ile bir JWT oluşturur.
     * @param username JWT'nin konusu (Subject) olacak kullanıcı adı.
     * @return Oluşturulan JWT string'i.
     */
    public String generateToken(String username) {
        // Token içine eklemek istediğimiz ek bilgileri (claim'leri) burada tanımlarız.
        // Şimdilik boş bırakabiliriz veya rolleri ekleyebiliriz.
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Verilen talepler (claims) ve konu (subject - genellikle kullanıcı adı) ile JWT oluşturur.
     * Bu metod, generateToken tarafından dahili olarak kullanılır.
     * @param claims Token içine eklenecek ek bilgiler.
     * @param subject JWT'nin konusu (örneğin kullanıcı adı).
     * @return Oluşturulan JWT string'i.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // Talepleri ayarla
                .setSubject(subject) // Konuyu (kullanıcı adını) ayarla
                .setIssuedAt(new Date(System.currentTimeMillis())) // Token oluşturulma zamanı
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Token son kullanma zamanı
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Token'ı gizli anahtar ve algoritma ile imzala
                .compact(); // JWT'yi sıkıştırılmış string olarak oluştur
    }

    /**
     * JWT'yi imzalamak için kullanılacak gizli anahtarı (SecretKey) döner.
     * SECRET_KEY string'inden türetilir.
     * @return İmzalama için kullanılacak SecretKey nesnesi.
     */
    private Key getSigningKey() {
        // Base64 kodlu SECRET_KEY'i byte dizisine dönüştürürüz
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        // Bu byte dizisinden güvenli bir HMAC anahtarı oluştururuz
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // --- JWT Doğrulama ve Bilgi Çekme Metotları ---

    /**
     * Verilen JWT'den tüm talepleri (claims) çıkarır.
     * @param token İşlenecek JWT.
     * @return JWT'deki tüm talepleri içeren Claims nesnesi.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // İmzalamak için kullanılan anahtarı belirle
                .build()
                .parseClaimsJws(token) // JWT'yi ayrıştır
                .getBody(); // Talepleri içeren gövdeyi al
    }

    /**
     * Verilen JWT'den belirli bir talebi (claim) çıkarır.
     * @param token İşlenecek JWT.
     * @param claimsResolver Talep çekme fonksiyonu (örneğin Claims::getSubject).
     * @param <T> Talep tipini belirtir.
     * @return Çekilen talep değeri.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Verilen JWT'den kullanıcı adını (subject) çıkarır.
     * @param token İşlenecek JWT.
     * @return JWT'den çıkarılan kullanıcı adı.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // getSubject ile kullanıcı adını çek
    }

    /**
     * Verilen JWT'den son kullanma tarihini çıkarır.
     * @param token İşlenecek JWT.
     * @return JWT'den çıkarılan son kullanma tarihi.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // getExpiration ile son kullanma tarihini çek
    }

    /**
     * JWT'nin süresinin dolup dolmadığını kontrol eder.
     * @param token Kontrol edilecek JWT.
     * @return JWT'nin süresi dolmuşsa true, aksi takdirde false.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Son kullanma tarihi şu anki zamandan önce mi?
    }

    /**
     * Verilen JWT'nin geçerli olup olmadığını doğrular.
     * Kullanıcı adı eşleşmeli ve token'ın süresi dolmamış olmalı.
     * @param token Doğrulanacak JWT.
     * @param userDetails Kullanıcı bilgilerini içeren UserDetails nesnesi.
     * @return Token geçerliyse true, aksi takdirde false.
     */
    public Boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
