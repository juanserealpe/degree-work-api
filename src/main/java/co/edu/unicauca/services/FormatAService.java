package co.edu.unicauca.services;

import co.edu.unicauca.dtos.AssignJuryRequestDTO;
import co.edu.unicauca.dtos.ProcessRequestDTO;
import co.edu.unicauca.entities.DegreeWork;
import co.edu.unicauca.entities.Draft;
import co.edu.unicauca.entities.FormatA;
import co.edu.unicauca.entities.Process;
import co.edu.unicauca.exceptions.DegreeWorkException;
import co.edu.unicauca.exceptions.ProcessException;
import co.edu.unicauca.interfaces.IProcessService;
import co.edu.unicauca.repositories.DegreeWorkRepository;
import co.edu.unicauca.repositories.FormatARepository;
import co.edu.unicauca.repositories.ProcessRepository;
import co.edu.unicauca.utilities.Logger;
import co.edu.unicauca.validators.FormatAValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FormatAService implements IProcessService {
    @Autowired
    private FormatARepository _formatARepository;
    @Autowired
    private ProcessRepository _processRepository;
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

        _validator.validateRequest(dto);
        _validator.validateWithDegreeWork(dto, degreeWork);

        FormatA formatA = new FormatA(degreeWork);
        formatA.setUrl(dto.getUrl());
        formatA.submit();

        Logger.success(getClass(), "Format A created successfully with ID: " + formatA.getId());
        return _formatARepository.save(formatA);
    }


    @Override
    public Process approveProcess(Long processId) {
        Process process = _processRepository.findById(processId)
                .orElseThrow(() -> new ProcessException("Process not found"));

        if (!(process instanceof FormatA formatA)) {
            throw new ProcessException("Process with id " + processId + " is not FormatA");
        }

        formatA.approve();

        return _formatARepository.save(formatA);
    }

    @Override
    public Process rejectProcess(Long processId, String observation) {
        FormatA formatA = _formatARepository.findById(processId)
                .orElseThrow(() -> new ProcessException(
                        "Format A not found with id: " + processId
                ));

        formatA.reject(observation);
        return _formatARepository.save(formatA);
    }

    @Override
    public Process resubmitProcess(Long processId, String newUrl) {
        FormatA formatA = _formatARepository.findById(processId)
                .orElseThrow(() -> new ProcessException(
                        "Format A not found with id: " + processId
                ));

        formatA.resubmit(newUrl);

        return _formatARepository.save(formatA);
    }

    @Override
    public Draft assignJuryToDraft(AssignJuryRequestDTO request) {
        throw new ProcessException("you cannot assign jury to format A");
    }
}
