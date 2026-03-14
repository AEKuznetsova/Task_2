package client;

import io.restassured.response.Response;

public class IngredientClient extends Client {
    private static final String INGREDIENTS_ENDPOINT = "/ingredients";

    public Response fetchAllIngredients() {
        return specification()
                .get(INGREDIENTS_ENDPOINT);
    }
}
