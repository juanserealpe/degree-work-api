package co.edu.unicauca.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false, name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "revoked")
    private boolean revoked = false;

    public RefreshToken() {this.createdAt = Instant.now();}

    // Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    public Account getAccount() {return account;}
    public void setAccount(Account account) {this.account = account;}

    public Instant getExpiryDate() {return expiryDate;}
    public void setExpiryDate(Instant expiryDate) {this.expiryDate = expiryDate;}

    public Instant getCreatedAt() {return createdAt;}
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}

    public boolean isRevoked() {return revoked;}
    public void setRevoked(boolean revoked) {this.revoked = revoked;}

    // Helper method
    public boolean isExpired() {return Instant.now().isAfter(this.expiryDate);}
}