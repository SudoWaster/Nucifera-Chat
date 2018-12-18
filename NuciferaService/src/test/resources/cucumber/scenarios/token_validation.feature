Feature: Token Validation
  Whether token validation works correctly

  @TokenUsed
  Scenario Outline: Token expiration due to inactivity
    Given today is <timestamp>
    And token expiration is <expiration>
    When I ask whether it expired
    Then expiration result should return "<expired>"

    Examples:
      | timestamp | expiration | expired |
      | 0         | 1200000    | false   |
      | 2400000   | 2300000    | true    |
      | 2400000   | 1200000    | true    |
      | 1200000   | 1300000    | false   |

  @TokenUsed
  Scenario Outline: Token expiration due to inactivity or wrong fingerprint
    Given current timestamp is <timestamp>
    And token timestamp expiration is <expiration>
    And my token fingerprint is "<token_fingerprint>"
    And my client fingerprint is "<client_fingerprint>"
    When I ask whether the token is valid
    Then token validity result should return "<valid>"

    Examples:
      | timestamp | expiration | token_fingerprint | client_fingerprint | valid |
      | 2400000   | 2300000    | abc               | abc                | false |
      | 2400000   | 2500000    | abc               | abc                | true  |
      | 2600000   | 3000000    | abc               | vasdas             | false |