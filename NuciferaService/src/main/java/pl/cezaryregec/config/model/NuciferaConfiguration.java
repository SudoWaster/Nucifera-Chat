package pl.cezaryregec.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "NuciferaConfiguration")
@AllArgsConstructor
public @Data
class NuciferaConfiguration implements Serializable {

    @XmlElement(name = "Token")
    private TokenConfiguration token;
}
