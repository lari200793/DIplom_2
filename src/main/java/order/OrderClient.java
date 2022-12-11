package order;

import client.Client;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private final String GET_ID_INGREDIENTS = "/api/ingredients";
    private final String CREATE_ORDER = "/api/orders";
    private final String GET_ORDER_USER = "/api/orders";
    @Step("get ingredients")
    public Response getIngredients(){
         return given()
                        .spec(getSpec())
                        .when()

                        .get(GET_ID_INGREDIENTS);


    }
    @Step("create order with authorization")
    public Response createOrderWithAuthorization(Order ingredients, String accessToken){

        return given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .body(ingredients)
                .when()
                .post(CREATE_ORDER);


    }
    @Step("create order without authorization")
    public Response createOrderWithoutAuthorization(Order ingredients){
        return given()
                .spec(getSpec())
                .body(ingredients)
                .when()
                .post(CREATE_ORDER);


    }
    @Step("get orders with auth ")
    public Response getOrdersWithAuth(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .when()
                .get(GET_ORDER_USER);
    }
    @Step("get orders without auth")
    public Response getOrdersWithoutAuth() {
        return given()
                .spec(getSpec())
                .when()
                .get(GET_ORDER_USER);
    }
}
