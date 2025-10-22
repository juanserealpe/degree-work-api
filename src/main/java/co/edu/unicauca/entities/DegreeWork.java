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
    
    @OneToMany(mappedBy = "degreeWork")
    private List<Student> students;
    
    @ManyToOne
    @JoinColumn(name = "id_coordinator", nullable = false)
    private Coordinator coordinator;
    
    @ManyToOne
    @JoinColumn(name = "id_director", nullable = false)
    private Director director;

    // Getters & setters
    public long getIdDegreeWork() { return idDegreeWork; }
    public void setIdDegreeWork(long idDegreeWork) { this.idDegreeWork = idDegreeWork; }

    public Modality getModality() { return modality; }
    public void setModality(Modality modality) { this.modality = modality; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    public Coordinator getCoordinator() { return coordinator; }
    public void setCoordinator(Coordinator coordinator) { this.coordinator = coordinator; }
    
    public Director getDirector(){return director;}
    public void setDirector(Director director){this.director = director;}
}
