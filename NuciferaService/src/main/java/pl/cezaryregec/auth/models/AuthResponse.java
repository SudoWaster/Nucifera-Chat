package pl.cezaryregec.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import pl.cezaryregec.auth.AuthState;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "tokens")
@JsonIgnoreProperties({"fingerprint"})
public @Data
class AuthResponse implements Serializable {
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
    @Column(name = "fingerprint")
    private String fingerprint;

    @Basic()
    @Column(name = "authState")
    private AuthState authState;

    public AuthResponse() {
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
        if (!(object instanceof AuthResponse)) {
            return false;
        }

        AuthResponse other = (AuthResponse) object;
        return this.token != null && this.token.equals(other.token);
    }
}
