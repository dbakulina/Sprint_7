package courier;
import lombok.Data;

import org.apache.commons.lang3.RandomStringUtils;

import org.checkerframework.checker.units.qual.C;
@Data
public class CourierCredentials {
    private String login;
    private String password;
    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
    public static CourierCredentials from(Courier courier) {
        return new CourierCredentials(courier.getLogin(), courier.getPassword());
    }
    public static CourierCredentials getWithoutLogin (Courier courier)  {
        return new CourierCredentials("", courier.getPassword());
    }
    public static CourierCredentials getWithoutPassword (Courier courier)  {
        return new CourierCredentials(courier.getLogin(), "");
    }
    public static CourierCredentials getWrongPassword (Courier courier)  {
        return new CourierCredentials(courier.getLogin(), "kuku");
    }
    public static CourierCredentials getWrongLogin (Courier courier)  {
        return new CourierCredentials("kuku", courier.getPassword());
    }
    public static CourierCredentials getWrongCourier (Courier courier)  {
        return new CourierCredentials("kuku", "ruru");
    }
}
