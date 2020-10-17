package jimmy.mcgymmy.storage;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.model.food.Carbohydrate;
import jimmy.mcgymmy.model.food.Fat;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.model.food.Protein;
import jimmy.mcgymmy.testutil.Assert;
import jimmy.mcgymmy.testutil.TypicalFoods;

public class JsonAdaptedFoodTest {
    private static final String INVALID_NAME = "R@ice";
    private static final String INVALID_PROTEIN = "512%)34";
    private static final String INVALID_FAT = " ";
    private static final String INVALID_CARBS = "123.132";
    private static final String INVALID_TAG = "##lucnsh";

    private static final String VALID_NAME = TypicalFoods.NASI_LEMAK.getName().toString();
    private static final String VALID_PROTEIN = Integer.toString(TypicalFoods.NASI_LEMAK.getProtein().getAmount());
    private static final String VALID_FAT = Integer.toString(TypicalFoods.NASI_LEMAK.getFat().getAmount());
    private static final String VALID_CARBS = Integer.toString(TypicalFoods.NASI_LEMAK.getCarbs().getAmount());
    private static final List<JsonAdaptedTag> VALID_TAGS = TypicalFoods.NASI_LEMAK.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validFoodDetails_returnsPerson() throws Exception {
        JsonAdaptedFood food = new JsonAdaptedFood(TypicalFoods.NASI_LEMAK);
        Assertions.assertEquals(TypicalFoods.NASI_LEMAK, food.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedFood food =
                new JsonAdaptedFood(INVALID_NAME, VALID_PROTEIN, VALID_FAT, VALID_CARBS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, food::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedFood food = new JsonAdaptedFood(null, VALID_PROTEIN, VALID_FAT, VALID_CARBS, VALID_TAGS);
        String expectedMessage =
                String.format(JsonAdaptedFood.MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, food::toModelType);
    }

    @Test
    public void toModelType_invalidProtein_throwsIllegalValueException() {
        JsonAdaptedFood food =
                new JsonAdaptedFood(VALID_NAME, INVALID_PROTEIN, VALID_FAT, VALID_CARBS, VALID_TAGS);
        String expectedMessage = Protein.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, food::toModelType);
    }

    @Test
    public void toModelType_invalidFat_throwsIllegalValueException() {
        JsonAdaptedFood food =
                new JsonAdaptedFood(VALID_NAME, VALID_PROTEIN, INVALID_FAT, VALID_CARBS, VALID_TAGS);
        String val = VALID_PROTEIN;
        String val2 = INVALID_FAT;
        String expectedMessage = Fat.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, food::toModelType);
    }

    @Test
    public void toModelType_invalidCarbs_throwsIllegalValueException() {
        JsonAdaptedFood food =
                new JsonAdaptedFood(VALID_NAME, VALID_PROTEIN, VALID_FAT, INVALID_CARBS, VALID_TAGS);
        String expectedMessage = Carbohydrate.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, food::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedFood food =
                new JsonAdaptedFood(VALID_NAME, VALID_PROTEIN, VALID_FAT, VALID_CARBS, invalidTags);
        Assert.assertThrows(IllegalValueException.class, food::toModelType);
    }

}
