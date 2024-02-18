package Order;

import Colour.Colours;
import Model.Order;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Collections;
import java.util.List;
import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private Order order;
    private Integer idTrack;
    public CreateOrderTest(Order order){
        this.order = order;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        RestAssured.filters(new AllureRestAssured());
    }

    @Parameterized.Parameters(name="Тестовые данные")
    public static Object[][] getOrderParameters(){
        return new Object[][] {
                {new Order("Анна", "Иванова", "Екатеринбург. Проспект Космонавтов",
                        "7", "8-900-123-45-67", 1, "2024-02-16",
                        List.of(Colours.GRAY.name()), "Оплата по карте")},
                {new Order("Иван", "Васильев", "Москва", "4",
                        "+7 900 000 00",7, "2024-02-17", List.of(Colours.BLACK.name()),
                        "Оплата со скидкой")},
                {new Order("Константин","Константинов","Волгоград","10",
                        "+7 999 999 99 99", 3, "2024-02-17",
                        List.of(Colours.GRAY.name(), Colours.BLACK.name()), "Хорошего дня")},
                {new Order("Мария", "Петровна", "Калининград", "1",
                        "+7 988 888 88 88", 4, "2024-02-17",
                        Collections.emptyList(),"Цвет любой" )}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа. Позитивный тест")
    public void createOrderTest(){
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
        response.then().log().all()
                .assertThat().body("track", Matchers.notNullValue()).and().statusCode(201);
        idTrack = response.jsonPath().getInt("track");
    }

    @After
    public void putTrack() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        given()
                .header("Content-type", "application/json")
                .body("{" + "track" + idTrack + "}")
                .when()
                .put("/api/v1/orders/cancel");
    }
}
