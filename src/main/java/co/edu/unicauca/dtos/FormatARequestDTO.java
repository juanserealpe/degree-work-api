package co.edu.unicauca.dtos;

import co.edu.unicauca.enums.TypeProcess;

import java.util.Date;

public class FormatARequestDTO {
    private String url;
    private TypeProcess typeProcess = TypeProcess.FORMAT_A;
    private Date created_at;


    public FormatARequestDTO(){
    }



}
