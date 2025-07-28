package net.javaguides.springboot_first_app.service;

import net.javaguides.springboot_first_app.bean.Student;
import net.javaguides.springboot_first_app.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired // @Autowired annotation'ı constructor'a eklendi
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void deleteStudentById(Integer id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Integer id, Student updatedStudent) {
        updatedStudent.setId(id);
        return studentRepository.save(updatedStudent);
    }
}