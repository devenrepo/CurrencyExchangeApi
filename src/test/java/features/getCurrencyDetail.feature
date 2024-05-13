Feature: Validate API Response for Currency Conversion

  Scenario Outline: Verify API call is successful and returns valid price
    Given I make a GET request to the currency conversion API
#    When I pass the URL USD in request
#    Then the API response status should be one of "SUCCESS" or "FAILURE"
#    And the response includes a valid price for USD against AED
#    And the price should be in the range of <startRange> to <lastRange>
#    And the API response time should be at least <minimumResponseTime> seconds
#    Then the API response should contain <currencyCode> currency pairs
    Then the API response should match the JSON schema

    Examples:
      | startRange | lastRange | minimumResponseTime | currencyCode |
      | 3.6        | 3.7       | 3                   | 162          |


#    Then the API response should match the JSON schema
