package co.edu.unicauca.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Long idAdmin;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_admin", referencedColumnName = "id_account")
    @MapsId
    private Account account;

    // Getters & Setters
    public Long getIdAdmin() {return idAdmin;}
    public void setIdAdmin(Long idAdmin) {this.idAdmin = idAdmin;}

    public Account getAccount() {return account;}
    public void setAccount(Account account) {this.account = account;}
}
