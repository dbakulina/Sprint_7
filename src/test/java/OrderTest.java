import config.Config;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotEquals;

public class OrderTest {
    private int orderTrack;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
    }
 // 1.Проверить что можно указать один из цветов — BLACK ;
    @Test
    public void createOrderBlack(){
        File json = new File("src/test/resources/order.json");
                given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(json)
                .when()
                .post("/orders")
                .then().log().all()
                .assertThat()
                .statusCode(201);
    }
    // 2.Проверить что можно указать один из цветов — GREY;
    @Test
    public void createOrderGrey(){
        File json = new File("src/test/resources/ordergrey.json");
        given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(json)
                .when()
                .post("/orders")
                .then().log().all()
                .assertThat()
                .statusCode(201);
    }
    // 3.Проверить что можно указать оба цвета;
    @Test
    public void createOrderTwoColours(){
        File json = new File("src/test/resources/ordertwocolours.json");
        given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(json)
                .when()
                .post("/orders")
                .then().log().all()
                .assertThat()
                .statusCode(201);
    }
    // 4.Проверить что можно не указывать цвет;
    @Test
    public void createOrderNoColours(){
        File json = new File("src/test/resources/ordernocolour.json");
        given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(json)
                .when()
                .post("/orders")
                .then().log().all()
                .assertThat()
                .statusCode(201);
    }
    // 5. Проверить что тело ответа содержит track.
    @Test
    public void orderReturnTrack(){
        File json = new File("src/test/resources/ordernocolour.json");
        orderTrack = given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(json)
                .when()
                .post("/orders")
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .extract().path("track");
        assertNotEquals(0, orderTrack);
    }
}
