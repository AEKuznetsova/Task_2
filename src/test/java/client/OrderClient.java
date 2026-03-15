package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;

public class OrderClient extends Client {
    private static final String ORDERS_PATH = "/orders";
    private static final String HEADER_AUTH = "Authorization";

    @Step("Отправка запроса на создание заказа без авторизации")
    public Response sendOrderWithoutLogin(Map<String, List<String>> ingredientsData) {
        return specification()
                .body(ingredientsData)
                .post(ORDERS_PATH);
    }

    @Step("Создание заказа авторизованным пользователем")
    public Response sendOrderWithLogin(Map<String, List<String>> ingredientsData, String token) {
        return specification()
                .header(HEADER_AUTH, token)
                .body(ingredientsData)
                .post(ORDERS_PATH);
    }

    @Step("Получение заказов пользователя с авторизацией")
    public Response getUserOrders(String token) {
        return specification()
                .header(HEADER_AUTH, token)
                .get(ORDERS_PATH);
    }

    @Step("Получение заказов без авторизации")
    public Response getAnonymousOrders() {
        return specification()
                .get(ORDERS_PATH);
    }
}