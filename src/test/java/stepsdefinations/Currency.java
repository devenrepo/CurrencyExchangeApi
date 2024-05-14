package stepsdefinations;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.json.JSONObject;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Currency {
    public RequestSpecification httpRequest;
    public Response response;
    public int responseCode;
    public ResponseBody responseBody;
    public float AedPrice;
    public long responseTime;
    public String body;

    @Given("I make a GET request to the currency conversion API")
    public void i_make_a_get_request_to_the_currency_conversion_api() {
        RestAssured.baseURI = "https://open.er-api.com/v6/latest";
    }

    @When("I pass the URL USD in request")
    public void i_pass_the_url_usd_in_request() {
        // Capture start time
        long startTime = System.currentTimeMillis();
        httpRequest = RestAssured.given();
        // get response from api
        response = httpRequest.get("USD");
        // Capture end time
        long endTime = System.currentTimeMillis();
        // Calculate response time
        responseTime = (endTime - startTime)/1000;
    }

    @Then("the API response status should be one of {string} or {string}")
    public void the_api_response_status_should_be_one_of_or(String string, String string2) {
        responseCode = response.statusCode();
        Assert.assertEquals(responseCode, 200);
        if(responseCode== 200){
            System.out.println("API is Successfully hit");
        }
        else{
            System.out.println("API is not Successfully hit");
        }

    }

    @Then("the response includes a valid price for USD against AED")
    public void the_response_includes_a_valid_price_for_usd_against_aed() {
        responseBody = response.getBody();
//        convert body in as string
        body = responseBody.asString();
        JsonPath jsonPath = response.jsonPath();
//        Json representation of body
        String priceList = jsonPath.getJsonObject("rates.AED").toString();
        AedPrice = Float.parseFloat(priceList);

    }

    @Then("the price should be in the range of {float} to {float}")
    public void the_price_should_be_in_the_range_of_to(Float initialRange, Float lastRange) {
        if(AedPrice>initialRange && AedPrice<lastRange){
            System.out.println("AED Price is within in rage");
        }
    }

    @Then("the API response time should be at least {int} seconds")
    public void the_api_response_time_should_be_at_least_seconds(Integer maxTime) {

        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Create a DateTimeFormatter to format the output
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime to a string using the formatter
        String formattedDateTime = currentDateTime.format(formatter);

        if(responseTime<maxTime){
            System.out.println("API is respond within giving timeframe: "+ responseTime+" and current date and timestamp is: "+formattedDateTime );
        }
    }

    @Then("the API response should contain {int} currency pairs")
    public void the_api_response_should_contain_currency_pairs(Integer numberOfCurrencyCode) {

        // Parse JSON string to JSONObject
        JSONObject jsonObject = new JSONObject(body);

        // Get the "rates" object from the JSONObject
        JSONObject ratesObject = jsonObject.getJSONObject("rates");

        // Get the size of the key set of the "rates" object
        int currencyCode = ratesObject.keySet().size();

        if(currencyCode==numberOfCurrencyCode){
            // Print the number of keys in the "rates" object
            System.out.println("Number of Currency in the response body: " + currencyCode);
        }
        else {
            System.out.println("Number of currency code is not equal to: "+ numberOfCurrencyCode );
        }
    }

}