package co.edu.unicauca.interfaces;

import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.entities.Process;

public interface IProcessState {
    // Enviar para evaluación
    void submit(Process process);

    // Aprobar el proceso
    void approve(Process process);

    // Rechazar con observaciones
    void reject(Process process, String observation);

    // Reenviar después de corregir
    void resubmit(Process process, String newUrl);

    // Solo para Draft - asignar jurados
    void assignJury(Process process);

    // Obtener el estado actual
    ProcessStatus getStatus();
}