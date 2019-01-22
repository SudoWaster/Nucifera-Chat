package pl.cezaryregec.user.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangePasswordRequest implements Serializable {
    private static final long serialVersionUID = 6982552553530073031L;

    private String username;
    private String password;
    private String newPassword;
}
