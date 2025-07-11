package net.javaguides.springboot_first_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // CustomerNotFoundException türündeki istisnaları yakalar.
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        // İstisnadan gelen mesajı kullanarak bir hata yanıtı oluştururuz.
        // HTTP 404 Not Found durum kodu ile birlikte hata mesajını döneriz.
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}