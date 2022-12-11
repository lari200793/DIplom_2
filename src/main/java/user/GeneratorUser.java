package user;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class GeneratorUser {
     private static  Faker faker = new Faker();
    public static String setEmailUserGenerator(){
        return faker.internet().emailAddress();
    }
    public static String setPasswordUserGenerator(){
        return faker.internet().password(2, 15);
    }
    public static String setNameUserGenerator(){
        return faker.name().firstName();
    }
    @Step(" get user with valid date ")
    public static User getDefault (){
        return new User(setEmailUserGenerator(),setPasswordUserGenerator(),setNameUserGenerator());
    }
    @Step(" get user without email")
    public static User getWithoutEmail(){
        return new User(null,setEmailUserGenerator(),setNameUserGenerator());
    }
    @Step ("get user without password")
    public static User getWithoutPassword(){
        return  new User(setEmailUserGenerator(),null,setNameUserGenerator());
    }
    @Step("get user without name")
    public static User getWithoutName(){
        return  new User(setEmailUserGenerator(),setPasswordUserGenerator(),null);
    }
    @Step("get user with empty password")
    public static User getEmptyPassword(){
        return new User(setEmailUserGenerator(),"",setNameUserGenerator());
    }
    @Step(" get user with empty email")
    public static User getEmptyEmail(){
        return new User("",setPasswordUserGenerator(),setNameUserGenerator());
    }
    @Step(" get user with empty name")
    public static User getEmptyName(){
        return new User(setEmailUserGenerator(),setPasswordUserGenerator(),"");
    }

}
