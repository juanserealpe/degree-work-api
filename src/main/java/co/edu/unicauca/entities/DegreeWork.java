package co.edu.unicauca.entities;

import co.edu.unicauca.enums.DegreeWorkStatus;
import co.edu.unicauca.enums.Modality;
import co.edu.unicauca.enums.ProcessStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "degreeworks")
public class DegreeWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDegreeWork;

    @Enumerated(EnumType.STRING)
    @Column(name = "modality",nullable = false)
    private Modality modality;

    @Column(name = "tittle", nullable = false, unique = true, length = 200)
    private String tittle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coordinator", nullable = false)
    @JsonIgnoreProperties({"directedWorks", "coordinatedWorks", "enrolledWorks", "account"})
    private User coordinator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_director", nullable = false)
    @JsonIgnoreProperties({"directedWorks", "coordinatedWorks", "enrolledWorks", "account"})
    private User director;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "degree_work_students",
            joinColumns = @JoinColumn(name = "degree_work_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties({"directedWorks", "coordinatedWorks", "enrolledWorks", "account"})
    private List<User> students = new ArrayList<>();

    @OneToMany(mappedBy = "degreeWork", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Process> processes = new ArrayList<>();


    @Column(name = "status")
    private DegreeWorkStatus status;

    // Constructors
    public DegreeWork() {}
    public DegreeWork(Modality modality, String tittle, User coordinator, User director) {
        this.modality = modality;
        this.tittle = tittle;
        this.coordinator = coordinator;
        this.director = director;
        this.status = DegreeWorkStatus.IN_PROGRESS;
    }

    // Getters & Setters
    public Long getIdDegreeWork() { return idDegreeWork; }
    public void setIdDegreeWork(Long idDegreeWork) { this.idDegreeWork = idDegreeWork; }

    public Modality getModality() { return modality; }
    public void setModality(Modality modality) { this.modality = modality; }

    public String getTittle() { return tittle; }
    public void setTittle(String tittle) { this.tittle = tittle; }

    public User getCoordinator() { return coordinator; }
    public void setCoordinator(User coordinator) { this.coordinator = coordinator; }

    public User getDirector() { return director; }
    public void setDirector(User director) { this.director = director; }

    public List<User> getStudents() { return students; }
    public void setStudents(List<User> students) { this.students = students; }

    public List<Process> getProcesses(){return processes;}

    public DegreeWorkStatus getStatus(){return status;}

    // Helper methods
    public void addStudent(User student) {
        students.add(student);
        student.getEnrolledWorks().add(this);
    }

    public void addStudents(List<User> students) {
        if (students == null || students.isEmpty()) return;
        for (User student : students) {
            if (!this.students.contains(student)) this.students.add(student);
            if (!student.getEnrolledWorks().contains(this)) student.getEnrolledWorks().add(this);
        }
    }

    public void removeStudent(User student) {
        students.remove(student);
        student.getEnrolledWorks().remove(this);
    }

    public void addProcess(Process process){
        if(processes.isEmpty()){
            processes.add(process);
        }else{
            Process a = processes.get(processes.size()-1);
            if(a.getProcess().equals(ProcessStatus.APPROVED)){
                processes.add(process);
            }
        }
    }
}
