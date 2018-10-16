package pl.cezaryregec.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import pl.cezaryregec.auth.AuthState;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "tokens")
@JsonIgnoreProperties({"token", "fingerprint", "challenge"})
public @Data
class AuthToken implements Serializable {
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

    @Enumerated
    @Column(name = "authstate")
    private AuthState authState;

    @Column(name = "challenge")
    private BigInteger challenge;

    public AuthToken() {
        this.token = "null" + System.currentTimeMillis();
    }

    @XmlTransient
    public String getToken() {
        return token;
    }

    @XmlTransient
    public String getFingerprint() {
        return fingerprint;
    }

    @XmlTransient
    public BigInteger getChallenge() {
        return challenge;
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
