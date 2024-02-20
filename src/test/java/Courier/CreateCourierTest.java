package Courier;

import Client.CourierClient;
import Model.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static io.restassured.RestAssured.given;

public class CreateCourierTest {
    private Integer idCourier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка статуса запроса создания курьера и получения сообщения об успешном создании курьера. Позитивный тест")
    public void createCourierTest() {
        CourierClient clientStep = new CourierClient();
        String login = RandomStringUtils.randomAlphanumeric(5, 10);
        String password = RandomStringUtils.randomAlphanumeric(6, 8);
        String firstName = RandomStringUtils.randomAlphabetic(3, 10);
        Courier courier = new Courier(login, password, firstName);
        Response response1 = clientStep.sendPostRequestApiV1Courier(courier);
        response1.then().log().all()
                .assertThat().body("ok", Matchers.is(true)).and().statusCode(201);
        Response response2 = clientStep.sendPostRequestApiV1CourierLogin(courier);
        idCourier = response2.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Проверка статуса запроса создания двух одинаковых курьреров и получения сообщения об ошибке. Негативный тест")
    public void createTwoIdenticalCouriersTest() {
        CourierClient clientStep = new CourierClient();
        String login = RandomStringUtils.randomAlphanumeric(5, 10);
        String password = RandomStringUtils.randomAlphanumeric(6, 8);
        String firstName = RandomStringUtils.randomAlphabetic(3, 10);
        Courier courier = new Courier(login, password, firstName);
        clientStep.sendPostRequestApiV1Courier(courier);
        Response response1 = clientStep.sendPostRequestApiV1Courier(courier);
        response1.then().log().all()
                .assertThat().body("message", Matchers.is("Этот логин уже используется. Попробуйте другой.")).and().statusCode(409);
        Response response2 = clientStep.sendPostRequestApiV1CourierLogin(courier);
        idCourier = response2.jsonPath().getInt("id");
    }
    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка обязательности заполнения поля, статуса запроса создания курьера без логина и " +
            "получения сообщения об ошибке. Негативный тест")
    public void createCourierWithoutLogin() {
        CourierClient clientStep = new CourierClient();
        String firstName = RandomStringUtils.randomAlphabetic(3, 10);
        String password = RandomStringUtils.randomAlphanumeric(6, 8);
        Courier courier = new Courier();
        courier.setPassword(password);
        courier.setFirstName(firstName);
        Response response = clientStep.sendPostRequestApiV1Courier(courier);
        response.then().log().all()
                .assertThat().body("message", Matchers.is("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка обязательности заполнения поля, статуса запроса создания курьера без пароля " +
            "и получения сообщения об ошибке. Негативный тест")
    public void createCourierWithoutPassword() {
        CourierClient clientStep = new CourierClient();
        String login = RandomStringUtils.randomAlphabetic(5, 10);
        String firstName = RandomStringUtils.randomAlphabetic(3, 10);
        Courier courier = new Courier();
        courier.setLogin(login);
        courier.setFirstName(firstName);
        Response response = clientStep.sendPostRequestApiV1Courier(courier);
        response.then().log().all()
                .assertThat().body("message", Matchers.is("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    @DisplayName("Создание курьера без указания имени")
    @Description("Проверка необязательности заполнения поля, статуса запроса создания курьера без указания имени" +
            "и получения сообщения об успешном создании курьера. Позитивный тест")
    public void createCourierWithoutFirstName() {
        CourierClient clientStep = new CourierClient();
        String login = RandomStringUtils.randomAlphabetic(3, 10);
        String password = RandomStringUtils.randomAlphanumeric(6, 8);
        Courier courier = new Courier();
        courier.setLogin(login);
        courier.setPassword(password);
        Response response = clientStep.sendPostRequestApiV1Courier(courier);
        response.then().log().all()
                .assertThat().body("ok", Matchers.is(true)).and().statusCode(201);
        Response response2 = clientStep.sendPostRequestApiV1CourierLogin(courier);
        idCourier = response2.jsonPath().getInt("id");
    }
    @After
    public void deleteCourier() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + idCourier);
    }
}
