package co.edu.unicauca.entities;

import co.edu.unicauca.enums.Modality;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "degree_work")
public class DegreeWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDegreeWork;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modality modality;


    @ManyToOne
    @JoinColumn(name = "id_coordinator", nullable = false)
    private User coordinator;

    @ManyToOne
    @JoinColumn(name = "id_director", nullable = false)
    private User director;

    @ManyToMany
    @JoinTable(
            name = "degree_work_students",
            joinColumns = @JoinColumn(name = "degree_work_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> students;


    // Getters & setters
    public long getIdDegreeWork() { return idDegreeWork; }
    public void setIdDegreeWork(long idDegreeWork) { this.idDegreeWork = idDegreeWork; }

    public Modality getModality() { return modality; }
    public void setModality(Modality modality) { this.modality = modality; }

    public List<User> getStudents() { return students; }
    public void setStudents(List<User> students) { this.students = students; }

    public User getCoordinator() { return coordinator; }
    public void setCoordinator(User coordinator) { this.coordinator = coordinator; }

    public User getDirector(){return director;}
    public void setDirector(User director){this.director = director;}
}
