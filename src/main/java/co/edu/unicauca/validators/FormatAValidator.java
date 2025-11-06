package co.edu.unicauca.validators;

import co.edu.unicauca.dtos.processes.ProcessRequestDTO;
import co.edu.unicauca.entities.DegreeWork;
import co.edu.unicauca.entities.FormatA;
import co.edu.unicauca.enums.DegreeWorkStatus;
import co.edu.unicauca.exceptions.DegreeWorkException;
import co.edu.unicauca.utilities.Logger;
import org.springframework.stereotype.Component;


@Component
public class FormatAValidator {

    public void validateRequest(ProcessRequestDTO dto) {
        Logger.info(getClass(), "Starting DegreeWork request validation.");

        if (dto == null)
            throw new DegreeWorkException("The request body cannot be null.");
    }

    public void validateWithDegreeWork(ProcessRequestDTO dto, DegreeWork degreeWork) {
        boolean alreadyHasFormatA = degreeWork.getProcesses().stream()
                .anyMatch(p -> p instanceof FormatA);

        if (alreadyHasFormatA) {
            throw new DegreeWorkException("The DegreeWork already has a FormatA process.");
        }
        if(degreeWork.getStatus().equals(DegreeWorkStatus.APPROVED)){
            throw new DegreeWorkException("Thes DegreeWork already is approved");
        }
        if(degreeWork.getStatus().equals(DegreeWorkStatus.REJECTED)){
            throw new DegreeWorkException("Thes DegreeWork already is rejected");
        }
    }


}
