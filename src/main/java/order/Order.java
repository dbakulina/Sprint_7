package order;
import config.Config;
import courier.Courier;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import static io.restassured.RestAssured.given;

public class Order {
    public final String ORDER_ROOT = "/orders";

    public ValidatableResponse createOrder(File file) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(file)
                .when()
                .post(ORDER_ROOT)
                .then().log().all();
    }
}
