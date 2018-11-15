package pl.cezaryregec.config.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "Verbose")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
public @Data
class DebugConfiguration {

    @XmlElement(name = "Verbose")
    private Boolean verbose;

    @XmlElement(name = "FormatLog")
    private Boolean formatLog;
}
