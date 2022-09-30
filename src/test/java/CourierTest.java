import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CourierTest {
    Courier courier;
    CourierClient courierClient;
    private int courierId;
    @Before
    public void setup() {
        courier = Courier.getRandomCourier();
        courierClient = new CourierClient();
    }
    @After
    public void deleteCourier() {
        CourierCredentials creds = CourierCredentials.from(courier);
        courierId = courierClient.login(creds)
                .extract().path("id");
        assertNotEquals(0, courierId);
        courierClient.delete(courierId);
    }
    // 1. Проверить что курьера можно создать;
    @Test
    public void courierCreateTest() {
        courierClient.create(courier)
                .statusCode(201);
    }
    // 2. Проверить, что нельзя создать двух одинаковых курьеров;
    @Test
    public void courierCreateTestTwoCouriers() {
        courierClient.create(courier);
        courierClient.create(courier)
                .statusCode(409);
    }
    // 7. Проверить, что успешный запрос возвращает ok: true;
    @Test
    public void courierTestReturnTrue() {
        boolean isOk = courierClient.create(courier)
                .extract().path("ok");
                assertTrue(isOk);
    }
    // 10. Проверить, что если создавать курьера с существующим логином или паролем, вернется ошибка
    @Test
    public void courierTestTwoCouriersMassage() {
        courierClient.create(courier);
        String loginAlreadyExists = courierClient.create(courier)
                .statusCode(409)
                .extract().path("message");
        assertEquals("Этот логин уже используется. Попробуйте другой.", loginAlreadyExists);
    }
    //Задание 5. Получить список заказов курьера
    @Test
    public void courierGetOrders() {
        courierClient.create(courier);
        CourierCredentials creds = CourierCredentials.from(courier);
        courierId = courierClient.login(creds)
            .extract().path("id");
        courierClient.getOrders(courierId);
    }
}