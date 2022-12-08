package Oder;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class OderGenerator {
 private static Faker faker = new Faker();


    public static List<String> ingredients= new ArrayList<>();

    public static List<String> getIngredients(List<String> jsonResponse) {
        ingredients.add(jsonResponse.get(faker.number().numberBetween(0, 14)));
        ingredients.add(jsonResponse.get(faker.number().numberBetween(0, 14)));
        ingredients.add(jsonResponse.get(faker.number().numberBetween(0, 14)));
        return ingredients;
    }
    public static List<String> getIngredientsInvalidDate() {
        ingredients.add(faker.bothify("?#########????#dfq"));
        ingredients.add(faker.bothify("?#########????#wrweq"));
        ingredients.add(faker.bothify("?#########????#wqw"));
        return ingredients;
    }
    @Step("get order  valid date")
    public static  Order getDefault(List<String> jsonResponse){
        return new Order(getIngredients( jsonResponse));
    }
    @Step(" get order invalid date")
    public static Order getDefaultInvalidHashIngredients(){
       return new Order(getIngredientsInvalidDate());
    }
}
