Feature: User support
  Whether user account login and user specific features work

  @UserUsed
  Scenario Outline: User login
    Given a username "<stored_user>" having a password "<stored_password>"
    When I try to login with "<login>" and "<password>"
    Then I should have following login result: "<result>"

    Examples:
      | stored_user | stored_password | login | password | result |
      | test        | test            | test  | test     | true   |
      | test        | test            | t2st  | test     | false  |
      | test        | test            | test  | tttt334  | false  |