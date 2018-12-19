package pl.cezaryregec.cucumber.steps;

import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.mockito.Mockito;
import pl.cezaryregec.config.ConfigSupplier;
import pl.cezaryregec.crypt.CredentialsCombiner;
import pl.cezaryregec.crypt.HashGenerator;
import pl.cezaryregec.crypt.Sha256Generator;
import pl.cezaryregec.cucumber.steps.utils.Users;
import pl.cezaryregec.exception.APIException;
import pl.cezaryregec.logger.ApplicationLogger;
import pl.cezaryregec.user.models.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserLoginSteps {
    @Inject
    private Users users;

    private static Optional<User> userOptional;

    @Given("^a username \"([^\"]*)\" having a password \"([^\"]*)\"$")
    public void storedUsername(String username, String password) throws APIException {
        users.user.setUsername(username);

        ApplicationLogger applicationLogger = Mockito.mock(ApplicationLogger.class);
        ConfigSupplier configSupplier = new ConfigSupplier(applicationLogger);
        CredentialsCombiner credentialsCombiner = new CredentialsCombiner();
        String salt = configSupplier.get().getSecurity().getSalt();
        String combinedPassword = credentialsCombiner.combine(username, password, salt);
        HashGenerator hashGenerator = new Sha256Generator();
        String encodedPassword = hashGenerator.encode(combinedPassword);
        users.user.setPassword(encodedPassword);
    }

    @When("^I try to login with \"([^\"]*)\" and \"([^\"]*)\"$")
    public void tryLogin(String login, String password) throws APIException {
        userOptional = users.userService.loginAndGet(login, password);
    }

    @Then("^I should have following login result: \"([^\"]*)\"$")
    public void thenLoginResult(String result) {
        boolean expected = Boolean.valueOf(result);
        boolean actual = userOptional.isPresent();

        assertEquals(expected, actual);
    }
}
