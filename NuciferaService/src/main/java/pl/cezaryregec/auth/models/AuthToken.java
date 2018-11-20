package pl.cezaryregec.auth.models;

import lombok.Data;
import pl.cezaryregec.user.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "tokens")
public @Data class AuthToken implements Serializable {
    private static final long serialVersionUID = -3873109846898344297L;

    @Id
    @Basic(optional = false)
    @Size(min = 1, max = 64)
    @Column(name = "token")
    private String token;

    @Basic(optional = false)
    @NotNull
    @Column(name = "expiration")
    private Timestamp expiration;

    @Basic(optional = false)
    private String fingerprint;

    @Column(name = "cipherspec")
    private Boolean cipherSpec;

    @Column(name = "challenge")
    private String challenge;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public AuthToken() {
        this.token = "null" + System.currentTimeMillis();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (token != null ? token.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AuthToken)) {
            return false;
        }

        AuthToken other = (AuthToken) object;
        return this.token != null && this.token.equals(other.token);
    }
}
