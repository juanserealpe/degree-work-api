package co.edu.unicauca.states;

import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessState;
import co.edu.unicauca.exceptions.StateProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class RejectedState implements IProcessState {

    @Override
    public void submit(Process process) {
        throw new StateProcessException("Cannot submit. Use resubmit() to send corrections.");
    }

    @Override
    public void approve(Process process) {
        throw new StateProcessException("Rejected process must be resubmitted before approval.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StateProcessException("Process is already rejected.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        if (newUrl == null || newUrl.isBlank()) {
            throw new StateProcessException("New URL is required for resubmission.");
        }

        process.setUrl(newUrl);
        process.setProcessState(ProcessState.SUBMITTED);
    }

    @Override
    public void assignJury(Process process) {
        throw new StateProcessException("Cannot assign jury to a rejected process.");
    }

    @Override
    public ProcessState getStatus() {
        return ProcessState.REJECTED;
    }
}