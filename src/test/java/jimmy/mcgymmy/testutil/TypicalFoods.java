package jimmy.mcgymmy.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Name;

/**
 * A utility class containing a list of {@code Food} objects to be used in tests.
 */
public class TypicalFoods {

    public static final Food CHICKEN_RICE = new FoodBuilder().withName(new Name("Chicken Rice"))
            .withProtein("253").withFat("123")
            .withCarb("456").withDate("20 Apr 2020")
            .withTags("lunch").build();
    public static final Food NASI_LEMAK = new FoodBuilder().withName(new Name("Nasi Alamak"))
            .withProtein("432").withFat("321")
            .withCarb("123").withDate("2 Sep 2020")
            .withTags("dinner", "lunch").build();
    public static final Food CRISPY_FRIED_FISH = new FoodBuilder().withName(new Name("Crispy Fried Fish"))
            .withProtein("563").withFat("456").withCarb("654")
            .withDate("13 Oct 2020").build();
    public static final Food DANISH_COOKIES = new FoodBuilder().withName(new Name("Danish Cookies"))
            .withProtein("533").withFat("654")
            .withCarb("456").withDate("1 Jan 2020")
            .withTags("lunch").build();
    public static final Food EGGS = new FoodBuilder().withName(new Name("Eggs"))
            .withProtein("224").withFat("246").withCarb("810")
            .withDate("7 Mar 2019").build();
    public static final Food FRUIT_CAKE = new FoodBuilder().withName(new Name("Fruit Cake"))
            .withProtein("427").withFat("987").withCarb("789")
            .withDate("3 Feb 1998").build();
    public static final Food GINGERBREAD = new FoodBuilder().withName(new Name("Gingerbread"))
            .withProtein("442").withFat("789").withCarb("987")
            .withDate("5 May 1973").build();

    // Manually added
    public static final Food HOT_PLATE = new FoodBuilder().withName(new Name("Hot Plate")).withProtein("424")
            .withFat("234").withCarb("234").build();
    public static final Food INDOMEE = new FoodBuilder().withName(new Name("Indomee")).withProtein("131")
            .withFat("234").withCarb("234").build();

    // Manually added - Food's details found in {@code CommandTestUtil}
    public static final Food APPLE = new FoodBuilder().withName(new Name("Apple")).withProtein("888")
            .withFat("234").withCarb("234").build();
    public static final Food BEANS = new FoodBuilder().withName(new Name("beans")).withProtein("888")
            .withFat("234").withCarb("234").build();

    public static final String KEYWORD_MATCHING_RICE = "Rice"; // A keyword that matches RICE

    private TypicalFoods() {
    } // prevents instantiation

    /**
     * Returns an {@code McGymmy} with all the typical foods.
     */
    public static McGymmy getTypicalMcGymmy() {
        McGymmy mg = new McGymmy();

        for (Food food : getTypicalFoodItems()) {
            mg.addFood(food);
        }
        return mg;
    }

    /**
     * Returns an {@code McGymmy} with each of the typical food duplicated once.
     */
    public static McGymmy getTypicalDuplicateMcGymmy() {
        McGymmy mg = new McGymmy();
        // each item is duplicated once
        for (Food food : getTypicalFoodItems()) {
            mg.addFood(food);
        }
        for (Food food : getTypicalFoodItems()) {
            mg.addFood(food);
        }
        return mg;
    }

    public static List<Food> getTypicalFoodItems() {
        return new ArrayList<>(Arrays.asList(CHICKEN_RICE, NASI_LEMAK, CRISPY_FRIED_FISH, DANISH_COOKIES, EGGS,
                FRUIT_CAKE, GINGERBREAD));
    }
}
