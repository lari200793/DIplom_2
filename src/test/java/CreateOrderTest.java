import order.Order;
import order.OrderClient;
import order.OrderGenerator;
import user.GeneratorUser;
import user.User;
import user.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class CreateOrderTest {

    private User user ;
    private UserClient userClient;
    private Order order;
    private OrderClient orderClient;
    private String accessToken;
    @Before
    public void start(){
        user= GeneratorUser.getDefault();
        userClient = new UserClient();
        accessToken = userClient.createUser(user).then().extract().path("accessToken");
        orderClient = new OrderClient();
        Response response = orderClient.getIngredients();
        List<String>jsonResponse =  response.then().extract().body().jsonPath().getList("data._id");
        order = OrderGenerator.getDefault(jsonResponse);
    }
    @Test
    @DisplayName("create order with authorization")
    public void createOrderWithAuthorization(){
        Response responseCreateOrder = orderClient.createOrderWithAuthorization(order,accessToken);
        responseCreateOrder.then().assertThat().statusCode(200).and().body("success", equalTo(true)).
                and().body("name",notNullValue()).
                and().body("order.number",notNullValue());
    }
    @Test
    @DisplayName("create order without authorization")
    public void createOrderWithoutAuthorization(){
        Response responseCreateOrder = orderClient.createOrderWithoutAuthorization(order);
        responseCreateOrder.then().assertThat().statusCode(401).and().body("success", equalTo(false)).
                and().body("message",equalTo("You should be authorised"));

    }
    @Test
    @DisplayName("create order without ingredients")
    public void createOrderWithoutIngredients(){
        order.getIngredients().clear();
        Response responseCreateOrder = orderClient.createOrderWithAuthorization(order,accessToken);
        responseCreateOrder.then().assertThat().statusCode(400).and().body("success", equalTo(false)).
                and().body("message",equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("create order with invalid ingredients")
    public void createOrderWithInvalidIngredients() {
        order = OrderGenerator.getDefaultInvalidHashIngredients();
        Response responseCreateOrder = orderClient.createOrderWithAuthorization(order, accessToken);
        responseCreateOrder.then().assertThat().statusCode(500);
    }
    @After
    public void end(){
        userClient.delete(accessToken);
    }
}
