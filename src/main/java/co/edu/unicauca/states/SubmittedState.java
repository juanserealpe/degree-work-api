package co.edu.unicauca.states;

import co.edu.unicauca.entities.FormatA;
import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.exceptions.StatusProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class SubmittedState implements IProcessState {

    @Override
    public void submit(Process process) {
        throw new StatusProcessException("Process is already submitted and pending review.");
    }

    @Override
    public void approve(Process process) {
        process.setProcessStatus(ProcessStatus.APPROVED);
    }

    @Override
    public void reject(Process process, String observation) {
        if (observation == null || observation.isBlank()) {
            throw new StatusProcessException("Rejection requires an observation.");
        }

        process.addObservation(observation);

        if (process instanceof FormatA formatA) {
            byte attempts = (byte) (formatA.getFailedAttempts() + 1);
            formatA.setFailedAttempts(attempts);

            if (attempts >= 3) {
                process.setProcessStatus(ProcessStatus.FAILED);
                return;
            }
        }

        process.setProcessStatus(ProcessStatus.REJECTED);
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StatusProcessException("Cannot resubmit while under review. Wait for approval or rejection.");
    }

    @Override
    public void assignJury(Process process) {
        throw new StatusProcessException("Cannot assign jury until the draft is approved.");
    }

    @Override
    public ProcessStatus getStatus() {
        return ProcessStatus.SUBMITTED;
    }
}