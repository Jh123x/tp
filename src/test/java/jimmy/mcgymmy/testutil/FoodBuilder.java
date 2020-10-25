package jimmy.mcgymmy.testutil;

import java.util.HashSet;
import java.util.Set;

import jimmy.mcgymmy.model.date.Date;
import jimmy.mcgymmy.model.food.Carbohydrate;
import jimmy.mcgymmy.model.food.Fat;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.model.food.Protein;
import jimmy.mcgymmy.model.tag.Tag;
import jimmy.mcgymmy.model.util.SampleDataUtil;

/**
 * A utility class to help with building Food objects.
 */
public class FoodBuilder {

    public static final String DEFAULT_NAME = "Chicken Rice";
    public static final Integer DEFAULT_PROTEIN = 123456;
    public static final Integer DEFAULT_FAT = 123456;
    public static final Integer DEFAULT_CARB = 123456;

    private Name name;
    private Protein protein;
    private Fat fat;
    private Carbohydrate carbohydrate;
    private Date date;
    private Set<Tag> tags;

    /**
     * Creates a {@code FoodBuilder} with the default details.
     */
    public FoodBuilder() {
        name = new Name(DEFAULT_NAME);
        protein = new Protein(DEFAULT_PROTEIN);
        fat = new Fat(DEFAULT_FAT);
        carbohydrate = new Carbohydrate(DEFAULT_CARB);
        tags = new HashSet<>();
        date = Date.currentDate();
    }

    /**
     * Initializes the FoodBuilder with the data of {@code foodToCopy}.
     */
    public FoodBuilder(Food foodToCopy) {
        name = foodToCopy.getName();
        protein = foodToCopy.getProtein();
        fat = foodToCopy.getFat();
        carbohydrate = foodToCopy.getCarbs();
        tags = new HashSet<>(foodToCopy.getTags());
        date = foodToCopy.getDate();
    }

    /**
     * Sets the {@code Name} of the {@code Food} that we are building.
     */
    public FoodBuilder withName(Name name) {
        this.name = name;
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Food} that we are building.
     */
    public FoodBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Carbohydrate} of the {@code Food} that we are building.
     */
    public FoodBuilder withCarb(String carb) {
        this.carbohydrate = new Carbohydrate(Integer.parseInt(carb));
        return this;
    }

    /**
     * Sets the {@code Protein} of the {@code Food} that we are building.
     */
    public FoodBuilder withProtein(String protein) {
        this.protein = new Protein(Integer.parseInt(protein));
        return this;
    }

    /**
     * Sets the {@code Fat} of the {@code Food} that we are building.
     */
    public FoodBuilder withFat(String fat) {
        this.fat = new Fat(Integer.parseInt(fat));
        return this;
    }

    /**
     * Sets the {@code Date} of the {@code Food} that we are building.
     */
    public FoodBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    public Food build() {
        return new Food(name, protein, fat, carbohydrate, tags, date);
    }

}
