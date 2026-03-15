package ingredient;

import client.IngredientClient;
import io.restassured.response.Response;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class IngredientData {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final IngredientClient ingredientClient = new IngredientClient();

    public IngredientData() {
        putTestData();
    }

    private void putTestData() {
        Response response = ingredientClient.fetchAllIngredients();
        IngredientResponse ingredientResponse = response.as(IngredientResponse.class);

        if (ingredientResponse != null && ingredientResponse.getData() != null) {
            ingredients.addAll(ingredientResponse.getData());
        }
    }
}
