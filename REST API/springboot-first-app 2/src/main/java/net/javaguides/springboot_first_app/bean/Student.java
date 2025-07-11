package net.javaguides.springboot_first_app.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private int id;

    @Getter
    @Setter
    @NotBlank(message = "Ad alanı boş bırakılamaz.")
    private String name;

    @Getter
    @Setter
    @NotBlank(message = "Soyisim alanı boş bırakılamaz.")
    private String surname;

    @Getter
    @Setter
    @Max(value = 24, message = "Yaş 25'ten küçük olmalıdır.")
    private int age;

    @Getter
    @Setter
    @NotBlank(message = "Email alanı boş bırakılamaz.")
    @Email(message = "Geçerli bir email adresi giriniz.")
    private String email;

    @Getter
    @Setter
    @NotBlank(message = "Ünvan alanı boş bırakılamaz")
    private String degree;

    @Getter
    @Setter
    @NotBlank(message = "Telefon numarası boş bırakılamaz.")
    @Pattern(regexp = "^(\\+90|0)?5[0-9]{2}[0-9]{3}[0-9]{2}[0-9]{2}$",
             message = "Geçerli bir Türkiye cep telefonu numarası giriniz.")
    private String phone_number;

}
