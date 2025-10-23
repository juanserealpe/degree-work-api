package co.edu.unicauca.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id_person")
    private Long idPerson;

    @Column(name = "names", nullable = false)
    private String names;

    @Column(name = "last_names", nullable = false)
    private String lastNames;

    @OneToOne
    @JoinColumn(name = "id_person")
    @MapsId
    private Account account;

    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    private List<DegreeWork> directedWorks;

    @OneToMany(mappedBy = "coordinator", fetch = FetchType.LAZY)
    private List<DegreeWork> coordinatedWorks;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private List<DegreeWork> enrolledWorks;


    // Getters & Setters
    public Long getIdPerson() { return idPerson; }
    public void setIdPerson(Long idPerson) { this.idPerson = idPerson; }

    public String getNames() { return names; }
    public void setNames(String names) { this.names = names; }

    public String getLastNames() { return lastNames; }
    public void setLastNames(String lastNames) { this.lastNames = lastNames; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public List<DegreeWork> getDirectedWorks() {return directedWorks;}
    public void setDirectedWorks(List<DegreeWork> directedWorks) {this.directedWorks = directedWorks;}

    public List<DegreeWork> getCoordinatedWorks() {return coordinatedWorks;}
    public void setCoordinatedWorks(List<DegreeWork> coordinatedWorks) {this.coordinatedWorks = coordinatedWorks;}

    public List<DegreeWork> getEnrolledWorks() {return enrolledWorks;}
    public void setEnrolledWorks(List<DegreeWork> enrolledWorks) {this.enrolledWorks = enrolledWorks;}
}
