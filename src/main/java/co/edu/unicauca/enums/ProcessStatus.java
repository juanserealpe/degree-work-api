package co.edu.unicauca.enums;

public enum ProcessStatus {
    CREATED,      // Estado inicial - documento subido
    SUBMITTED,    // Enviado para evaluación
    REJECTED,     // Rechazado con observaciones (puede reintentar)
    APPROVED,     // Aprobado
    ASSIGNED,     // Solo para Draft - jurados asignados
    FAILED        // Rechazado definitivamente (3 intentos)
}
