package net.javaguides.springboot_first_app.controller;

import net.javaguides.springboot_first_app.bean.Customer;
import net.javaguides.springboot_first_app.bean.Student;
import net.javaguides.springboot_first_app.service.CustomerService;
import net.javaguides.springboot_first_app.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class Controller {

    private final CustomerService customerService;
    private final StudentService studentService;

    @Autowired
    public Controller(CustomerService customerService, StudentService studentService) {
        this.customerService = customerService;
        this.studentService = studentService;
    }

    // --- CUSTOMER ENDPOINTS --- //

    @GetMapping("/customers")
    public Iterable<Customer> getCustomers() {
        return customerService.getAllCustomers();
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
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer, BindingResult bindingResult) {
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
    public ResponseEntity<?> addStudent(@Valid @RequestBody Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }
        studentService.saveStudent(student);
        return new ResponseEntity<>("New student successfully added!", HttpStatus.CREATED);
    }

    @GetMapping("/students")
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