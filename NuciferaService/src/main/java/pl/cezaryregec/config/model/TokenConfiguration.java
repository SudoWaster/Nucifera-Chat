package pl.cezaryregec.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;

@XmlRootElement(name = "Token")
@AllArgsConstructor
public @Data
class TokenConfiguration implements Serializable {

    @XmlElement(name = "Expiration")
    private BigInteger expiration;
}
