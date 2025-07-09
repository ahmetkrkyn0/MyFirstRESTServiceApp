package net.javaguides.springboot_first_app.repository;

import net.javaguides.springboot_first_app.bean.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

}