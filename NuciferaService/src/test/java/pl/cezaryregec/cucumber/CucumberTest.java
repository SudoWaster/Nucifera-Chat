package pl.cezaryregec.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/cucumber/scenarios",
        glue = {"pl.cezaryregec.cucumber.steps"}
)
public class CucumberTest {
}
