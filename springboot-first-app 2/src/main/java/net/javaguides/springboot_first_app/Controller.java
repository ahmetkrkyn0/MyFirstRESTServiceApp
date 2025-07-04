package net.javaguides.springboot_first_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers") // Tüm endpointler için bir ana yol tanımladım
public class Controller {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/add")
    public String addCustomer(@RequestParam String first, @RequestParam String last) {
        Customer customer = new Customer();
        customer.setFirstName(first);
        customer.setLastName(last);
        customerRepository.save(customer);
        return "Added new customer to repo!";
    }

    @GetMapping("/list")
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/find/{id}")
    public Customer findCustomerById(@PathVariable Integer id) {
        return customerRepository.findCustomerById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCustomerByID(@PathVariable Integer id){
        customerRepository.deleteById(id);
        return "Deleted customer with id: " + id;
    }
    @PostMapping
    public String addCustomer(@RequestBody Customer customer){
        customerRepository.save(customer);
        return "Added new customer to repo!";
    }
}
