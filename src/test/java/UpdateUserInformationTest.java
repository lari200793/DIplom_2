import user.User;
import user.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import user.GeneratorUser;
import org.junit.Test;
import user.UserCredentials;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateUserInformationTest {
    private User user;
    private UserClient userClient;
    private Response response;

    @Before
    public void start() {
        user = GeneratorUser.getDefault();
        userClient = new UserClient();
        response = userClient.createUser(user);
    }
    @Test
    @DisplayName("update date user with authorization success")
    public void updateDateUserWithAuthorization_Success(){
         response = userClient.loginUser(user);
         String accessToken = response.then().extract().path("accessToken");
         userClient.getInfo(accessToken);
         user = GeneratorUser.getDefault();
         response = userClient.updateInfo(user, accessToken);
         response.then().assertThat().statusCode(200).body("success",equalTo(true))
                 .and().body("user.email",equalTo(user.getEmail()))
                 .and().body("user.name",equalTo(user.getName()));
    }
    @Test
    @DisplayName("update date user without authorization not success")
    public void updateDateUserWithoutAuthorization_NotSuccess() {
        String accessToken = response.then().extract().path("accessToken");
        userClient.getInfo(accessToken);
        user = GeneratorUser.getDefault();
        response = userClient.updateInfoWithoutAuthorization(user);
        response.then().assertThat().statusCode(401).body("success", equalTo(false))
                .and().body( "message", equalTo("You should be authorised"));
    }
    @After
    public void end() {
        Response response = userClient.loginUser(UserCredentials.from(user));
        String accessToken = response.then().extract().path("accessToken");
        userClient.delete(accessToken);
    }
}
