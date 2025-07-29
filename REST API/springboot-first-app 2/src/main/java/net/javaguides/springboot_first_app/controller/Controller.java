package net.javaguides.springboot_first_app.controller;

import net.javaguides.springboot_first_app.Util.JwtUtil;
import net.javaguides.springboot_first_app.bean.Customer;
import net.javaguides.springboot_first_app.bean.KullaniciRol;
import net.javaguides.springboot_first_app.bean.Student;
import net.javaguides.springboot_first_app.service.CustomerService;
import net.javaguides.springboot_first_app.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
@Tag(name = "Customer and Student Management", description = "Müşteri ve öğrenci işlemleri için API")
public class Controller {

    private final CustomerService customerService;
    private final StudentService studentService;
    private final JwtUtil jwtUtil;

    @Autowired
    public Controller(CustomerService customerService, StudentService studentService, JwtUtil jwtUtil) {
        this.customerService = customerService;
        this.studentService = studentService;
        this.jwtUtil = jwtUtil;
    }

    // --- CUSTOMER ENDPOINTS --- //

    @GetMapping("/customers")
    @Operation(summary = "Tüm Müşterileri Listele", description = "Mevcut tüm müşteri kayıtlarını döndürür.\n")
    public ResponseEntity<?> getCustomers(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Geçersiz token");
            }

            KullaniciRol rol = jwtUtil.extractRol(token);
            if (rol != KullaniciRol.ADMIN && rol != KullaniciRol.KULLANICI) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Bu işlem için yetkiniz yok");
            }

            return ResponseEntity.ok(customerService.getAllCustomers());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token gerekli");
        }

    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable Integer id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<String> deleteCustomerByID(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Deleted customer with id: " + id);
    }

    @PostMapping("/customers")
    @Operation(summary = "Müşteri Ekle", description = "Yeni bir müşteri kaydı oluşturur.\n")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer,
                                         BindingResult bindingResult,
                                         @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Token gerekli");
        }

        String token = authHeader.substring(7);
        if(!jwtUtil.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Geçersiz token");
        }

        KullaniciRol rol = jwtUtil.extractRol(token);
        if(rol != KullaniciRol.ADMIN){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body("Bu işlem için ADMIN yetkisi gerekli");
        }
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }
        customerService.saveCustomer(customer);
        return new ResponseEntity<>("New customer successfully added!", HttpStatus.CREATED);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id,
                                            @Valid @RequestBody Customer customer,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }
        Customer existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer == null) {
            return new ResponseEntity<>("Customer to be updated not found.", HttpStatus.NOT_FOUND);
        }
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return ResponseEntity.ok(updatedCustomer);
    }


    // --- STUDENT ENDPOINTS --- //

    @PostMapping("/students")
    @Operation(summary = "Öğrenci Ekle", description = "Yeni bir öğrenci kaydı oluşturur.\n")
    public ResponseEntity<?> addStudent(@Valid @RequestBody Student student,
                                        BindingResult bindingResult,
                                        @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // JWT kontrolu ekledim
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("JWT token eksik veya geçersiz format.");
        }

        // Token doğrulama işlemi
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Geçersiz veya süresi dolmuş token.");
        }

        // Normal işlem akışı
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }
        studentService.saveStudent(student);
        return new ResponseEntity<>("New student successfully added!", HttpStatus.CREATED);
    }

    @GetMapping("/students")
    @Operation(summary = "Tüm Öğrencileri Listele", description = "Mevcut tüm öğrenci kayıtlarını döndürür.\n")
    public Iterable<Student> getStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> findStudentById(@PathVariable Integer id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudentByID(@PathVariable Integer id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.ok("Deleted student with id: " + id);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer id,
                                           @Valid @RequestBody Student student,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }
        Student existingStudent = studentService.getStudentById(id);
        if (existingStudent == null) {
            return new ResponseEntity<>("Student to be updated not found.", HttpStatus.NOT_FOUND);
        }
        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
    }
}