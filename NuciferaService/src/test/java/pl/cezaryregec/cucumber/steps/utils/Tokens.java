package pl.cezaryregec.cucumber.steps.utils;

import cucumber.runtime.java.guice.ScenarioScoped;
import pl.cezaryregec.auth.models.AuthToken;
import pl.cezaryregec.auth.session.IdentityService;

import java.time.Clock;

@ScenarioScoped
public class Tokens {
    public Clock clock;
    public IdentityService identityService;
    public AuthToken token = new AuthToken();
}
