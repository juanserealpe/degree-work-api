package co.edu.unicauca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student extends Person {
    @ManyToOne
    @JoinColumn(name = "id_degree_work")
    private DegreeWork degreeWork;

    public DegreeWork getDegreeWork() { return degreeWork; }
    public void setDegreeWork(DegreeWork degreeWork) { this.degreeWork = degreeWork; }
}
