package co.edu.unicauca.interfaces;

import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.entities.Process;

public interface IProcessState {
    void approve(Process process);
    void reject(Process process, String observation);
    void resubmit(Process process, String newUrl);

    ProcessStatus getStatus();
}
