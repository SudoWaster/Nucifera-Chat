package pl.cezaryregec.cucumber.steps;

import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.mockito.Mockito;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.cucumber.steps.utils.Tokens;
import pl.cezaryregec.logger.ApplicationLogger;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenValiditySteps {

    @Inject
    private Tokens tokens;

    @Given("^current timestamp is (\\d+)$")
    public void todayIs(long timestamp) {
        Mockito.when(tokens.clock.millis())
                .thenReturn(timestamp);

    }

    @Given("^token timestamp expiration is (\\d+)$")
    public void tokenExpiration(long expiration) {
        ApplicationLogger applicationLogger = Mockito.mock(ApplicationLogger.class);
        ConfigSupplier configSupplier = new ConfigSupplier(applicationLogger);
        long inactivityPeriod = configSupplier.get().getSecurity().getTokenExpiration();
        tokens.token.setExpiration(new Timestamp(expiration - inactivityPeriod));
    }

    @Given("^my token fingerprint is \"([^\"]*)\"$")
    public void tokenFingerprint(String fingerprint) {
        tokens.token.setFingerprint(fingerprint);
    }

    @Given("^my client fingerprint is \"([^\"]*)\"$")
    public void clientFingerprint(String fingerprint) {
        tokens.identityService.setFingerprint(fingerprint);
    }

    @When("^I ask whether the token is valid$")
    public void ifExpired() {
        tokens.token.setToken("token");
        tokens.identityService.retrieveToken("token");
    }

    @Then("^token validity result should return \"([^\"]*)\"$")
    public void thenItShouldBe(String result) {
        boolean expected = Boolean.valueOf(result);
        boolean actual = tokens.identityService.isTokenValid();

        assertEquals(expected, actual);
    }
}
