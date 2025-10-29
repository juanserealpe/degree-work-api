package co.edu.unicauca.entities;

import co.edu.unicauca.enums.ProcessState;
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
@DiscriminatorColumn(name = "process_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Process {
    @Id
    @Column(name = "id_process")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_degree_work")
    @JsonBackReference
    private DegreeWork degreeWork;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ProcessState processState;

    @Column(name = "url")
    private String url;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "process_observations", joinColumns = @JoinColumn(name = "process_id"))
    @Column(name = "observation")
    @OrderColumn(name = "position")
    private List<String> observations = new ArrayList<>();

    @Transient
    private IProcessState currentState;

    // Constructors

    public Process() {
        this.processState = ProcessState.CREATED;
    }

    public Process(DegreeWork degreeWork) {
        this();
        this.degreeWork = degreeWork;
    }

    // Getter current state

    public IProcessState getCurrentState() {
        if (currentState == null) {
            currentState = ProcessStateFactory.fromStatus(processState);
        }
        return currentState;
    }

    // State pattern methods

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

    // Refresh the state after a transition

    private void refreshState() {
        this.currentState = ProcessStateFactory.fromStatus(this.processState);
    }

    // Getters & Setters

    public Long getId() { return id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public DegreeWork getDegreeWork() { return degreeWork; }
    public void setDegreeWork(DegreeWork degreeWork) { this.degreeWork = degreeWork; }

    public ProcessState getProcessState() { return processState; }
    public void setProcessState(ProcessState processState) {
        this.processState = processState;
        this.currentState = null;
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