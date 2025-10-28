package co.edu.unicauca.services;

import co.edu.unicauca.dtos.ProcessRequestDTO;
import co.edu.unicauca.entities.DegreeWork;
import co.edu.unicauca.entities.Draft;
import co.edu.unicauca.entities.FormatA;
import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.enums.ProcessType;
import co.edu.unicauca.exceptions.DegreeWorkException;
import co.edu.unicauca.interfaces.IProcessService;
import co.edu.unicauca.repositories.DegreeWorkRepository;
import co.edu.unicauca.repositories.FormatARepository;
import co.edu.unicauca.utilities.Logger;
import co.edu.unicauca.validators.FormatAValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormatAService implements IProcessService {
    @Autowired
    private FormatARepository _formatARepository;
    @Autowired
    private DegreeWorkRepository _degreeWorkRepository;
    @Autowired
    private FormatAValidator _validator;

    @Override
    public Process submitProcess(ProcessRequestDTO dto) {
        Logger.info(getClass(), "Starting creation Format A for DegreeWork");
        DegreeWork degreeWork = _degreeWorkRepository.findById(dto.getIdDegreeWork())
                .orElseThrow(() -> new DegreeWorkException(
                        "DegreeWork not found with id: " + dto.getIdDegreeWork()
                ));

        //_validator.validateRequest(dto);
        //_validator.validateWithDegreeWork(dto, degreeWork);

        FormatA formatA = new FormatA(degreeWork);
        formatA.setUrl(dto.getUrl());
        formatA.setProcessStatus(ProcessStatus.SUBMITTED);
        formatA.setProcess(ProcessType.FORMAT_A);

        Logger.success(getClass(), "Format A created successfully with ID: " + formatA.getId());
        return _formatARepository.save(formatA);
    }


    @Override
    public Process approveProcess(Long processId) {
        return null;
    }

    @Override
    public Process rejectProcess(Long processId, String observation) {
        return null;
    }

    @Override
    public Process resubmitProcess(Long processId, String newUrl) {
        return null;
    }

    @Override
    public Draft assignJuryToDraft(Long draftId) {
        return null;
    }
}
