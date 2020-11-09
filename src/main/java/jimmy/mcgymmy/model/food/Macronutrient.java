package jimmy.mcgymmy.model.food;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.commons.util.AppUtil;
import jimmy.mcgymmy.commons.util.CollectionUtil;

/**
 * Represents a food item with hidden internal logic.
 */
public abstract class Macronutrient {

    private static final int LOWER_BOUND = 0; //Inclusive
    private static final int UPPER_BOUND = 999; //Non inclusive
    private static final String VALIDATION_REGEX = "(\\d){1,3}";

    protected static final String MESSAGE_CONSTRAINTS = String.format(
            " can only contain non-negative integers between %d and %d", LOWER_BOUND, UPPER_BOUND);

    private final int amount;
    private final int totalCalories;
    private final int caloricMultiplier;

    /**
     * Represents macronutrients of 3 types
     *
     * @param amount            The amount of the macronutrient
     * @param caloricMultiplier This value varies for each macronutrient type
     */
    public Macronutrient(int amount, int caloricMultiplier) throws IllegalValueException {
        CollectionUtil.requireAllNonNull(amount, caloricMultiplier);

        // use this instead of assert because the amount < 0 error is more because of user input than developer's fault
        AppUtil.checkArgument(isValidAmount(amount), getMessageConstraint());

        // add assertion for negative multiplier
        assert isValidMultiplier(caloricMultiplier) : "Caloric multiplier is negative";

        // initialise variables
        this.amount = amount;
        this.caloricMultiplier = caloricMultiplier;
        this.totalCalories = caloricMultiplier * amount;
    }

    /**
     * Check if the string is a Valid Macronutrient amount.
     *
     * @param value String value of Macronutrient.
     * @return if the String is valid.
     */
    public static boolean isValid(String value) {
        //Trim for defensive programming
        return value.trim().matches(VALIDATION_REGEX);
    }

    private boolean isValidAmount(int amount) {
        return amount >= LOWER_BOUND && amount <= UPPER_BOUND;
    }

    private boolean isValidMultiplier(int amount) {
        return amount > 0;
    }

    abstract String getMessageConstraint();

    @Override
    public String toString() {
        return this.getMacronutrientType() + ": " + this.getAmount() + "\n";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Macronutrient)) {
            return false;
        }
        Macronutrient otherMacronutrient = (Macronutrient) other;
        return this.getMacronutrientType().equals(otherMacronutrient.getMacronutrientType())
                && this.getCaloricMultiplier() == otherMacronutrient.getCaloricMultiplier()
                && this.getAmount() == otherMacronutrient.getAmount();
    }

    // take the type from the class name
    public String getMacronutrientType() {
        return this.getClass().getSimpleName();
    }

    public int getAmount() {
        return amount;
    }

    public int getCaloricMultiplier() {
        return caloricMultiplier;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

}
