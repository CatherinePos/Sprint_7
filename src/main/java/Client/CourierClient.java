package Client;

import Model.Courier;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierClient {
    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestApiV1CourierLogin(Courier courier){
        return given().log().all()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestApiV1Courier(Courier courier){
        return given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }
}

