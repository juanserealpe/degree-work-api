package co.edu.unicauca.dtos;

import co.edu.unicauca.enums.DegreeWorkStatus;
import co.edu.unicauca.enums.Modality;
import java.util.List;

public class DegreeWorkResponseDTO {
    private Modality modality;
    private String tittle;
    private String coordinatorFullName;
    private String directorFullName;
    private List<String> studentEmails;
    private DegreeWorkStatus degreeWorkStatus;

    // Getters & setters
    public Modality getModality() { return modality; }
    public void setModality(Modality modality) { this.modality = modality; }

    public String getCoordinatorFullName() { return coordinatorFullName; }
    public void setCoordinatorFullName(String coordinatorFullName) { this.coordinatorFullName = coordinatorFullName; }

    public String getDirectorEmail() { return directorFullName; }
    public void setDirectorEmail(String directorEmail) { this.directorFullName = directorEmail; }

    public List<String> getStudentEmails() { return studentEmails; }
    public void setStudentEmails(List<String> studentEmails) { this.studentEmails = studentEmails; }

    public String getTittle(){return tittle;}
    public void setTittle(String tittle){this.tittle = tittle;}
}
