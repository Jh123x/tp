package jimmy.mcgymmy.model.food;

import jimmy.mcgymmy.commons.util.AppUtil;
import jimmy.mcgymmy.commons.util.CollectionUtil;

public abstract class Macronutrient {
    public static final String MESSAGE_CONSTRAINTS = "values should only contain positive numbers";
    private static final String VALIDATION_REGEX = "(\\d){1,3}";
    private final int amount;
    private final int caloricMultiplier;
    private final int totalCalories;
    private String type;

    /**
     * Represents macronutrients of 3 types
     *
     * @param amount            The amount of the macronutrient
     * @param caloricMultiplier This value varies for each macronutrient type
     */
    public Macronutrient(int amount, int caloricMultiplier) {
        CollectionUtil.requireAllNonNull(amount, caloricMultiplier);

        // use this instead of assert because the amount < 0 error is more because of user input than developer's fault
        AppUtil.checkArgument(isValidAmount(amount), getMessageConstraint());

        assert (caloricMultiplier == 4 || caloricMultiplier == 9) : "Invalid Macronutrient Multiplier";
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
        return value.matches(VALIDATION_REGEX);
    }

    private boolean isValidAmount(int amount) {
        return amount >= 0 && amount < 1000;
    }

    private String getMessageConstraint() {
        return this.getMacronutrientType() + " amount can only take in value larger than 0 and less than 1000";
    }

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
