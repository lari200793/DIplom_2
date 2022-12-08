import Oder.Order;
import Oder.OderClient;
import Oder.OderGenerator;
import User.GeneratorUser;
import User.User;
import User.UserClient;
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
    private OderClient oderClient;
    private OderGenerator oderGenerator;
    private String accessToken;
    @Before
    public void start(){
        user= GeneratorUser.getDefault();
        userClient = new UserClient();
        accessToken = userClient.createUser(user).then().extract().path("accessToken");
        oderClient = new OderClient();
        Response response = oderClient.getIngredients();
        List<String>jsonResponse =  response.then().extract().body().jsonPath().getList("data._id");
        order = OderGenerator.getDefault(jsonResponse);
    }
    @Test
    @DisplayName("create order with authorization")
    public void createOrderWithAuthorization(){
        Response response1 = oderClient.creatingOrderWithAuthorization(order,accessToken);
        response1.then().assertThat().statusCode(200).and().body("success", equalTo(true)).
                and().body("name",notNullValue()).
                and().body("order.number",notNullValue());
    }
    @Test
    @DisplayName("create order without authorization")
    public void createOrderWithoutAuthorization(){
        Response response1 = oderClient.creatingOrderWithoutAuthorization(order);
        response1.then().assertThat().statusCode(401).and().body("success", equalTo(false)).
                and().body("message",equalTo("You should be authorised"));

    }
    @Test
    @DisplayName("create order without ingredients")
    public void createOrderWithoutIngredients(){
        order.ingredients.clear();
        Response response1 = oderClient.creatingOrderWithAuthorization(order,accessToken);
        response1.then().assertThat().statusCode(400).and().body("success", equalTo(false)).
                and().body("message",equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("create order with invalid ingredients")
    public void createOrderWithInvalidIngredients() {
        order = OderGenerator.getDefaultInvalidHashIngredients();
        Response response1 = oderClient.creatingOrderWithAuthorization(order, accessToken);
        response1.then().assertThat().statusCode(500);
    }
    @After
    public void end(){
        userClient.delete(accessToken);
    }
}
