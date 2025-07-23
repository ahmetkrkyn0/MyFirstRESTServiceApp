package net.javaguides.springboot_first_app.repository;

import net.javaguides.springboot_first_app.bean.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {
    Optional<Kullanici> findByKullaniciAdi(String kullaniciAdi);
}