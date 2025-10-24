package co.edu.unicauca.validators;

import co.edu.unicauca.dtos.DegreeWorkRequestDTO;
import co.edu.unicauca.entities.User;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.exceptions.DegreeWorkException;
import co.edu.unicauca.exceptions.InvalidRoleException;
import co.edu.unicauca.exceptions.UserNotFoundException;
import co.edu.unicauca.repositories.UserRepository;
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
        if (dto == null)
            throw new DegreeWorkException("The request body cannot be null.");

        if (dto.getModality() == null)
            throw new DegreeWorkException("The modality is required.");

        if (dto.getTittle() == null || dto.getTittle().trim().isEmpty())
            throw new DegreeWorkException("The tittle is required.");

        if (dto.getCoordinatorEmail() == null || dto.getCoordinatorEmail().isBlank())
            throw new DegreeWorkException("Coordinator email is required.");

        if (dto.getDirectorEmail() == null || dto.getDirectorEmail().isBlank())
            throw new DegreeWorkException("Director email is required.");

        if (dto.getStudentEmails() == null || dto.getStudentEmails().isEmpty())
            throw new DegreeWorkException("At least one student email is required.");
    }

    public User validateCoordinator(String email) {
        User coordinator = _userRepository.findByAccount_Email(email)
                .orElseThrow(() -> new UserNotFoundException("Coordinator not found: " + email));

        if (!hasRole(coordinator.getAccount().getRoles(), Role.COORDINATOR))
            throw new InvalidRoleException("User " + email + " is not a coordinator.");

        return coordinator;
    }

    public User validateDirector(String email) {
        User director = _userRepository.findByAccount_Email(email)
                .orElseThrow(() -> new UserNotFoundException("Director not found: " + email));

        if (!hasRole(director.getAccount().getRoles(), Role.DIRECTOR))
            throw new InvalidRoleException("User " + email + " is not a director.");

        return director;
    }

    public List<User> validateStudents(List<String> emails) {
        List<User> students = _userRepository.findByAccount_EmailIn(emails);

        if (students.size() > 2)
            throw new DegreeWorkException("The degree work just allows max 2 students.");

        for (User student : students) {
            if (!hasRole(student.getAccount().getRoles(), Role.STUDENT))
                throw new InvalidRoleException("User " + student.getAccount().getEmail() + " is not a student.");
            if (!student.getEnrolledWorks().isEmpty())
                throw new DegreeWorkException("Student " + student.getAccount().getEmail() + " already has a degree work.");
        }

        return students;
    }

    private boolean hasRole(Set<Role> roles, Role expectedRole) {
        return roles.contains(expectedRole);
    }
}
