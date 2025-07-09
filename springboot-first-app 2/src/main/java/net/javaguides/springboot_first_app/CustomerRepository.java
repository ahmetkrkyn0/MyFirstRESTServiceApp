package net.javaguides.springboot_first_app;

import net.javaguides.springboot_first_app.bean.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    Customer findCustomerById(Integer id);
}