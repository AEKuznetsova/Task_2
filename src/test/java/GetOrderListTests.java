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

public class GetOrderListTests {
    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();
    private final IngredientsGenerator ingredientsGenerator = new IngredientsGenerator();
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        user = UserGenerator.generateValidUser();
        accessToken = userClient.register(user).body().path("accessToken");
        Map<String, List<String>> ingr = ingredientsGenerator.getValidIngredients();
        orderClient.sendOrderWithLogin(ingr, accessToken);
    }

    @Test
    @DisplayName("Получить список заказов авторизованным пользователем")
    public void getOrdersWithAuth() {
        Response response = orderClient.getUserOrders(accessToken);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Нельзя получить список заказов без авторизации")
    public void getOrdersWithoutAuth() {
        Response response = orderClient.getAnonymousOrders();
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(user, accessToken);
        }
    }
}
