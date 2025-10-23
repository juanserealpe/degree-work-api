package co.edu.unicauca.services;

import co.edu.unicauca.entities.User;
import co.edu.unicauca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private UserRepository _userRepository;

    @Transactional
    public List<User> getAllStudents() { return _userRepository.findAllStudents();}


}
