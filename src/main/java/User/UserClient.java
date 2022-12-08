package User;
import Client.Client;
import io.qameta.allure.Step;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private final String CREATING_USER = "/api/auth/register";
    private final String LOGIN_USER = "/api/auth/login";
    private final String DELETE_USER = "/api/auth/user";
    private final String GET_INFO = "/api/auth/user";
    @Step(" create user")
    public Response createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(CREATING_USER);
    }
    @Step("login user")
    public Response loginUser(Object object) {
        return given()
                .spec(getSpec())
                .body(object)
                .when()
                .post(LOGIN_USER);
    }
    @Step(" delete user")
    public void delete(String accessToken) {
        if (accessToken == null) {
            return;
        }
        given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .when()
                .delete(DELETE_USER)
                .then()
                .statusCode(202);
    }
    @Step(" get info about user")
    public void getInfo(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .when()
                .get(GET_INFO)
                .then();

    }
    @Step("update date user with authorization")
    public Response updateInfo(User user, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .body(user)
                .when()
                .patch(GET_INFO);
    }
    @Step("update date user without authorization")
    public Response updateInfoWithoutAuthorization(Object object) {
        return given()
                .spec(getSpec())
                .body(object)
                .when()
                .patch(GET_INFO);
    }

}
