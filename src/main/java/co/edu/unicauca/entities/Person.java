package co.edu.unicauca.entities;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Person {
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

    // Getters & Setters
    public Long getIdPerson() { return idPerson; }
    public void setIdPerson(Long idPerson) { this.idPerson = idPerson; }

    public String getNames() { return names; }
    public void setNames(String names) { this.names = names; }

    public String getLastNames() { return lastNames; }
    public void setLastNames(String lastNames) { this.lastNames = lastNames; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
