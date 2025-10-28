package co.edu.unicauca.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "format_a")
@DiscriminatorValue("FORMAT_A")
public class FormatA extends Process {
    @Column(name = "failed_attempts")
    private byte failedAttempts;

    //Constructor

    public FormatA(DegreeWork degreeWork) {
        super(degreeWork);
        this.failedAttempts = 0;
    }

    //Getters & setters

    public byte getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(byte failedAttempts) { this.failedAttempts = failedAttempts; }

}
