package co.edu.unicauca.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "draft")
@DiscriminatorValue("DRAFT")
public class Draft extends Process {

    @Column(name = "deadline")
    private LocalDateTime deadline;

    //Constructors

    public Draft() {
        super();
    }

    public Draft(DegreeWork degreeWork, LocalDateTime deadline) {
        super(degreeWork);
        this.deadline = deadline;
    }

    public Draft(DegreeWork degreeWork) {
        super(degreeWork);
    }

    // Getters & setters

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}