package pl.cezaryregec.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import pl.cezaryregec.config.model.NuciferaConfiguration;
import pl.cezaryregec.logger.ApplicationLogger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.function.Supplier;

@Singleton
public class ConfigSupplier implements Supplier<NuciferaConfiguration> {
    private static final String NUCIFERA_CONFIG_FILENAME = "nucifera.xml";
    private static final String DEFAULT_CONFIG_FILENAME = "default-nucifera.xml";


    private final ApplicationLogger applicationLogger;
    private NuciferaConfiguration nuciferaConfiguration;

    @Inject
    public ConfigSupplier(ApplicationLogger applicationLogger) {
        this.applicationLogger = applicationLogger;
    }

    @Override
    public NuciferaConfiguration get() {
        if (nuciferaConfiguration == null) {
            loadConfigFromResource();
        }
        return nuciferaConfiguration;
    }

    /**
     * Tries to load config from {@value #NUCIFERA_CONFIG_FILENAME}
     *
     * If it fails, logs it to application log
     * and from now on anything using this config will throw NullPointerException
     *
     * By default, it uses {@value #DEFAULT_CONFIG_FILENAME} file from resources
     */
    private void loadConfigFromResource() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILENAME);
        if (getClass().getResource(NUCIFERA_CONFIG_FILENAME) != null) {
            inputStream = getClass().getResourceAsStream(NUCIFERA_CONFIG_FILENAME);
        }
        JAXBContext context = null;
        Unmarshaller unmarshaller = null;
        try {
            context = JAXBContext.newInstance(NuciferaConfiguration.class);
            unmarshaller = context.createUnmarshaller();
            nuciferaConfiguration = (NuciferaConfiguration) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            applicationLogger.log(e);
        }
    }
}
