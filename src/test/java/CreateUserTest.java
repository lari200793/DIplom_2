import user.UserClient;
import user.GeneratorUser;
import user.User;
import user.UserCredentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTest {
     private User user;
     private UserClient userClient;
     private String accessToken;

    @Before
    public void start(){
        user = GeneratorUser.getDefault();
        userClient = new UserClient();

    }
    @Test
    @DisplayName("check create user")
    public void checkCreateUser(){
        Response response = userClient.createUser(user);
        response.then().assertThat().statusCode(200).and().body("success", equalTo(true))
                .and().body("accessToken",notNullValue())
                .and().body("refreshToken",notNullValue());

    }
    @Test
    @DisplayName("check creating identical users")
    public void checkCreatingIdenticalUsers(){
        userClient.createUser(user);
        Response response = userClient.createUser(user);
        response.then().statusCode(403).and().assertThat().body("success", equalTo(false))
        .and().body("message",equalTo("User already exists"));

    }
    @After
    public void deleteUser(){
    Response response = userClient.loginUser(UserCredentials.from(user));
    accessToken = response.then().extract().path("accessToken");
    userClient.delete(accessToken);

    }
}
