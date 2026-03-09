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

public class ChangeUserDataTests {
    private final UserClient userClient = new UserClient();
    private User originalUser;
    private String accessToken;

    @Before
    public void setup() {
        originalUser = UserGenerator.generateValidUser();
        accessToken = userClient.register(originalUser).body().path("accessToken");
    }

    @Test
    @DisplayName("Можно изменить данные авторизованного пользователя")
    public void shouldUpdateUserWithToken() {
        User updatedUser = User.builder()
                .email("upd_" + originalUser.getEmail())
                .password(originalUser.getPassword())
                .name("Измененный")
                .build();

        userClient.updateUser(updatedUser, accessToken)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя изменить данные пользователя без авторизации")
    public void shouldNotUpdateUserWithoutToken() {
        User updatedUser = User.builder()
                .email("upd_" + originalUser.getEmail())
                .password(originalUser.getPassword())
                .name("Некто")
                .build();

        userClient.updateUserWithoutToken(updatedUser)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(originalUser, accessToken);
        }
    }
}
