package co.edu.unicauca.interfaces;

import co.edu.unicauca.dtos.AssignJuryRequestDTO;
import co.edu.unicauca.dtos.ProcessRequestDTO;
import co.edu.unicauca.entities.Draft;
import co.edu.unicauca.entities.Process;
import org.springframework.transaction.annotation.Transactional;

public interface IProcessService {
    @Transactional
    public Process submitProcess(ProcessRequestDTO dto);
    @Transactional
    public Process approveProcess(Long processId);
    @Transactional
    public Process rejectProcess(Long processId, String observation);
    @Transactional
    public Process resubmitProcess(Long processId, String newUrl);
    @Transactional
    public Draft assignJuryToDraft(AssignJuryRequestDTO request);
}
