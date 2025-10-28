package co.edu.unicauca.entities;

import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.enums.TypeProcess;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    @Column(name = "date")
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "type_process")
    @Enumerated(EnumType.STRING)
    private TypeProcess process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "degree_work_id")
    @JsonBackReference
    private DegreeWork degreeWork;

    @Column(name = "status")
    private ProcessStatus processStatus;

    @Column(name = "url")
    private String url;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "format_a_observations", joinColumns = @JoinColumn(name = "format_a_id"))
    @Column(name = "observation")
    @OrderColumn(name = "position")
    private List<String> observations = new ArrayList<>();

    public Process(DegreeWork degreeWork) {
        this.degreeWork = degreeWork;
        this.processStatus = ProcessStatus.PENDIENTE;
    }

    //Getters & setters

    public Long getId() { return id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public TypeProcess getProcess() { return process; }
    public void setProcess(TypeProcess process) { this.process = process; }

    public DegreeWork getDegreeWork() { return degreeWork; }
    public void setDegreeWork(DegreeWork degreeWork) { this.degreeWork = degreeWork; }

    public ProcessStatus getProcessStatus(){return processStatus;}
    public void setProcessStatus(ProcessStatus processStatus){this.processStatus = processStatus;}

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

    public List<String> getObservations() {return observations;}
    public void setObservations(List<String> observations) {this.observations = observations;}

    //Helpers
    public void addObservation(String observation){this.observations.add(observation);}
}

