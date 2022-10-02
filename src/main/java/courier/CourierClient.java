package courier;

import io.restassured.response.ValidatableResponse;
public class CourierClient extends BaseClient {
    private final String ROOT = "/courier";
    private final String WRONG_ROOT = "/courieru";
    private final String COURIER = "/courier/{courierId}";
    private final String LOGIN = ROOT + "/login";
    private final String ORDER = "/orders?courierId={courierId}";
    public ValidatableResponse create(Courier courier) {
        return getSpec()
                .body(courier)
                .when()
                .post(ROOT)
                .then().log().all();
    }
    public ValidatableResponse createWrongRoot(Courier courier) {
        return getSpec()
                .body(courier)
                .when()
                .post(WRONG_ROOT)
                .then().log().all();
    }
    public ValidatableResponse createWrongMethod(Courier courier) {
        return getSpec()
                .body(courier)
                .when()
                .get(ROOT)
                .then().log().all();
    }
    public ValidatableResponse createFailed(Courier courier) {
        return getSpec()
                .body(courier)
                .when()
                .post(ROOT)
                .then().log().all();
    }
    public ValidatableResponse login(CourierCredentials creds) {
        return getSpec()
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all();
    }
    public ValidatableResponse loginWrong(CourierCredentials creds) {
        return getSpec()
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all();
    }
    public void delete(int courierId) {
        getSpec()
                .pathParam("courierId", courierId)
                .when()
                .delete(COURIER)
                .then().log().all()
                .assertThat()
                .statusCode(200);
    }
    public ValidatableResponse getOrders(int courierId) {
        return getSpec()
                .pathParam("courierId", courierId)
                .when()
                .get(ORDER)
                .then().log().all();
    }
}


