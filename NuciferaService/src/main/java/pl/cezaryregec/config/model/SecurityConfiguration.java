package pl.cezaryregec.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;

@XmlRootElement(name = "Token")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
public @Data
class SecurityConfiguration implements Serializable {

    @XmlElement(name = "TokenExpiration")
    private Long tokenExpiration;

    @XmlElement(name = "ParseBase64")
    private Boolean base64;
}
