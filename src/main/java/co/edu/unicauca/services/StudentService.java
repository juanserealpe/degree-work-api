package co.edu.unicauca.services;

import co.edu.unicauca.entities.Student;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.repositories.StudentRepository;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AccountService accountService;

    @Transactional
    public Student registerStudent(Student student) {
        Logger.info(getClass(), "Attempting to register new student: "
                + student.getAccount().getEmail());

        accountService.validateEmailNotExists(student.getAccount().getEmail());
        accountService.prepareAccountForRegistration(student.getAccount());
        student.getAccount().setRole(Role.STUDENT);
        validateStudentData(student);
        Student saved = studentRepository.save(student);
        Logger.success(getClass(), "Student registered successfully. ID: "
                + saved.getIdPerson());

        return saved;
    }

    private void validateStudentData(Student student) {
        if (student.getNames() == null || student.getNames().isBlank())
                throw new IllegalArgumentException("Student names are required");

        if (student.getLastNames() == null || student.getLastNames().isBlank())
                throw new IllegalArgumentException("Student last names are required");
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
    }
}
