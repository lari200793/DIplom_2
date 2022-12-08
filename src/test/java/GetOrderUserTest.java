import Oder.OderClient;
import Oder.OderGenerator;
import Oder.Order;
import User.User;
import User.UserClient;
import User.GeneratorUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class GetOrderUserTest {
    User user;
    UserClient userClient;
    Order order;
    OderClient orderClient;
    String accessToken;
    @Before
    public  void start(){
        user = GeneratorUser.getDefault();
        userClient =new UserClient();
        accessToken = userClient.createUser(user).then().extract().path("accessToken");
        orderClient = new OderClient();
        Response response = orderClient.getIngredients();
        List<String> jsonResponse =  response.then().extract().body().jsonPath().getList("data._id");
        for(int i =0;i<3;i++ ) {
            order = OderGenerator.getDefault(jsonResponse);
            orderClient.creatingOrderWithAuthorization(order, accessToken);
        }
    }

    @Test
    @DisplayName("check get order user with authorization")
    public void checkGetOrderUserWithAuthorization(){
        Response response1 = orderClient.getOrdersWithAuth(accessToken);
         response1.then().assertThat().statusCode(200).body("success",equalTo(true)).and()
                 .body("orders._id", hasSize(3));

    }
    @Test
    @DisplayName("check get order user without authorization")
    public void checkGetOrderUserWithoutAuthorization(){
        Response response1 = orderClient.getOrdersWithoutAuth();
        response1.then().assertThat().statusCode(401).body("success",equalTo(false)).and()
                .body("message",  equalTo( "You should be authorised"));
    }
    @After
    public void end(){
        userClient.delete(accessToken);
    }
}
