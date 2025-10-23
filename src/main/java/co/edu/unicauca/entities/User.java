package co.edu.unicauca.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idPerson;

    @Column(name = "names", nullable = false)
    private String names;

    @Column(name = "last_names", nullable = false)
    private String lastNames;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_user", referencedColumnName = "id_account")
    @MapsId
    private Account account;

    @OneToMany(mappedBy = "director", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"director", "coordinator", "students"})
    private List<DegreeWork> directedWorks = new ArrayList<>();

    @OneToMany(mappedBy = "coordinator", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"director", "coordinator", "students"})
    private List<DegreeWork> coordinatedWorks = new ArrayList<>();

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"director", "coordinator", "students"})
    private List<DegreeWork> enrolledWorks = new ArrayList<>();

    // Getters & Setters
    public Long getIdPerson() { return idPerson; }
    public void setIdPerson(Long idPerson) { this.idPerson = idPerson; }

    public String getNames() { return names; }
    public void setNames(String names) { this.names = names; }

    public String getLastNames() { return lastNames; }
    public void setLastNames(String lastNames) { this.lastNames = lastNames; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) {this.account = account;}

    public List<DegreeWork> getDirectedWorks() {return directedWorks;}
    public void setDirectedWorks(List<DegreeWork> directedWorks) {this.directedWorks = directedWorks;}

    public List<DegreeWork> getCoordinatedWorks() {return coordinatedWorks;}
    public void setCoordinatedWorks(List<DegreeWork> coordinatedWorks) {this.coordinatedWorks = coordinatedWorks;}

    public List<DegreeWork> getEnrolledWorks() {return enrolledWorks;}
    public void setEnrolledWorks(List<DegreeWork> enrolledWorks) {this.enrolledWorks = enrolledWorks;}
}