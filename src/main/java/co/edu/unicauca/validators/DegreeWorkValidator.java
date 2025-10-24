package co.edu.unicauca.validators;

import co.edu.unicauca.dtos.DegreeWorkRequestDTO;
import co.edu.unicauca.entities.User;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.exceptions.DegreeWorkException;
import co.edu.unicauca.exceptions.RoleException;
import co.edu.unicauca.exceptions.UserException;
import co.edu.unicauca.repositories.UserRepository;
import co.edu.unicauca.utilities.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DegreeWorkValidator {

    private final UserRepository _userRepository;

    public DegreeWorkValidator(UserRepository userRepository) {
        this._userRepository = userRepository;
    }

    public void validateRequest(DegreeWorkRequestDTO dto) {
        Logger.info(getClass(), "Starting DegreeWork request validation.");

        if (dto == null) {
            Logger.error(getClass(), "The request body is null.");
            throw new DegreeWorkException("The request body cannot be null.");
        }

        if (dto.getModality() == null) {
            Logger.error(getClass(), "Modality is missing.");
            throw new DegreeWorkException("The modality is required.");
        }

        if (dto.getTittle() == null || dto.getTittle().trim().isEmpty()) {
            Logger.error(getClass(), "Tittle is empty or null.");
            throw new DegreeWorkException("The tittle is required.");
        }

        if (dto.getCoordinatorEmail() == null || dto.getCoordinatorEmail().isBlank()) {
            Logger.error(getClass(), "Coordinator email is missing.");
            throw new DegreeWorkException("Coordinator email is required.");
        }

        if (dto.getDirectorEmail() == null || dto.getDirectorEmail().isBlank()) {
            Logger.error(getClass(), "Director email is missing.");
            throw new DegreeWorkException("Director email is required.");
        }

        if (dto.getStudentEmails() == null || dto.getStudentEmails().isEmpty()) {
            Logger.error(getClass(), "No student emails provided.");
            throw new DegreeWorkException("At least one student email is required.");
        }

        Logger.success(getClass(), "DegreeWorkRequestDTO basic validation passed.");
    }

    public User validateCoordinator(String email) {
        Logger.info(getClass(), "Validating coordinator with email: " + email);

        User coordinator = _userRepository.findByAccount_Email(email)
                .orElseThrow(() -> {
                    Logger.error(getClass(), "Coordinator not found: " + email);
                    return new UserException("Coordinator not found: " + email);
                });

        if (!hasRole(coordinator.getAccount().getRoles(), Role.COORDINATOR)) {
            Logger.error(getClass(), "Invalid role for coordinator: " + email);
            throw new RoleException("User " + email + " is not a coordinator.");
        }

        Logger.success(getClass(), "Coordinator validated successfully: " + email);
        return coordinator;
    }

    public User validateDirector(String email) {
        Logger.info(getClass(), "Validating director with email: " + email);

        User director = _userRepository.findByAccount_Email(email)
                .orElseThrow(() -> {
                    Logger.error(getClass(), "Director not found: " + email);
                    return new UserException("Director not found: " + email);
                });

        if (!hasRole(director.getAccount().getRoles(), Role.DIRECTOR)) {
            Logger.error(getClass(), "Invalid role for director: " + email);
            throw new RoleException("User " + email + " is not a director.");
        }

        Logger.success(getClass(), "Director validated successfully: " + email);
        return director;
    }

    public List<User> validateStudents(List<String> emails) {
        Logger.info(getClass(), "Validating student list: " + emails);

        List<User> students = _userRepository.findByAccount_EmailIn(emails);

        if (students.size() > 2) {
            Logger.error(getClass(), "More than two students provided.");
            throw new DegreeWorkException("The degree work just allows max 2 students.");
        }

        for (User student : students) {
            if (!hasRole(student.getAccount().getRoles(), Role.STUDENT)) {
                Logger.error(getClass(), "Invalid role for student: " + student.getAccount().getEmail());
                throw new RoleException("User " + student.getAccount().getEmail() + " is not a student.");
            }

            if (!student.getEnrolledWorks().isEmpty()) {
                Logger.error(getClass(), "Student already has a degree work: " + student.getAccount().getEmail());
                throw new DegreeWorkException("Student " + student.getAccount().getEmail() + " already has a degree work.");
            }
        }

        Logger.success(getClass(), "All students validated successfully.");
        return students;
    }

    protected boolean hasRole(Set<Role> roles, Role expectedRole) {
        return roles.contains(expectedRole);
    }
}
