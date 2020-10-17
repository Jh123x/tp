package jimmy.mcgymmy.model.food;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jimmy.mcgymmy.commons.util.AppUtil;
import jimmy.mcgymmy.commons.util.CollectionUtil;
import jimmy.mcgymmy.model.date.Date;
import jimmy.mcgymmy.model.tag.Tag;


/**
 * Represents a Food item in McGymmy.
 */
public class Food {
    public static final String FOOD_NAME_MESSAGE_CONSTRAINT = "Food name can take in any value, and it cannot be blank";

    /*
     * The first character of name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    // Identity field names
    private final Name name;
    private final Protein protein;
    private final Carbohydrate carbs;
    private final Fat fat;
    private final Date date;
    private final Set<Tag> tags = new HashSet<>();


    /**
     * Every field must be present and not null.
     */
    public Food(Name name, Protein protein, Fat fat, Carbohydrate carbs, Set<Tag> tags, Date date) {
        CollectionUtil.requireAllNonNull(name, protein, carbs, fat, date);
        AppUtil.checkArgument(isValidName(name.toString()), FOOD_NAME_MESSAGE_CONSTRAINT);
        this.name = name;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.tags.addAll(tags);
        this.date = date;
    }

    // Constructor for convenience

    /**
     * Every field must be present and not null.
     * A Constructor made for convenience
     */

    public Food(String name, int proteinAmount, int fatAmount, int carbsAmount) {
        this(new Name(name), new Protein(proteinAmount), new Fat(fatAmount), new Carbohydrate(carbsAmount));
    }

    /**
     * Every field must be present and not null.
     */
    public Food(Name name, Protein protein, Fat fat, Carbohydrate carbs) {
        this(name, protein, fat, carbs, new HashSet<Tag>());
    }

    /**
     * Every field must be present and not null.
     */
    public Food(Name name, Protein protein, Fat fat, Carbohydrate carbs, Date date) {
        this(name, protein, fat, carbs, new HashSet<Tag>(), date);
    }

    /**
     * Every field must be present and not null.
     */
    public Food(Name name, Protein protein, Fat fat, Carbohydrate carbs, Set<Tag> tags) {
        this(name, protein, fat, carbs, tags, Date.currentDate());
    }

    private boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * @return total caloric content of food item
     */
    public int getCalories() {
        return this.getProtein().getTotalCalories()
                + this.getCarbs().getTotalCalories()
                + this.getFat().getTotalCalories();
    }

    public Name getName() {
        return this.name;
    }

    public Protein getProtein() {
        return this.protein;
    }

    public Carbohydrate getCarbs() {
        return this.carbs;
    }

    public Fat getFat() {
        return this.fat;
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        CollectionUtil.requireAllNonNull(protein, carbs, fat);

        if (other == this) {
            return true;
        }

        if (!(other instanceof Food)) {
            return false;
        }

        Food otherFood = (Food) other;

        return otherFood != null
                && this.getName().equals(otherFood.getName())
                && this.getProtein().equals(otherFood.getProtein())
                && this.getCarbs().equals(otherFood.getCarbs())
                && this.getFat().equals(otherFood.getFat())
                && this.getDate().equals(otherFood.getDate());
    }

    // Displays
    // name + PCF details + total calories
    @Override
    public String toString() {
        return "Food:" + this.getName() + "\n"
                + "protein: " + protein.getAmount() + "\n"
                + "carbs: " + carbs.getAmount() + "\n"
                + "fat: " + fat.getAmount() + "\n";
    }

}
