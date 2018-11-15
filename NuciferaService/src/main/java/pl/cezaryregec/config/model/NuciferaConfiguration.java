package pl.cezaryregec.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "NuciferaConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
public @Data
class NuciferaConfiguration implements Serializable {

    @XmlElement(name = "Debug")
    private DebugConfiguration debug;

    @XmlElement(name = "Security")
    private SecurityConfiguration security;
}
