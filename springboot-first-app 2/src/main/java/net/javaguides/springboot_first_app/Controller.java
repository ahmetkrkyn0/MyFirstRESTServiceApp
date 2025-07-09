package net.javaguides.springboot_first_app;

import net.javaguides.springboot_first_app.bean.Student;
import net.javaguides.springboot_first_app.bean.Customer;
import net.javaguides.springboot_first_app.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {

    private final CustomerService customerService;

    @Autowired
    public Controller(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/customer/add")
    public String addCustomer(@RequestParam String first, @RequestParam String last) {
        Customer customer = new Customer();
        customer.setFirstName(first);
        customer.setLastName(last);
        customerService.saveCustomer(customer);
        return "Added new customer to repo!";
    }

    @GetMapping("/customer/list")
    public Iterable<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customer/find/{id}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable Integer id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/customer/delete/{id}")
    public ResponseEntity<String> deleteCustomerByID(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Deleted customer with id: " + id);
    }

    @PostMapping
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        return new ResponseEntity<>("Added new customer to repo!", HttpStatus.CREATED);
    }

    @PutMapping("/customer/update/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody Student student){

    }
}
