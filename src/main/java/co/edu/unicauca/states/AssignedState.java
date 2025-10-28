package co.edu.unicauca.states;

import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.exceptions.StatusProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class AssignedState implements IProcessState {

    @Override
    public void submit(Process process) {
        throw new StatusProcessException("Process is already finalized with jury assignment.");
    }

    @Override
    public void approve(Process process) {
        throw new StatusProcessException("Process is already approved and assigned.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StatusProcessException("Cannot reject an assigned process.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StatusProcessException("Cannot resubmit an assigned process.");
    }

    @Override
    public void assignJury(Process process) {
        throw new StatusProcessException("Jury has already been assigned.");
    }

    @Override
    public ProcessStatus getStatus() {
        return ProcessStatus.ASSIGNED;
    }
}