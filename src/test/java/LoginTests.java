import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.*;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.UserClient;
import generator.UserGenerator;
import user.User;
import static org.hamcrest.CoreMatchers.*;

public class LoginTests {
    private final UserClient userClient = new UserClient();
    private User testUser;
    private String accessToken;

    @Before
    public void setUp() {
        testUser = UserGenerator.generateValidUser();
        accessToken = userClient.register(testUser).body().path("accessToken");
    }

    @Test
    @DisplayName("Можно успешно авторизоваться, введя корректные данные")
    public void shouldLoginSuccessfully() {
        userClient.login(testUser)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с неверным паролем")
    public void shouldNotLoginWithWrongPassword() {
        testUser.setPassword("wrongPassword");
        userClient.login(testUser)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться без имейла")
    public void shouldNotLoginWithoutEmail() {
        testUser.setEmail(null);
        userClient.login(testUser)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться без пароля")
    public void shouldNotLoginWithoutPassword() {
        testUser.setPassword(null);
        userClient.login(testUser)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(testUser, accessToken);
        }
    }
}
