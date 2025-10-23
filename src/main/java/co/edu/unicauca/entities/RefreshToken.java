package co.edu.unicauca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "refresh_roken")
public class RefreshToken {
    @Id
    private String token;
    private Long userId;
    private Instant expiryDate;

    public Instant getExpiryDate() {return expiryDate;}
    public void setExpiryDate(Instant expiryDate) {this.expiryDate = expiryDate;}

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
}
