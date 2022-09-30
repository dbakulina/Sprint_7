import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.restassured.response.ValidatableResponse;
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
    // 1. Проверить что курьера можно создать;
    @Test
    public void courierCreateTest() {
        courierClient.create(courier)
                .statusCode(201);
        CourierCredentials creds = CourierCredentials.from(courier);
        courierId = courierClient.login(creds)
                .extract().path("id");
        assertNotEquals(0, courierId);
        courierClient.delete(courierId);
    }
    // 2. Проверить, что нельзя создать двух одинаковых курьеров;
    @Test
    public void courierCreateTestTwoCouriers() {
        courierClient.create(courier);
        courierClient.create(courier)
                .statusCode(409);
        CourierCredentials creds = CourierCredentials.from(courier);
        courierId = courierClient.login(creds)
                .extract().path("id");
        courierClient.delete(courierId);
    }
    //чтобы создать курьера, нужно передать в ручку все обязательные поля;
    // 3. Проверить, что нельзя создать курьера без пароля
    @Test
    public void createWithoutPassword() {
        courier = Courier.getWithoutPassword();
        courierClient.createFailed(courier)
                .statusCode(400);
    }
    // 4. Проверить, что нельзя создать курьера без логина
    @Test
    public void createWithoutLogin() {
        courier = Courier.getWithoutLogin();
        courierClient.createFailed(courier)
                .statusCode(400);
    }
    // запрос возвращает правильный код ответа; Коды 201 400 и 409 протестированы в предыдущих тестах
    // 5. Код ответа если отправить запрос на несуществующую ручку 404
    @Test
    public void courierCreateWrongRootStatus() {
       int status =  courierClient.createWrongRoot(courier)
        .extract().statusCode();
        assertEquals(404, status);
        System.out.println(status);
    }
    // 6. Код ответа если отправить неверный метод запроса GET
    @Test
    public void courierCreateWrongMethodStatus() {
        courierClient.createWrongMethod(courier)
                .statusCode(404);
    }
    // 7. Проверить, что успешный запрос возвращает ok: true;
    @Test
    public void courierTestReturnTrue() {
        boolean isOk = courierClient.create(courier)
                .extract().path("ok");
        CourierCredentials creds = CourierCredentials.from(courier);
        courierId = courierClient.login(creds)
                .extract().path("id");
        assertTrue(isOk);
        assertNotEquals(0, courierId);
        courierClient.delete(courierId);
    }
    //если одного из полей нет, запрос возвращает ошибку;
    // 8. Проверить, что если создавать запрос без пароля, вернется ошибка
    @Test
    public void createWithoutPasswordMassage() {
        courier = Courier.getWithoutPassword();
        String requestWithoutLoginOrPassword = courierClient.createFailed(courier)
                .statusCode(400)
                .extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", requestWithoutLoginOrPassword);
    }
    // 9. Проверить, что если создавать запрос без логина, вернется ошибка
    @Test
    public void createWithoutLoginMassage() {
        courier = Courier.getWithoutLogin();
        String requestWithoutLoginOrPassword = courierClient.createFailed(courier)
                .statusCode(400)
                .extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", requestWithoutLoginOrPassword);
    }
    // 10. Проверить, что если создавать курьера с существующим логином или паролем, вернется ошибка
    @Test
    public void courierTestTwoCouriersMassage() {
        courierClient.create(courier);
        String loginAlreadyExists = courierClient.create(courier)
                .statusCode(409)
                .extract().path("message");
        CourierCredentials creds = CourierCredentials.from(courier);
        courierId = courierClient.login(creds)
                .extract().path("id");
        assertEquals("Этот логин уже используется. Попробуйте другой.", loginAlreadyExists);
        courierClient.delete(courierId);
    }
    //Задание 5. Получить список заказов курьера
    @Test
    public void courierGetOrders() {
        courierClient.create(courier);
        CourierCredentials creds = CourierCredentials.from(courier);
        courierId = courierClient.login(creds)
            .extract().path("id");
        courierClient.getOrders(courierId);
        courierClient.delete(courierId);
    }
}