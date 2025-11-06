package co.edu.unicauca.services.degreework;

import co.edu.unicauca.dtos.AssignJuryRequestDTO;
import co.edu.unicauca.dtos.ProcessRequestDTO;
import co.edu.unicauca.entities.DegreeWork;
import co.edu.unicauca.entities.Draft;
import co.edu.unicauca.entities.Process;
import co.edu.unicauca.exceptions.DegreeWorkException;
import co.edu.unicauca.exceptions.ProcessException;
import co.edu.unicauca.interfaces.IProcessService;
import co.edu.unicauca.repositories.DegreeWorkRepository;
import co.edu.unicauca.repositories.DraftRepository;
import co.edu.unicauca.repositories.ProcessRepository;
import co.edu.unicauca.utilities.Logger;
import co.edu.unicauca.validators.DraftValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DraftService implements IProcessService {
    @Autowired
    private DraftRepository _draftRepository;
    @Autowired
    private ProcessRepository _processRepository;
    @Autowired
    private DegreeWorkRepository _degreeWorkRepository;
    @Autowired
    private DraftValidator _validator;

    @Override
    public Process submitProcess(ProcessRequestDTO dto) {
        Logger.info(getClass(), "Starting creation Draft for DegreeWork");
        DegreeWork degreeWork = _degreeWorkRepository.findById(dto.getIdDegreeWork())
                .orElseThrow(() -> new DegreeWorkException("DegreeWork not found with id: " + dto.getIdDegreeWork()));

        _validator.validateRequest(dto);
        _validator.validateWithDegreeWork(dto, degreeWork);

        Draft draft = new Draft(degreeWork);
        draft.setUrl(dto.getUrl());
        //draft.setProcessState(ProcessState.SUBMITTED);
        draft.setDeadline(LocalDateTime.now().plusMonths(6));
        draft.submit();

        Logger.success(getClass(), "Draft created successfully with ID: " + draft.getId());
        return _draftRepository.save(draft);
    }

    @Override
    public Process approveProcess(Long processId) {
        Draft draft = _draftRepository.findById(processId)
                .orElseThrow(() -> new ProcessException("Draft with that id doesn't exist"));
        draft.approve();
        return _draftRepository.save(draft);
    }

    @Override
    public Process rejectProcess(Long processId, String observation) {
        Draft draft = _draftRepository.findById(processId)
                .orElseThrow(() -> new ProcessException("Draft with that id doesn't exist"));
        draft.reject(observation);
        return _draftRepository.save(draft);
    }

    @Override
    public Process resubmitProcess(Long processId, String newUrl) {
        return null;
    }

    @Override
    public Draft assignJuryToDraft(AssignJuryRequestDTO request) {
        Draft draft = _draftRepository.findById(request.getIdProcess())
                .orElseThrow(() -> new ProcessException("Draft with that id doesn't exist"));

        draft.assignJury();

        return _draftRepository.save(draft);
    }
}
