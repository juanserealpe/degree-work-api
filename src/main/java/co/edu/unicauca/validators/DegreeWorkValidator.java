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

    public void validateRequest(DegreeWorkRequestDTO dto) {
        Logger.info(getClass(), "Starting DegreeWork request validation.");

        if (dto == null)
            throw new DegreeWorkException("The request body cannot be null.");

        if (dto.getModality() == null)
            throw new DegreeWorkException("The modality is required.");

        if (dto.getTittle() == null || dto.getTittle().trim().isEmpty())
            throw new DegreeWorkException("The title is required.");

        if (dto.getCoordinatorEmail() == null || dto.getCoordinatorEmail().isBlank())
            throw new DegreeWorkException("Coordinator email is required.");

        if (dto.getDirectorEmail() == null || dto.getDirectorEmail().isBlank())
            throw new DegreeWorkException("Director email is required.");

        if (dto.getStudentEmails() == null || dto.getStudentEmails().isEmpty())
            throw new DegreeWorkException("At least one student email is required.");
    }

    public void validateUniqueTitle(boolean titleExists, String title) {
        if (titleExists)
            throw new DegreeWorkException("A DegreeWork with title '" + title + "' already exists.");
    }

    public void validateUsers(User coordinator, User director, List<User> students, List<String> requestedEmails) {

        if (!coordinator.getAccount().getRoles().contains(Role.COORDINATOR))
            throw new RoleException("User " + coordinator.getAccount().getEmail() + " is not a coordinator.");

        if (!director.getAccount().getRoles().contains(Role.DIRECTOR))
            throw new RoleException("User " + director.getAccount().getEmail() + " is not a director.");

        if (students.size() != requestedEmails.size()) {
            List<String> missing = requestedEmails.stream()
                    .filter(email -> students.stream()
                            .noneMatch(u -> u.getAccount().getEmail().equalsIgnoreCase(email)))
                    .toList();
            throw new DegreeWorkException("Students not found: " + missing);
        }

        if (students.size() > 2)
            throw new DegreeWorkException("The degree work allows max 2 students.");

        for (User s : students) {
            if (!s.getAccount().getRoles().contains(Role.STUDENT))
                throw new RoleException("User " + s.getAccount().getEmail() + " is not a student.");

            if (!s.getEnrolledWorks().isEmpty())
                throw new DegreeWorkException("Student " + s.getAccount().getEmail() + " already has a degree work.");
        }
    }
}
