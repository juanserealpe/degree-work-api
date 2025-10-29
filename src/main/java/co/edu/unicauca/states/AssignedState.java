package co.edu.unicauca.states;

import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessState;
import co.edu.unicauca.exceptions.StateProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class AssignedState implements IProcessState {

    @Override
    public void submit(Process process) {
        throw new StateProcessException("Process is already finalized with jury assignment.");
    }

    @Override
    public void approve(Process process) {
        throw new StateProcessException("Process is already approved and assigned.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StateProcessException("Cannot reject an assigned process.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StateProcessException("Cannot resubmit an assigned process.");
    }

    @Override
    public void assignJury(Process process) {
        throw new StateProcessException("Jury has already been assigned.");
    }

    @Override
    public ProcessState getStatus() {
        return ProcessState.ASSIGNED;
    }
}