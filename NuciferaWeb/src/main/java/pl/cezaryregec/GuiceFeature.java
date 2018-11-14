package pl.cezaryregec;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.ServiceLocatorProvider;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(1)
public class GuiceFeature implements Feature {

    @Inject
    ServiceLocator serviceLocator;

    GuiceFeature() {
    }

    /**
     * Configures a HK2 to Guice bridge feature
     *
     * @param featureContext context in which the feature is enabled
     * @return {@code true}
     * @see APIServletContextListener
     */
    @Override
    public boolean configure(FeatureContext featureContext) {
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);

        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(APIServletContextListener.injector);

        return true;
    }
}
