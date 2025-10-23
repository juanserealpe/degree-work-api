package co.edu.unicauca.entities;

import co.edu.unicauca.enums.Modality;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "degree_work")
public class DegreeWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDegreeWork;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modality modality;

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

    // Getters & setters
    public Long getIdDegreeWork() { return idDegreeWork; }
    public void setIdDegreeWork(Long idDegreeWork) { this.idDegreeWork = idDegreeWork; }

    public Modality getModality() { return modality; }
    public void setModality(Modality modality) { this.modality = modality; }

    public List<User> getStudents() { return students; }
    public void setStudents(List<User> students) { this.students = students; }

    public User getCoordinator() { return coordinator; }
    public void setCoordinator(User coordinator) { this.coordinator = coordinator; }

    public User getDirector(){return director;}
    public void setDirector(User director){this.director = director;}

    public void addStudent(User student) {
        students.add(student);
        student.getEnrolledWorks().add(this);
    }

    public void removeStudent(User student) {
        students.remove(student);
        student.getEnrolledWorks().remove(this);
    }
}