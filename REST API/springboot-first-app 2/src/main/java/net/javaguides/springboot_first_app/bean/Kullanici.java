package net.javaguides.springboot_first_app.bean;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String kullaniciAdi;

    @Getter
    @Setter
    private String sifre;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private KullaniciRol rol;


}