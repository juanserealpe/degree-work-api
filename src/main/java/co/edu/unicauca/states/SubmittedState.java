package co.edu.unicauca.states;

import co.edu.unicauca.entities.FormatA;
import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessState;
import co.edu.unicauca.exceptions.StateProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class SubmittedState implements IProcessState {

    @Override
    public void submit(Process process) {
        throw new StateProcessException("Process is already submitted and pending review.");
    }

    @Override
    public void approve(Process process) {
        process.setProcessState(ProcessState.APPROVED);
    }

    @Override
    public void reject(Process process, String observation) {
        if (observation == null || observation.isBlank()) {
            throw new StateProcessException("Rejection requires an observation.");
        }

        process.addObservation(observation);

        if (process instanceof FormatA formatA) {
            byte attempts = (byte) (formatA.getFailedAttempts() + 1);
            formatA.setFailedAttempts(attempts);

            if (attempts >= 3) {
                process.setProcessState(ProcessState.FAILED);
                return;
            }
        }

        process.setProcessState(ProcessState.REJECTED);
    }

    @Override
    public void resubmit(Process process, String newUrl) {

        if(process instanceof FormatA formatA){
            if(formatA.getFailedAttempts() == 0)
                throw new StateProcessException("the format_a should have at least 1 failed attempt.");
        }

        throw new StateProcessException("Cannot resubmit while under review. Wait for approval or rejection.");
    }

    @Override
    public void assignJury(Process process) {
        throw new StateProcessException("Cannot assign jury until the draft is approved.");
    }

    @Override
    public ProcessState getStatus() {
        return ProcessState.SUBMITTED;
    }
}