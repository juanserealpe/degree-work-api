package co.edu.unicauca.interfaces;

import co.edu.unicauca.enums.ProcessState;
import co.edu.unicauca.entities.Process;

public interface IProcessState {
    void submit(Process process);
    void approve(Process process);
    void reject(Process process, String observation);
    void resubmit(Process process, String newUrl);
    void assignJury(Process process);
    ProcessState getStatus();
}