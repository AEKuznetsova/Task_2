import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.*;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.OrderClient;
import client.UserClient;
import generator.IngredientsGenerator;
import generator.UserGenerator;
import user.User;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.*;

public class MakeOrderTests {
    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();
    private final IngredientsGenerator ingredientsGenerator = new IngredientsGenerator();
    private String accessToken;
    private User user;

    @Before
    public void setup() {
        user = UserGenerator.generateValidUser();
        Response resp = userClient.register(user);
        accessToken = resp.body().path("accessToken");
    }

    @Test
    @DisplayName("Создать заказ авторизованным пользователем")
    public void createOrderWithLogin() {
        Map<String, List<String>> data = ingredientsGenerator.getValidIngredients();
        Response response = orderClient.sendOrderWithLogin(data, accessToken);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создать заказа неавторизованным пользователем")
    public void createOrderWithoutLogin() {
        Map<String, List<String>> data = ingredientsGenerator.getValidIngredients();
        Response response = orderClient.sendOrderWithoutLogin(data);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создать заказ без ингредиентов")
    public void createOrderWithoutIngredients() {
        Map<String, List<String>> data = ingredientsGenerator.getEmptyIngredients();
        Response response = orderClient.sendOrderWithLogin(data, accessToken);
        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создать заказ с несуществующими ингредиентами")
    public void createOrderWithInvalidIngredients() {
        Map<String, List<String>> data = ingredientsGenerator.getInvalidIngredients();
        Response response = orderClient.sendOrderWithLogin(data, accessToken);
        response.then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(user, accessToken);
        }
    }
}
