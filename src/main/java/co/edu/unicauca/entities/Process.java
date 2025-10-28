package co.edu.unicauca.entities;

import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.enums.ProcessType;
import co.edu.unicauca.interfaces.IProcessState;
import co.edu.unicauca.fabrics.ProcessStateFactory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "processes")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "process_type")
public abstract class Process {
    @Id
    @Column(name = "id_process")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "type_process")
    @Enumerated(EnumType.STRING)
    private ProcessType process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_degree_work")
    @JsonBackReference
    private DegreeWork degreeWork;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProcessStatus processStatus;

    @Column(name = "url")
    private String url;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "process_observations", joinColumns = @JoinColumn(name = "process_id"))
    @Column(name = "observation")
    @OrderColumn(name = "position")
    private List<String> observations = new ArrayList<>();

    @Transient
    private IProcessState currentState;

    // Constructores
    public Process() {
        this.processStatus = ProcessStatus.CREATED;
    }

    public Process(DegreeWork degreeWork) {
        this();
        this.degreeWork = degreeWork;
    }

    // Getter del estado actual (lazy loading)
    public IProcessState getCurrentState() {
        if (currentState == null) {
            currentState = ProcessStateFactory.fromStatus(processStatus);
        }
        return currentState;
    }

    // Métodos delegados al patrón State
    public void submit() {
        getCurrentState().submit(this);
        refreshState();
    }

    public void approve() {
        getCurrentState().approve(this);
        refreshState();
    }

    public void reject(String observation) {
        getCurrentState().reject(this, observation);
        refreshState();
    }

    public void resubmit(String newUrl) {
        getCurrentState().resubmit(this, newUrl);
        refreshState();
    }

    public void assignJury() {
        getCurrentState().assignJury(this);
        refreshState();
    }

    // Refrescar el estado después de una transición
    private void refreshState() {
        this.currentState = ProcessStateFactory.fromStatus(this.processStatus);
    }

    // Getters & Setters
    public Long getId() { return id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public ProcessType getProcess() { return process; }
    public void setProcess(ProcessType process) { this.process = process; }

    public DegreeWork getDegreeWork() { return degreeWork; }
    public void setDegreeWork(DegreeWork degreeWork) { this.degreeWork = degreeWork; }

    public ProcessStatus getProcessStatus() { return processStatus; }
    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
        this.currentState = null; // Forzar recarga del estado
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public List<String> getObservations() { return observations; }
    public void setObservations(List<String> observations) { this.observations = observations; }

    // Helper
    public void addObservation(String observation) {
        this.observations.add(observation);
    }
}