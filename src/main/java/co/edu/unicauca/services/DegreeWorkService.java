package co.edu.unicauca.services;

import co.edu.unicauca.validators.DegreeWorkValidator;
import co.edu.unicauca.dtos.DegreeWorkRequestDTO;
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
    private DegreeWorkValidator  _validator;

    @Transactional
    public DegreeWork createDegreeWork(DegreeWorkRequestDTO dto) {
        Logger.info(DegreeWorkService.class, "Starting creation process for DegreeWork with title: " + dto.getTittle());

        try {
            _validator.validateRequest(dto);
            Logger.info(DegreeWorkService.class, "Request data validated successfully.");

            User coordinator = _validator.validateCoordinator(dto.getCoordinatorEmail());
            Logger.info(DegreeWorkService.class, "Coordinator validated: " + coordinator.getAccount().getEmail());

            User director = _validator.validateDirector(dto.getDirectorEmail());
            Logger.info(DegreeWorkService.class, "Director validated: " + director.getAccount().getEmail());

            List<User> students = _validator.validateStudents(dto.getStudentEmails());
            Logger.info(DegreeWorkService.class, students.size() + " students validated successfully.");

            DegreeWork degreeWork = new DegreeWork();
            degreeWork.setModality(dto.getModality());
            degreeWork.setTittle(dto.getTittle());
            degreeWork.addStudents(students);
            degreeWork.setDirector(director);
            degreeWork.setCoordinator(coordinator);

            DegreeWork saved = _degreeWorkRepository.save(degreeWork);
            Logger.success(DegreeWorkService.class, "DegreeWork created successfully with ID: " + saved.getIdDegreeWork());

            return saved;

        } catch (Exception e) {
            Logger.error(DegreeWorkService.class, "Error creating DegreeWork: " + e.getMessage());
            throw e;
        }
    }

}
