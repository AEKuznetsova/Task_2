import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.*;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import client.UserClient;
import generator.UserGenerator;
import user.User;
import static org.hamcrest.CoreMatchers.*;

public class MakeUserTests {
    private final UserClient userClient = new UserClient();
    private User user;
    private String accessToken;

    @Test
    @DisplayName("Можно зарегистрироваться, введя корректные данные")
    public void shouldRegisterUserSuccessfully() {
        user = UserGenerator.generateValidUser();
        Response response = userClient.register(user);
        accessToken = response.body().path("accessToken");

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться без имейла")
    public void shouldNotRegisterUserWithoutEmail() {
        user = UserGenerator.generateValidUser();
        user.setEmail(null);
        Response response = userClient.register(user);
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться без имени")
    public void shouldNotRegisterUserWithoutName() {
        user = UserGenerator.generateValidUser();
        user.setName(null);
        Response response = userClient.register(user);
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться без пароля")
    public void shouldNotRegisterUserWithoutPassword() {
        user = UserGenerator.generateValidUser();
        user.setPassword(null);
        Response response = userClient.register(user);
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя зарегистрировать уже существующего пользователя")
    public void shouldNotRegisterExistingUser() {
        user = UserGenerator.generateValidUser();
        userClient.register(user).then().statusCode(HttpStatus.SC_OK);
        Response response = userClient.register(user);
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(user, accessToken);
        }
    }
}
