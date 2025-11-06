package co.edu.unicauca.dtos.processes;

import co.edu.unicauca.enums.ProcessType;

public class ProcessRequestDTO {
    private long idDegreeWork;
    private String url;
    private ProcessType typeProcess;

    public ProcessRequestDTO(){
    }

    public ProcessRequestDTO(long idDegreeWork, String url, ProcessType typeProcess) {
        this.url = url;
        this.typeProcess = typeProcess;
        this.idDegreeWork = idDegreeWork;
    }

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

    public ProcessType getTypeProcess() {return typeProcess;}
    public void setTypeProcess(ProcessType typeProcess) {this.typeProcess = typeProcess;}

    public long getIdDegreeWork() {return idDegreeWork;}
    public void setIdDegreeWork(long idDegreeWork) {this.idDegreeWork = idDegreeWork;}
}
