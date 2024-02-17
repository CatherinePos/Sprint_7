package Courier;

import Client.CourierClient;
import Model.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class CourierLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Курьер авторизуется на сайте")
    @Description("Проверка успешного статуса авторизации курьера на сайте и получения id курьера. Позитивный тест")
    public void authorizationTest(){
        CourierClient clientStep = new CourierClient();
        Courier courier = new Courier();
        courier.setLogin("ninja777888");
        courier.setPassword("1234");
        Response response = clientStep.sendPostRequestApiV1CourierLogin(courier);
        response.then().log().all()
                .assertThat().body("id", Matchers.notNullValue()).and().statusCode(200);
    }

    @Test
    @DisplayName("Курьер заходит на сайт без логина")
    @Description("Проверка обязтельности заполнения поля, статуса запроса авторизации курьера на сайте без логина " +
            "и получения сообщения об ошибке. Негативный тест")
    public void authorizationWithoutLoginTest(){
        CourierClient clientStep = new CourierClient();
        Courier courier = new Courier();
        courier.setPassword("1234");
        Response response = clientStep.sendPostRequestApiV1CourierLogin(courier);
        response.then().log().all()
                .assertThat().body("message", Matchers.is("Недостаточно данных для входа")).
                and().statusCode(400);
    }

    @Test
    @DisplayName("Курьер заходит на сайт без пароля")
    @Description("Проверка обязательности заполнения поля, статуса запроса авторизации курьера на сайте без пароля " +
            "и получения сообщения об ошибке. Негативный тест")
    public void authorizationWithoutPasswordTest(){
        CourierClient clientStep = new CourierClient();
        Courier courier = new Courier();
        courier.setLogin("ninja777888");
        courier.setPassword("");
        Response response = clientStep.sendPostRequestApiV1CourierLogin(courier);
        response.then().log().all()
                .assertThat().body("message", Matchers.is("Недостаточно данных для входа")).and().statusCode(400);
    }

    @Test
    @DisplayName("Курьер авторизуется на сайте с неправильным, несуществующим логином")
    @Description("Проверка статуса запроса авторизации курьрера на сайте с неправильным, несуществущим логином. Негативный тест")
    public void authorizationWithWrongLoginTest(){
        CourierClient clientStep = new CourierClient();
        Courier courier = new Courier();
        courier.setLogin("ne_sushestvuet");
        courier.setPassword("1234");
        Response response = clientStep.sendPostRequestApiV1CourierLogin(courier);
        response.then().log().all()
                .assertThat().body("message", Matchers.is("Учетная запись не найдена")).and().statusCode(404);

    }

    @Test
    @DisplayName("Курьер авторизуется на сайте с неправильным паролем")
    @Description("Проверка статуса запроса авторизации курьрера на сайте с неправильным паролем. Негативный тест")
    public void authorizationWithWrongPasswordTest(){
        CourierClient clientStep = new CourierClient();
        Courier courier = new Courier();
        courier.setLogin("ninja777888");
        courier.setPassword("777888");
        Response response = clientStep.sendPostRequestApiV1CourierLogin(courier);
        response.then().log().all()
                .assertThat().body("message", Matchers.is("Учетная запись не найдена")).and().statusCode(404);
    }

    @Test
    @DisplayName("Курьер авторизуется на сайте с неправильным логином и паролем")
    @Description("Проверка статуса запроса авторизации курьрера на сайте с неправильным логином и паролем. Негативный тест")
    public void authorizationWithWrongLoginAndPasswordTest(){
        CourierClient clientStep = new CourierClient();
        Courier courier = new Courier();
        courier.setLogin("ninja777888ninja");
        courier.setPassword("555666");
        Response response = clientStep.sendPostRequestApiV1CourierLogin(courier);
        response.then().log().all()
                .assertThat().body("message", Matchers.is("Учетная запись не найдена")).and().statusCode(404);
    }
}