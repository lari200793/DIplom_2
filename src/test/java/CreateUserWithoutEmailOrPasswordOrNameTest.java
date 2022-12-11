import user.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class CreateUserWithoutEmailOrPasswordOrNameTest {
    private User user;
    private UserClient userClient;
    private int expectedStatusCode;
    private String expectedMessage;
    private boolean expectedSuccess;

    public CreateUserWithoutEmailOrPasswordOrNameTest(User user, int expectedStatusCode, String expectedMessage,boolean expectedSuccess) {
        this.user = user;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedMessage = expectedMessage;
        this.expectedSuccess = expectedSuccess;
    }
    @Parameterized.Parameters
    public static Object[][] getData(){
        return new Object[][]{
                {GeneratorUser.getWithoutEmail(),403, "Email, password and name are required fields",false},
                {GeneratorUser.getWithoutPassword(),403,"Email, password and name are required fields",false},
                {GeneratorUser.getWithoutName(),403,"Email, password and name are required fields",false},
                {GeneratorUser.getEmptyEmail(),403,"Email, password and name are required fields",false},
                {GeneratorUser.getEmptyPassword(),403,"Email, password and name are required fields",false},
                {GeneratorUser.getEmptyName(),403,"Email, password and name are required fields",false}

        };
    }
    @Before
    public void start (){
        userClient = new UserClient();
    }
    @Test
    @DisplayName("create user invalid data")
    public void createUserInvalidData(){
        Response response = userClient.createUser(user);
        response.then().assertThat().statusCode(expectedStatusCode).and().body("success", equalTo(expectedSuccess)).and().body("message",equalTo(expectedMessage));
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {}
    }
    @After
    public void end(){
        Response response = userClient.loginUser(UserCredentials.from(user));
        String accessToken = response.then().extract().path("accessToken");
        userClient.delete(accessToken);

    }

}

