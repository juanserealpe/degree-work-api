package co.edu.unicauca.dtos;

import java.util.List;

public class AssignJuryRequestDTO {
    private Long idProcess;
    private List<String> emailEvaluator;

    //Getters & setters

    public Long getIdProcess() {return idProcess;}
    public void setIdProcess(Long idProcess) {this.idProcess = idProcess;}

    public List<String> getEmailEvaluator() {return emailEvaluator;}
    public void setEmailEvaluator(List<String> emailEvaluator) {this.emailEvaluator = emailEvaluator;}
}
