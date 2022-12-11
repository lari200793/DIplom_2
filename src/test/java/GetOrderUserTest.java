import order.OrderClient;
import order.OrderGenerator;
import order.Order;
import user.User;
import user.UserClient;
import user.GeneratorUser;
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
    OrderClient orderClient;
    String accessToken;
    @Before
    public  void start(){
        user = GeneratorUser.getDefault();
        userClient =new UserClient();
        accessToken = userClient.createUser(user).then().extract().path("accessToken");
        orderClient = new OrderClient();
        Response response = orderClient.getIngredients();
        List<String> jsonResponse =  response.then().extract().body().jsonPath().getList("data._id");
        for(int i =0;i<3;i++ ) {
            order = OrderGenerator.getDefault(jsonResponse);
            orderClient.createOrderWithAuthorization(order, accessToken);
        }
    }

    @Test
    @DisplayName("check get order user with authorization")
    public void checkGetOrderUserWithAuthorization(){
        Response responseGetOrder = orderClient.getOrdersWithAuth(accessToken);
         responseGetOrder.then().assertThat().statusCode(200).body("success",equalTo(true)).and()
                 .body("orders._id", hasSize(3));

    }
    @Test
    @DisplayName("check get order user without authorization")
    public void checkGetOrderUserWithoutAuthorization(){
        Response responseGetOrder = orderClient.getOrdersWithoutAuth();
        responseGetOrder.then().assertThat().statusCode(401).body("success",equalTo(false)).and()
                .body("message",  equalTo( "You should be authorised"));
    }
    @After
    public void end(){
        userClient.delete(accessToken);
    }
}
