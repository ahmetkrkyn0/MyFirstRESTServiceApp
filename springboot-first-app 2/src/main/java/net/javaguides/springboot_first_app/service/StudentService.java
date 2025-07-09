package net.javaguides.springboot_first_app.service;

import net.javaguides.springboot_first_app.bean.Student;
import net.javaguides.springboot_first_app.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student){
        return studentRepository.save(student);
    }

    public Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id){
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    public void deleteStudentById(Integer id){
        studentRepository.deleteById(id);
    }
    public Student updateStudent(Integer id, Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isPresent()) {
            Student existingStudent = optionalStudent.get();
            existingStudent.setName(studentDetails.getName());
            existingStudent.setSurname(studentDetails.getSurname());
            existingStudent.setAge(studentDetails.getAge());
            existingStudent.setEmail(studentDetails.getEmail());
            existingStudent.setDegree(studentDetails.getDegree());
            existingStudent.setPhone_number(studentDetails.getPhone_number());

            return studentRepository.save(existingStudent);
        } else {
            return null;
        }
    }
}
