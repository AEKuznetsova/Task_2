package ingredient;

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

    public IngredientData() {
        putTestData();
    }

    private void putTestData() {
        ingredients.add(new Ingredient(
                "61c0c5a71d1f82001bdaaa6d",
                "Флюоресцентная булка R2-D3",
                "bun",
                44, 26, 85, 643, 988,
                "https://code.s3.yandex.net/react/code/bun-01.png",
                "https://code.s3.yandex.net/react/code/bun-01-mobile.png",
                "https://code.s3.yandex.net/react/code/bun-01-large.png",
                0
        ));

    }
}

