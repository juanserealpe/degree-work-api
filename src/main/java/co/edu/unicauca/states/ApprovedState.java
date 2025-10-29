package co.edu.unicauca.states;

import co.edu.unicauca.entities.Draft;
import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessState;
import co.edu.unicauca.exceptions.StateProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class ApprovedState implements IProcessState {

    @Override
    public void submit(Process process) {
        throw new StateProcessException("Approved process cannot be resubmitted.");
    }

    @Override
    public void approve(Process process) {
        throw new StateProcessException("Process is already approved.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StateProcessException("Cannot reject an approved process.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StateProcessException("Approved process cannot be resubmitted.");
    }

    @Override
    public void assignJury(Process process) {
        if (!(process instanceof Draft)) {
            throw new StateProcessException("Only Draft processes can have jury assignment.");
        }

        process.setProcessState(ProcessState.ASSIGNED);
    }

    @Override
    public ProcessState getStatus() {
        return ProcessState.APPROVED;
    }
}