package co.edu.unicauca.entities;

import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.enums.TypeProcess;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "processes")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "process_type")
public abstract class Process {
    @Id
    @Column(name = "id_process")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "type_process")
    @Enumerated(EnumType.STRING)
    private TypeProcess process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "degree_work_id")
    @JsonBackReference
    private DegreeWork degreeWork;

    @Column(name = "status")
    private ProcessStatus processStatus;

    public Process() { }

    public Process(Date date, TypeProcess process, DegreeWork degreeWork) {
        this.date = date;
        this.process = process;
        this.degreeWork = degreeWork;
        this.processStatus = ProcessStatus.PENDIENTE;
    }

    //Getters & setters
    public Long getId() { return id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public TypeProcess getProcess() { return process; }
    public void setProcess(TypeProcess process) { this.process = process; }

    public DegreeWork getDegreeWork() { return degreeWork; }
    public void setDegreeWork(DegreeWork degreeWork) { this.degreeWork = degreeWork; }

    public ProcessStatus getProcessStatus(){return processStatus;}
    public void setProcessStatus(ProcessStatus processStatus){this.processStatus = processStatus;}
}

