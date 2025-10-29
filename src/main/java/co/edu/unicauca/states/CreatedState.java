package co.edu.unicauca.states;

import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessState;
import co.edu.unicauca.exceptions.StateProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class CreatedState implements IProcessState {

    @Override
    public void submit(Process process) {
        // Validar que tenga URL
        if (process.getUrl() == null || process.getUrl().isBlank()) {
            throw new StateProcessException("Cannot submit process without a document URL.");
        }
        process.setProcessState(ProcessState.SUBMITTED);
    }

    @Override
    public void approve(Process process) {
        throw new StateProcessException("Cannot approve a process that hasn't been submitted yet.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StateProcessException("Cannot reject a process that hasn't been submitted yet.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StateProcessException("Cannot resubmit. Use submit() instead.");
    }

    @Override
    public void assignJury(Process process) {
        throw new StateProcessException("Cannot assign jury to a non-approved draft.");
    }

    @Override
    public ProcessState getStatus() {
        return ProcessState.CREATED;
    }
}