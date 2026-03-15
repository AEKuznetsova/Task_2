package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import user.User;

public class UserClient extends Client {

    private static final String ENDPOINT = "/auth";
    private static final String HEADER_AUTH = "Authorization";

    @Step("Регистрация пользователя (включая случаи с null-полями)")
    public Response register(User user) {
        return specification()
                .body(user)
                .post(ENDPOINT + "/register");
    }

    @Step("Авторизация пользователя")
    public Response login(User user) {
        return specification()
                .body(user)
                .post(ENDPOINT + "/login");
    }

    @Step("Получение информации о пользователе")
    public Response getUserInfo(String accessToken) {
        return specification()
                .header(HEADER_AUTH, accessToken)
                .get(ENDPOINT + "/user");
    }

    @Step("Обновление данных пользователя с токеном")
    public Response updateUser(User user, String accessToken) {
        return specification()
                .header(HEADER_AUTH, accessToken)
                .body(user)
                .patch(ENDPOINT + "/user");
    }

    @Step("Обновление данных пользователя без токена")
    public Response updateUserWithoutToken(User user) {
        return specification()
                .body(user)
                .patch(ENDPOINT + "/user");
    }

    @Step("Удаление пользователя")
    public Response deleteUser(User user, String accessToken) {
        return specification()
                .header(HEADER_AUTH, accessToken)
                .body(user)
                .delete(ENDPOINT + "/user");
    }
}

