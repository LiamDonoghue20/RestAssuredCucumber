package step.definitions;


import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;


public class DemoRestAPITest {

    private Scenario scenario;
    private Response response;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
    LocalDate localDate = LocalDate.now();
    private final String BASE_URL = "http://localhost:3000";

    @Before
    public void before(Scenario scenarioVal) {
        this.scenario = scenarioVal;
    }

    @Given("Get Call to {string}")
    public void get_call_to_url(String url) throws Exception {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification req = RestAssured.given();
        response = req.when().get(new URI(url));
    }

    @Given("Post new item to {string}")
    public void post_to_url(String url) throws Exception {
        System.out.print(url);
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        if (url.equals("/posts")) {
            request.header("Content-Type", "application/json");
            response = request.body("{ \"title\":\"New Post\", \"author\":\"User One\"}")
                    .post(url);
        } else if (url.equals("/comments")) {
            request.header("Content-Type", "application/json");
            response = request.body("{\"title\":\"New Comment\", \"author\":\"User Two\"}")
                    .post(url);
        } else if (url.equals("/profile")) {
            request.header("Content-Type", "application/json");
            response = request.body("{ \"username\":\"New Profile\", \"Member since\":\"" + localDate + "\"}")
                    .post(url);
        }

    }

    @Given("Post new item to {string} with unique ID")
    public void post_to_url_with_uniqueID(String url) throws Exception {
        System.out.print(url);
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        response = request.body("{ \"id\":99999,\"title\":\"To be deleted\", \"author\":\"User One\"}")
                .post(url);


    }

    @When("delete item from {string}")
    public void delete_item_from(String url) {
        System.out.print(url);
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.delete(url + "/99999");


    }


    @Then("Response Code 200 is returned")
    public void response200_is_validated() {
        int responseCode = response.then().extract().statusCode();
        Assert.assertEquals("200", responseCode + "");
    }

    @Then("Response Code 201 is returned")
    public void response201_is_validated() {
        int responseCode = response.then().extract().statusCode();
        Assert.assertEquals("201", responseCode + "");
    }


    @Then("new {string} item is returned")
    public void new_item_is_returned(String url) {

        if (url.equals("/posts")) {
            response.then().body(containsString("User One"));
        } else if (url.equals("/comments")) {
            response.then().body(containsString("New Comment"));
        } else if (url.equals("/profile")) {
            response.then().body(containsString(localDate.toString()));
        }
    }


    @Then("new post is not returned")
    public void new_post_is_not_returned() {

        response.then()
                .body("$", not(hasKey("To be deleted")));

    }
}

