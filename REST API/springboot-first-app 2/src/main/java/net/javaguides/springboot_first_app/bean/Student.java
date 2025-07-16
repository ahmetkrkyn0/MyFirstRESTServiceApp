package net.javaguides.springboot_first_app.bean;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Ad alanı boş bırakılamaz")
    private String name;

    @NotBlank(message = "Soyad alanı boş bırakılamaz")
    private String surname;

    @Min(value = 18, message = "Yaş 18'den küçük olamaz")
    @Max(value = 24, message = "Yaş 24'ten büyük olamaz")
    private Integer age;

    @NotBlank(message = "Email alanı boş bırakılamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @NotBlank(message = "Ünvan alanı boş bırakılamaz")
    private String degree;

    @NotBlank(message = "Telefon numarası boş bırakılamaz")
    @Pattern(regexp = "^(\\+90|0)?5[0-9]{2}[0-9]{3}[0-9]{2}[0-9]{2}$",
             message = "Geçerli bir Türkiye telefon numarası giriniz")
    private String phone_number;
}