import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

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
// 1. Проверить что курьер может авторизоваться;
@Test
public void courierlodinTest() {
    courierClient.create(courier);
    CourierCredentials creds = CourierCredentials.from(courier);
    courierClient.login(creds);
    courierId = courierClient.login(creds)
        .extract().path("id");
    courierClient.delete(courierId);
}
//для авторизации нужно передать все обязательные поля;
//2. Проверить что нельзя авторизоваться без логина
@Test
public void courierLoginWithoutLoginTest() {
    courierClient.create(courier);
    CourierCredentials credsNoLogin = CourierCredentials.getWithoutLogin(courier);
    CourierCredentials creds = CourierCredentials.from(courier);
    courierClient.loginWrong(credsNoLogin);
    courierId = courierClient.login(creds)
            .extract().path("id");
    courierClient.delete(courierId);
}

//3. Проверить что нельзя авторизоваться без пароля
@Test
public void courierLoginWithoutPasswordTest() {
    courierClient.create(courier);
    CourierCredentials credsNoPassword = CourierCredentials.getWithoutPassword(courier);
    CourierCredentials creds = CourierCredentials.from(courier);
    courierClient.loginWrong(credsNoPassword);
    courierId = courierClient.login(creds)
            .extract().path("id");
    courierClient.delete(courierId);
}

//  если какого-то поля нет, запрос возвращает ошибку;
//4. Проверить что система вернет ошибку если не указать логин
@Test
public void courierLoginWithoutLoginTestMassage() {
    courierClient.create(courier);
    CourierCredentials credsNoLogin = CourierCredentials.getWithoutLogin(courier);
    CourierCredentials creds = CourierCredentials.from(courier);
    courierClient.loginWrong(credsNoLogin);
    String massage = courierClient.loginWrong(credsNoLogin)
            .extract().path("message");
    assertEquals("Недостаточно данных для входа", massage);
    courierId = courierClient.login(creds)
            .extract().path("id");
    courierClient.delete(courierId);
}
//5. Проверить что система вернет ошибку если не указать пароль
@Test
public void courierLoginWithoutPasswordTestMassage() {
    courierClient.create(courier);
    CourierCredentials credsNoPassword = CourierCredentials.getWithoutPassword(courier);
    CourierCredentials creds = CourierCredentials.from(courier);
    courierClient.loginWrong(credsNoPassword)
            .statusCode(400);
    String massage = courierClient.loginWrong(credsNoPassword)
        .extract().path("message");
    assertEquals("Недостаточно данных для входа", massage);
    courierId = courierClient.login(creds)
         .extract().path("id");
    courierClient.delete(courierId);
}
//    система вернёт ошибку, если неправильно указать логин или пароль;
//6. Проверить что система вернет ошибку если неправильно указать пароль
    @Test
    public void courierLoginWrongPasswordTestMassage() {
        courierClient.create(courier);
        CourierCredentials credsWrongPassword = CourierCredentials.getWrongPassword(courier);
        CourierCredentials creds = CourierCredentials.from(courier);
        courierClient.loginWrong(credsWrongPassword)
                .statusCode(404);
        String massage = courierClient.loginWrong(credsWrongPassword)
                .extract().path("message");
        assertEquals("Учетная запись не найдена", massage);
        courierId = courierClient.login(creds)
                .extract().path("id");
        courierClient.delete(courierId);
    }
//7. Проверить что система вернет ошибку если неправильно указать логин
    @Test
    public void courierLoginWrongLoginTestMassage() {
        courierClient.create(courier);
        CourierCredentials credsWrongPLogin = CourierCredentials.getWrongLogin(courier);
        CourierCredentials creds = CourierCredentials.from(courier);
        courierClient.loginWrong(credsWrongPLogin)
                .statusCode(404);
        String massage = courierClient.loginWrong(credsWrongPLogin)
                .extract().path("message");
        assertEquals("Учетная запись не найдена", massage);
        courierId = courierClient.login(creds)
                .extract().path("id");
        courierClient.delete(courierId);
    }
//8. Eсли авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
@Test
public void courierLoginWrongCourierTestMassage() {
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
    courierClient.delete(courierId);
}
}
