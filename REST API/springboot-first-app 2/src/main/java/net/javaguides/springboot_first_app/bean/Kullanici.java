package net.javaguides.springboot_first_app.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Getter
    @Setter
    private String kullaniciAdi;
    @Getter
    @Setter
    private String sifre;


}