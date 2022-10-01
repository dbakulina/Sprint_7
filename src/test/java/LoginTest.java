import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LoginTest {
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
        courierClient.delete(courierId);
    }
    // 1. Проверить что курьер может авторизоваться;
    @Test
    public void courierlodinTest() {
        courierClient.create(courier);
        CourierCredentials creds = CourierCredentials.from(courier);
        courierId = courierClient.login(creds)
            .extract().path("id");
            courierClient.login(creds)
                .assertThat()
                .statusCode(200);

    }
    //для авторизации нужно передать все обязательные поля;
    //2. Проверить что нельзя авторизоваться без логина
    @Test
    public void courierLoginWithoutLoginTest() {
        courierClient.create(courier);
        CourierCredentials credsNoLogin = CourierCredentials.getWithoutLogin(courier);
        courierClient.loginWrong(credsNoLogin)
                .assertThat()
                .statusCode(400);
    }
    //3. Проверить что нельзя авторизоваться без пароля
    @Test
    public void courierLoginWithoutPasswordTest() {
        courierClient.create(courier);
        CourierCredentials credsNoPassword = CourierCredentials.getWithoutPassword(courier);
        courierClient.loginWrong(credsNoPassword)
                .assertThat()
                .statusCode(400);
    }
    //  если какого-то поля нет, запрос возвращает ошибку;
    //4. Проверить что система вернет ошибку если не указать логин
    @Test
    public void courierLoginWithoutLoginTestMassage() {
        courierClient.create(courier);
        CourierCredentials credsNoLogin = CourierCredentials.getWithoutLogin(courier);
        courierClient.loginWrong(credsNoLogin);
        String massage = courierClient.loginWrong(credsNoLogin)
                .extract().path("message");
        assertEquals("Недостаточно данных для входа", massage);
    }
    //5. Проверить что система вернет ошибку если не указать пароль
    @Test
    public void courierLoginWithoutPasswordTestMassage() {
        courierClient.create(courier);
        CourierCredentials credsNoPassword = CourierCredentials.getWithoutPassword(courier);
        courierClient.loginWrong(credsNoPassword)
                .statusCode(400);
        String massage = courierClient.loginWrong(credsNoPassword)
            .extract().path("message");
        assertEquals("Недостаточно данных для входа", massage);
    }
    //    система вернёт ошибку, если неправильно указать логин или пароль;
    //6. Проверить что система вернет ошибку если неправильно указать пароль
    @Test
    public void courierLoginWrongPasswordTestMassage() {
        courierClient.create(courier);
        CourierCredentials credsWrongPassword = CourierCredentials.getWrongPassword(courier);
        courierClient.loginWrong(credsWrongPassword)
                .statusCode(404);
        String massage = courierClient.loginWrong(credsWrongPassword)
                .extract().path("message");
        assertEquals("Учетная запись не найдена", massage);
    }
    //7. Проверить что система вернет ошибку если неправильно указать логин
    @Test
    public void courierLoginWrongLoginTestMassage() {
        courierClient.create(courier);
        CourierCredentials credsWrongPLogin = CourierCredentials.getWrongLogin(courier);
        courierClient.loginWrong(credsWrongPLogin)
                .statusCode(404);
        String massage = courierClient.loginWrong(credsWrongPLogin)
                .extract().path("message");
        assertEquals("Учетная запись не найдена", massage);
    }
    //8. Eсли авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    @Test
    public void courierLoginWrongCourierTestMassage() {
        courierClient.create(courier);
        CourierCredentials credsWrongCourier = CourierCredentials.getWrongCourier(courier);
        courierClient.loginWrong(credsWrongCourier)
                .statusCode(404);
        String massage = courierClient.loginWrong(credsWrongCourier)
                .extract().path("message");
        assertEquals("Учетная запись не найдена", massage);
    }
    //9. Проверить что успешный запрос возвращает id.
    @Test
    public void courierlodinTestReturnId() {
        courierClient.create(courier);
        CourierCredentials creds = CourierCredentials.from(courier);
        courierClient.login(creds);
        courierId = courierClient.login(creds)
                .extract().path("id");
        assertNotEquals(0, courierId);
    }
}
