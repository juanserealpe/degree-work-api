package co.edu.unicauca.states;

import co.edu.unicauca.entities.FormatA;
import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.exceptions.StatusProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class PendingState implements IProcessState {


    @Override
    public void approve(Process process) {
        process.setProcessStatus(ProcessStatus.APPROVATED);
    }

    @Override
    public void reject(Process process, String observation) {
        process.addObservation(observation);

        if (process instanceof FormatA formatA) {
            byte attempts = (byte) (formatA.getFailedAttempts() + 1);
            formatA.setFailedAttempts(attempts);

            if (attempts >= 3) {
                process.setProcessStatus(ProcessStatus.REJECTED_DEF);
                return;
            }
        }

        process.setProcessStatus(ProcessStatus.REJECTED_TEMP);
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StatusProcessException("Cannot resubmit while pending review.");
    }

    @Override
    public ProcessStatus getStatus() {
        return ProcessStatus.PENDING;
    }
}
