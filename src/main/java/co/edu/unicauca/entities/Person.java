package co.edu.unicauca.entities;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_person")
    private long idPerson;

    @Column(name = "names", nullable = false)
    private String names;

    @Column(name = "last_names", nullable = false)
    private String lastNames;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    public long getIdPerson() {return idPerson;}
    public void setIdPerson(long idPerson) {this.idPerson = idPerson;}
    public String getNames() {return names;}
    public void setNames(String names) {this.names = names;}
    public String getLastNames() {return lastNames;}
    public void setLastNames(String lastNames) {this.lastNames = lastNames;}
    public Account getAccount() {return account;}
    public void setAccount(Account account) {this.account = account;}
}
