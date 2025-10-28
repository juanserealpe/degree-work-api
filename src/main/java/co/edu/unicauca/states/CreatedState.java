package co.edu.unicauca.states;

import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.exceptions.StatusProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class CreatedState implements IProcessState {

    @Override
    public void submit(Process process) {
        // Validar que tenga URL
        if (process.getUrl() == null || process.getUrl().isBlank()) {
            throw new StatusProcessException("Cannot submit process without a document URL.");
        }
        process.setProcessStatus(ProcessStatus.SUBMITTED);
    }

    @Override
    public void approve(Process process) {
        throw new StatusProcessException("Cannot approve a process that hasn't been submitted yet.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StatusProcessException("Cannot reject a process that hasn't been submitted yet.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StatusProcessException("Cannot resubmit. Use submit() instead.");
    }

    @Override
    public void assignJury(Process process) {
        throw new StatusProcessException("Cannot assign jury to a non-approved draft.");
    }

    @Override
    public ProcessStatus getStatus() {
        return ProcessStatus.CREATED;
    }
}