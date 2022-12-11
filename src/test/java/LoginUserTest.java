import user.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;
import user.GeneratorUser;
import user.UserCredentials;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest {
    private User user;
    private UserClient userClient;
    @Before
    public void start(){
        user= GeneratorUser.getDefault();
        userClient = new UserClient();
        userClient.createUser(user);
    }
    @Test
    @DisplayName("check login user with valid date")
    public void checkLoginUserWithValidDate(){
        Response response = userClient.loginUser( UserCredentials.from(user));
        response.then().assertThat().statusCode(200).body("success",equalTo(true)).and()
                .and().body("accessToken",notNullValue())
                .and().body("refreshToken",notNullValue())
                .and().body("user.email",equalTo(user.getEmail()))
                .and().body("user.name",equalTo(user.getName()));
    }
    @Test
    @DisplayName("check login user with invalid email")
    public void checkLoginUserWithInvalidEmail(){
       user.setEmail(user.getEmail()+"hkhkl");
        Response response = userClient.loginUser(UserCredentials.from(user));
        response.then().assertThat().statusCode(401).and().body("success",equalTo(false))
                .and().body("message",equalTo("email or password are incorrect"));
    }
    @Test
    @DisplayName("check login user with invalid password")
    public void checkLoginUserWithInvalidPassword(){
        user.setPassword(user.getPassword()+"hhdjds");
        Response response = userClient.loginUser(UserCredentials.from(user));
        response.then().assertThat().statusCode(401).and().body("success",equalTo(false))
                .and().body("message",equalTo("email or password are incorrect"));
    }
    @After
    public void end(){
        Response response = userClient.loginUser(UserCredentials.from(user));
        String accessToken = response.then().extract().path("accessToken");
        userClient.delete(accessToken);
    }
}
