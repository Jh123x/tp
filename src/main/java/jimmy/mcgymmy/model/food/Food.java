package jimmy.mcgymmy.model.food;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import jimmy.mcgymmy.commons.core.LogsCenter;
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

    private static final Logger logger = LogsCenter.getLogger(Food.class);

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
        // logging
        logger.finer("Creating food item: ");
        logger.finer("With name: " + name.toString());
        logger.finer("With protein: " + protein.toString());
        logger.finer("With fat: " + fat.toString());
        logger.finer("With carbs: " + carbs.toString());
        logger.finer("With date: " + date.toString());
        logger.finer("With tag(s): ");
        for (Tag tag : tags) {
            logger.finer(tag.toString());
        }
        logger.finer("==============[Create food done]==============");
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

    /**
     * Adds a new tag to food
     * @return A new Food with the tag
     */
    public Food addTag(Tag tag) {
        Set<Tag> newTags = new HashSet<>(tags);
        newTags.add(tag);
        return new Food(name, protein, fat, carbs, newTags, date);
    }

    /**
     * Removes a tag from food
     * @return A new Food without the tag
     */
    public Food removeTag(Tag tag) {
        Set<Tag> newTags = new HashSet<>(tags);
        newTags.remove(tag);
        return new Food(name, protein, fat, carbs, newTags, date);
    }

    /**
     * Check if this food is already tagged with this tag
     * @return True if this food is already tagged with this tag
     */
    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
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
        return "Food: " + this.getName() + "\n"
                + "Protein: " + protein.getAmount() + "\n"
                + "Carbs: " + carbs.getAmount() + "\n"
                + "Fat: " + fat.getAmount() + "\n";
    }

}
