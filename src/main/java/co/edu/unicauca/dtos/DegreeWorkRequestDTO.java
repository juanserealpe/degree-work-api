package co.edu.unicauca.dtos;

import co.edu.unicauca.enums.Modality;
import java.util.List;

public class DegreeWorkRequestDTO {
    private Modality modality;
    private String tittle;
    private String coordinatorEmail;
    private String directorEmail;
    private List<String> studentEmails;

    // Getters & setters
    public Modality getModality() { return modality; }
    public void setModality(Modality modality) { this.modality = modality; }

    public String getCoordinatorEmail() { return coordinatorEmail; }
    public void setCoordinatorEmail(String coordinatorEmail) { this.coordinatorEmail = coordinatorEmail; }

    public String getDirectorEmail() { return directorEmail; }
    public void setDirectorEmail(String directorEmail) { this.directorEmail = directorEmail; }

    public List<String> getStudentEmails() { return studentEmails; }
    public void setStudentEmails(List<String> studentEmails) { this.studentEmails = studentEmails; }

    public String getTittle(){return tittle;}
    public void setTittle(String tittle){this.tittle = tittle;}
}
