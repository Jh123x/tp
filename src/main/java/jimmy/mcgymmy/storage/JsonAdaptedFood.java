package jimmy.mcgymmy.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.commons.util.AppUtil;
import jimmy.mcgymmy.model.date.Date;
import jimmy.mcgymmy.model.food.Carbohydrate;
import jimmy.mcgymmy.model.food.Fat;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.model.food.Protein;
import jimmy.mcgymmy.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Food}.
 */
class JsonAdaptedFood {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Food's %s field is missing!";

    private final String name;
    private final String protein;
    private final String fat;
    private final String carbs;
    private final String date;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedFood} with the given food details.
     */
    @JsonCreator
    public JsonAdaptedFood(@JsonProperty("name") String name, @JsonProperty("protein") String protein,
                           @JsonProperty("fat") String fat, @JsonProperty("carbs") String carbs,
                           @JsonProperty("date") String date, @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.date = date;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Food} into this class for Jackson use.
     */
    public JsonAdaptedFood(Food source) {
        name = source.getName().fullName;
        protein = Integer.toString(source.getProtein().getAmount());
        fat = Integer.toString(source.getFat().getAmount());
        carbs = Integer.toString(source.getCarbs().getAmount());
        date = source.getDate().toString();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    private void checkNull(Object item, String className) throws IllegalValueException {
        AppUtil.checkArgument(item != null,
                String.format(JsonAdaptedFood.MISSING_FIELD_MESSAGE_FORMAT, className));
    }

    private <T>void checkValid(T item, String className, Predicate<T> isValid, String classError)
            throws IllegalValueException {
        checkNull(item, className);
        AppUtil.checkArgument(isValid.test(item), classError);
    }

    /**
     * Converts this Jackson-friendly adapted food object into the model's {@code Food} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted food.
     */
    public Food toModelType() throws IllegalValueException {

        //Check all variables
        checkValid(fat, Fat.class.getSimpleName(), Fat::isValid, Fat.MESSAGE_CONSTRAINTS);
        checkValid(date, Date.class.getSimpleName(), Date::isValid, Date.MESSAGE_CONSTRAINTS);
        checkValid(name, Name.class.getSimpleName(), Name::isValidName, Name.MESSAGE_CONSTRAINTS);
        checkValid(protein, Protein.class.getSimpleName(), Protein::isValid, Protein.MESSAGE_CONSTRAINTS);
        checkValid(carbs, Carbohydrate.class.getSimpleName(), Carbohydrate::isValid, Carbohydrate.MESSAGE_CONSTRAINTS);

        //Load Tags using a for loop due to IllegalValueException.
        final List<Tag> foodTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            foodTags.add(tag.toModelType());
        }

        //Create Object from data
        final Name modelName = new Name(name);
        final Date modelDate = new Date(date);
        final Set<Tag> modelTags = new HashSet<>(foodTags);
        final Fat modelFat = new Fat(Integer.parseInt(fat));
        final Protein modelProtein = new Protein(Integer.parseInt(protein));
        final Carbohydrate modelCarbohydrate = new Carbohydrate(Integer.parseInt(carbs));

        //Return the Food item.
        return new Food(modelName, modelProtein, modelFat, modelCarbohydrate, modelTags, modelDate);
    }

}
