package co.edu.unicauca.states;

import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.exceptions.StatusProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class RejectedState implements IProcessState {

    @Override
    public void submit(Process process) {
        throw new StatusProcessException("Cannot submit. Use resubmit() to send corrections.");
    }

    @Override
    public void approve(Process process) {
        throw new StatusProcessException("Rejected process must be resubmitted before approval.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StatusProcessException("Process is already rejected.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        if (newUrl == null || newUrl.isBlank()) {
            throw new StatusProcessException("New URL is required for resubmission.");
        }

        process.setUrl(newUrl);
        process.setProcessStatus(ProcessStatus.SUBMITTED);
    }

    @Override
    public void assignJury(Process process) {
        throw new StatusProcessException("Cannot assign jury to a rejected process.");
    }

    @Override
    public ProcessStatus getStatus() {
        return ProcessStatus.REJECTED;
    }
}