package co.edu.unicauca.services.degreework;

import co.edu.unicauca.exceptions.DegreeWorkException;
import co.edu.unicauca.repositories.UserRepository;
import co.edu.unicauca.validators.DegreeWorkValidator;
import co.edu.unicauca.dtos.degreework.DegreeWorkRequestDTO;
import co.edu.unicauca.entities.DegreeWork;
import co.edu.unicauca.entities.User;
import co.edu.unicauca.repositories.DegreeWorkRepository;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DegreeWorkService {
    @Autowired
    private DegreeWorkRepository _degreeWorkRepository;
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private DegreeWorkValidator _validator;

    @Transactional
    public DegreeWork createDegreeWork(DegreeWorkRequestDTO dto) {
        Logger.info(getClass(), "Starting creation DegreeWork with title: " + dto.getTittle());
        _validator.validateRequest(dto);

        boolean titleExists = _degreeWorkRepository.findByTittle(dto.getTittle()).isPresent();
        _validator.validateUniqueTitle(titleExists, dto.getTittle());

        User coordinator = _userRepository.findByAccount_Email(dto.getCoordinatorEmail())
                .orElseThrow(() -> new DegreeWorkException("Coordinator not found: " + dto.getCoordinatorEmail()));

        User director = _userRepository.findByAccount_Email(dto.getDirectorEmail())
                .orElseThrow(() -> new DegreeWorkException("Director not found: " + dto.getDirectorEmail()));

        List<User> students = _userRepository.findByAccount_EmailIn(dto.getStudentEmails());
        _validator.validateUsers(coordinator, director, students, dto.getStudentEmails());

        DegreeWork degreeWork = new DegreeWork();
        degreeWork.setModality(dto.getModality());
        degreeWork.setTittle(dto.getTittle());
        degreeWork.addStudents(students);
        degreeWork.setDirector(director);
        degreeWork.setCoordinator(coordinator);

        DegreeWork saved = _degreeWorkRepository.save(degreeWork);
        Logger.success(getClass(), "DegreeWork created successfully with ID: " + saved.getIdDegreeWork());
        return saved;
    }
}
