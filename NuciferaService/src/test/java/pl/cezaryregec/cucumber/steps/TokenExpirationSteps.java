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

public class TokenExpirationSteps {

    @Inject
    private Tokens tokens;

    @Given("^today is (\\d+)$")
    public void todayIs(long timestamp) {
        Mockito.when(tokens.clock.millis())
                .thenReturn(timestamp);

    }

    @Given("^token expiration is (\\d+)$")
    public void tokenExpiration(long expiration) {
        ApplicationLogger applicationLogger = Mockito.mock(ApplicationLogger.class);
        ConfigSupplier configSupplier = new ConfigSupplier(applicationLogger);
        long inactivityPeriod = configSupplier.get().getSecurity().getTokenExpiration();
        tokens.token.setExpiration(new Timestamp(expiration - inactivityPeriod));
    }

    @When("^I ask whether it expired$")
    public void ifExpired() {
        tokens.token.setToken("token");
        tokens.identityService.retrieveToken("token");
    }

    @Then("^expiration result should return \"([^\"]*)\"$")
    public void thenItShouldBe(String result) {
        boolean expected = Boolean.valueOf(result);
        boolean actual = tokens.identityService.hasExpired();

        assertEquals(expected, actual);
    }
}
