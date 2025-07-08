package net.javaguides.springboot_first_app.service;

import net.javaguides.springboot_first_app.Customer;
import net.javaguides.springboot_first_app.CustomerRepository;
import net.javaguides.springboot_first_app.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerById(Integer id){
        return customerRepository.findById(id)
                                 .orElseThrow(() -> new CustomerNotFoundException("ID'si" + id + "olan müşteri bulunamadı."));

    }
    public Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Integer id, Customer updatedCustomer) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Güncellenmek istenen ID'si " + id + " olan müşteri bulunamadı.");
        }
        updatedCustomer.setId(id);
        return customerRepository.save(updatedCustomer);
    }

    public void deleteCustomer(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Silinmek istenen ID'si " + id + " olan müşteri bulunamadı.");
        }
        customerRepository.deleteById(id);
    }

}
