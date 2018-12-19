package pl.cezaryregec.cucumber.steps.utils;

import cucumber.runtime.java.guice.ScenarioScoped;
import pl.cezaryregec.user.UserService;
import pl.cezaryregec.user.models.User;

@ScenarioScoped
public class Users {
    public User user = new User();
    public UserService userService;
}
