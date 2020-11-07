package jimmy.mcgymmy.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Name;

/**
 * A utility class containing a list of {@code Food} objects to be used in tests.
 */
public class TypicalFoods {

    public static final String KEYWORD_MATCHING_RICE = "Rice"; // A keyword that matches RICE

    private static Food chickenRice;
    private static Food nasiLemak;
    private static Food crispyFriedFish;
    private static Food danishCookies;
    private static Food eggs;
    private static Food fruitCake;
    private static Food gingerbread;

    // Manually added
    private static Food hotPlate;
    private static Food indomee;

    // Manually added - Food's details found in {@code CommandTestUtil}
    private static Food apple;
    private static Food beans;

    static {
        try {
            generateTypicalFoods();
        } catch (IllegalValueException e) {
            assert false : "Error in Typical foods";
        }
    }

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

    private static void generateTypicalFoods() throws IllegalValueException {
        //Generate all of the foods
        chickenRice = new FoodBuilder().withName(new Name("Chicken Rice"))
                .withProtein("253").withFat("123")
                .withCarb("456").withDate("20 Apr 2020")
                .withTags("lunch").build();
        crispyFriedFish = new FoodBuilder().withName(new Name("Crispy Fried Fish"))
                .withProtein("563").withFat("456").withCarb("654")
                .withDate("13 Oct 2020").build();
        nasiLemak = new FoodBuilder().withName(new Name("Nasi Alamak"))
                .withProtein("432").withFat("321")
                .withCarb("123").withDate("2 Sep 2020")
                .withTags("dinner", "lunch").build();
        danishCookies = new FoodBuilder().withName(new Name("Danish Cookies"))
                .withProtein("533").withFat("654")
                .withCarb("456").withDate("1 Jan 2020")
                .withTags("lunch").build();
        eggs = new FoodBuilder().withName(new Name("Eggs"))
                .withProtein("224").withFat("246").withCarb("810")
                .withDate("7 Mar 2019").build();
        fruitCake = new FoodBuilder().withName(new Name("Fruit Cake"))
                .withProtein("427").withFat("987").withCarb("789")
                .withDate("3 Feb 1998").build();
        gingerbread = new FoodBuilder().withName(new Name("Gingerbread"))
                .withProtein("442").withFat("789").withCarb("987")
                .withDate("5 May 1973").build();
        hotPlate = new FoodBuilder().withName(new Name("Hot Plate")).withProtein("424")
                .withFat("234").withCarb("234").build();
        indomee = new FoodBuilder().withName(new Name("Indomee")).withProtein("131")
                .withFat("234").withCarb("234").build();
        apple = new FoodBuilder().withName(new Name("Apple")).withProtein("888")
                .withFat("234").withCarb("234").build();
        beans = new FoodBuilder().withName(new Name("beans")).withProtein("888")
                .withFat("234").withCarb("234").build();
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
        return new ArrayList<>(Arrays.asList(chickenRice, nasiLemak, crispyFriedFish, danishCookies, eggs,
                fruitCake, gingerbread));
    }

    public static Food getChickenRice() {
        return chickenRice;
    }

    public static Food getNasiLemak() {
        return nasiLemak;
    }

    public static Food getCrispyFriedFish() {
        return crispyFriedFish;
    }

    public static Food getDanishCookies() {
        return danishCookies;
    }

    public static Food getEggs() {
        return eggs;
    }

    public static Food getFruitCake() {
        return fruitCake;
    }

    public static Food getGingerbread() {
        return gingerbread;
    }

    public static Food getHotPlate() {
        return hotPlate;
    }

    public static Food getIndomee() {
        return indomee;
    }

    public static Food getApple() {
        return apple;
    }

    public static Food getBeans() {
        return beans;
    }
}
