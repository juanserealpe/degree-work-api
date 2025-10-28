package co.edu.unicauca.services;

import co.edu.unicauca.entities.FormatA;
import co.edu.unicauca.entities.Draft;
import co.edu.unicauca.entities.Process;
import co.edu.unicauca.repositories.FormatARepository;
import co.edu.unicauca.repositories.DraftRepository;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para gestionar las transiciones de estado de los procesos.
 * Usa el patrón State para delegar las validaciones y transiciones.
 */
@Service
public class ProcessService {

    @Autowired
    private FormatARepository _formatARepository;

    @Autowired
    private DraftRepository _draftRepository;

    /**
     * Envía un proceso para evaluación.
     */
    @Transactional
    public Process submitProcess(Long processId) {
        Process process = findProcess(processId);
        Logger.info(getClass(), "Submitting process ID: " + processId);

        process.submit();

        return saveProcess(process);
    }

    /**
     * Aprueba un proceso (solo coordinador).
     */
    @Transactional
    public Process approveProcess(Long processId) {
        Process process = findProcess(processId);
        Logger.info(getClass(), "Approving process ID: " + processId);

        process.approve();

        return saveProcess(process);
    }

    /**
     * Rechaza un proceso con observaciones.
     */
    @Transactional
    public Process rejectProcess(Long processId, String observation) {
        Process process = findProcess(processId);
        Logger.info(getClass(), "Rejecting process ID: " + processId);

        process.reject(observation);

        return saveProcess(process);
    }

    /**
     * Reenvía un proceso corregido.
     */
    @Transactional
    public Process resubmitProcess(Long processId, String newUrl) {
        Process process = findProcess(processId);
        Logger.info(getClass(), "Resubmitting process ID: " + processId);

        process.resubmit(newUrl);

        return saveProcess(process);
    }

    /**
     * Asigna jurados a un Draft aprobado.
     */
    @Transactional
    public Draft assignJuryToDraft(Long draftId) {
        Draft draft = _draftRepository.findById(draftId)
                .orElseThrow(() -> new RuntimeException("Draft not found"));

        Logger.info(getClass(), "Assigning jury to Draft ID: " + draftId);

        draft.assignJury();

        return _draftRepository.save(draft);
    }

    private Process findProcess(Long processId) {

        Draft responeDraft = _draftRepository.getById(processId);
        FormatA responseFormatA = _formatARepository.getById(processId);

        return _formatARepository.findById(processId)
                .map(p -> (Process) p)
                .orElseGet(() -> _draftRepository.findById(processId)
                        .map(p -> (Process) p)
                        .orElseThrow(() -> new RuntimeException("Process not found.")));
    }

    private Process saveProcess(Process process) {
        if (process instanceof FormatA)
            return _formatARepository.save((FormatA) process);
        if(process instanceof  Draft)
            return _draftRepository.save((Draft) process);

        throw new RuntimeException("Unknown process type.");
    }
}