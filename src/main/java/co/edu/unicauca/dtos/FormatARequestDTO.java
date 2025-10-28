package co.edu.unicauca.dtos;

import co.edu.unicauca.enums.ProcessType;

import java.util.Date;

public class FormatARequestDTO {
    private String url;
    private ProcessType typeProcess = ProcessType.FORMAT_A;
    private Date created_at;


    public FormatARequestDTO(){
    }



}
