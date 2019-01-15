package pl.cezaryregec.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import pl.cezaryregec.auth.models.AuthToken;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data class User implements Serializable {
    private static final long serialVersionUID = -650368645952142122L;

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "lastseen")
    private Timestamp lastSeen;

    @JsonIgnore
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<AuthToken> tokenList;
}
