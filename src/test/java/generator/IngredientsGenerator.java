package generator;

import ingredient.Ingredient;
import ingredient.IngredientData;
import java.util.*;

public class IngredientsGenerator {
    private final Random random = new Random();
    private final List<Ingredient> all = new IngredientData().getIngredients();

    public Map<String, List<String>> getValidIngredients() {
        List<String> selected = new ArrayList<>();
        int count = random.nextInt(all.size()) + 1;

        for (int i = 0; i < count; i++) {
            selected.add(all.get(random.nextInt(all.size())).get_id());
        }

        Map<String, List<String>> map = new HashMap<>();
        map.put("ingredients", selected);
        return map;
    }

    public Map<String, List<String>> getEmptyIngredients() {
        return Map.of("ingredients", new ArrayList<>());
    }

    public Map<String, List<String>> getInvalidIngredients() {
        return Map.of("ingredients", List.of("invalid_ingredient_hash"));
    }
}