package Oder;

import Client.Client;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OderClient extends Client {
    private final String GET_ID_INGREDIENTS = "/api/ingredients";
    private final String CREATING_ORDER = "/api/orders";
    private final String GET_ORDER_USER = "/api/orders";
    @Step("get ingredients")
    public Response getIngredients(){
         return given()
                        .spec(getSpec())
                        .when()
                        .get(GET_ID_INGREDIENTS);


    }
    @Step("creating order with authorization")
    public Response creatingOrderWithAuthorization(Order ingredients, String accessToken){

        return given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .body(ingredients)
                .when()
                .post(CREATING_ORDER);


    }
    @Step("creating order without authorization")
    public Response creatingOrderWithoutAuthorization(Order ingredients){
        return given()
                .spec(getSpec())
                .body(ingredients)
                .when()
                .post(CREATING_ORDER);


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
