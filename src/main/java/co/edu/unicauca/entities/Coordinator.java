package co.edu.unicauca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "coordinator")
public class Coordinator extends Person {
    @OneToMany(mappedBy = "coordinator")
    private List<DegreeWork> degreeWorks;
    // Getters & setters
    public List<DegreeWork> getDegreeWorks() { return degreeWorks; }
    public void setDegreeWorks(List<DegreeWork> degreeWorks) { this.degreeWorks = degreeWorks; }
}
