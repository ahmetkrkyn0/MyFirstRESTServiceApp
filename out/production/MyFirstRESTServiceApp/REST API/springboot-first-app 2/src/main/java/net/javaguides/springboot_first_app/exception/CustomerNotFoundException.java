package net.javaguides.springboot_first_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {


    public CustomerNotFoundException(String message) {
        super(message);
    }
}