import courier.Courier;
import courier.CourierClient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CourierTestWithoutCreationCourier {
    Courier courier;
    CourierClient courierClient;
    private int courierId;
    @Before
    public void setup() {
        courier = Courier.getRandomCourier();
        courierClient = new CourierClient();
    }
    //чтобы создать курьера, нужно передать в ручку все обязательные поля;
    // 3. Проверить, что нельзя создать курьера без пароля
    @Test
    public void createWithoutPassword() {
        courier = Courier.getWithoutPassword();
        courierClient.createFailed(courier)
                .assertThat()
                .statusCode(400);
    }
    // 4. Проверить, что нельзя создать курьера без логина
    @Test
    public void createWithoutLogin() {
        courier = Courier.getWithoutLogin();
        courierClient.createFailed(courier)
                .assertThat()
                .statusCode(400);
    }
    // запрос возвращает правильный код ответа; Коды 201 400 и 409 протестированы в предыдущих тестах
    // 5. Код ответа если отправить запрос на несуществующую ручку 404
    @Test
    public void courierCreateWrongRootStatus() {
        courierClient.createWrongRoot(courier)
                .assertThat()
                .statusCode(404);
    }
    // 6. Код ответа если отправить неверный метод запроса GET
    @Test
    public void courierCreateWrongMethodStatus() {
        courierClient.createWrongMethod(courier)
                .assertThat()
                .statusCode(404);
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
}
