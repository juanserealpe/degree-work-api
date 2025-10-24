package co.edu.unicauca.services;

import co.edu.unicauca.validators.DegreeWorkValidator;
import co.edu.unicauca.dtos.DegreeWorkRequestDTO;
import co.edu.unicauca.entities.DegreeWork;
import co.edu.unicauca.entities.User;
import co.edu.unicauca.repositories.DegreeWorkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DegreeWorkService {

    private final DegreeWorkRepository _degreeWorkRepository;
    private final DegreeWorkValidator  _validator;

    public DegreeWorkService(DegreeWorkRepository degreeWorkRepository, DegreeWorkValidator validator) {
        this._degreeWorkRepository = degreeWorkRepository;
        this._validator = validator;
    }

    @Transactional
    public DegreeWork createDegreeWork(DegreeWorkRequestDTO dto) {
        _validator.validateRequest(dto);

        User coordinator = _validator.validateCoordinator(dto.getCoordinatorEmail());
        User director = _validator.validateDirector(dto.getDirectorEmail());
        List<User> students = _validator.validateStudents(dto.getStudentEmails());

        DegreeWork degreeWork = new DegreeWork();
        degreeWork.setModality(dto.getModality());
        degreeWork.setTittle(dto.getTittle());
        degreeWork.addStudents(students);
        degreeWork.setDirector(director);
        degreeWork.setCoordinator(coordinator);

        return _degreeWorkRepository.save(degreeWork);
    }
}
