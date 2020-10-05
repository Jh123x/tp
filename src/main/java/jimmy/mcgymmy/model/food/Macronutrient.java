package jimmy.mcgymmy.model.food;

import jimmy.mcgymmy.commons.util.AppUtil;
import jimmy.mcgymmy.commons.util.CollectionUtil;

public abstract class Macronutrient {
    private static final String VALIDATION_REGEX = "(\\d)+";
    public static String MESSAGE_CONSTRAINTS = "values should only contain numbers";
    private int amount;
    private int caloricMultiplier;
    private int totalCalories;

    /**
     * Represents macronutrients of 3 types
     *
     * @param amount            The amount of the macronutrient
     * @param caloricMultiplier This value varies for each macronutrient type
     */
    public Macronutrient(int amount, int caloricMultiplier) {
        CollectionUtil.requireAllNonNull(amount, caloricMultiplier);

        // use this instead of assert because the amount < 0 error is more because of user input than developer's fault
        AppUtil.checkArgument(isValidAmount(amount), getMessageContraint());

        assert (caloricMultiplier == 4 || caloricMultiplier == 9) : "Invalid Macronutrient Multiplier";
        // initialise variables
        this.amount = amount;
        this.caloricMultiplier = caloricMultiplier;
        this.totalCalories = caloricMultiplier * amount;

    }

    private boolean isValidAmount(int amount) {
        return amount > 0;
    }

    private String getMessageContraint() {
        return this.getMacronutrientType() + " amount can only take in value larger than 0";
    }

    @Override
    public String toString() {
        return "MacronutrientType:" + this.getMacronutrientType() + "\n"
            + "Amount: "
            + this.getAmount() + "\n"
            + "Caloric Count: " + this.getTotalCalories() + "\n";
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
            && this.getAmount() == otherMacronutrient.getAmount()
            && this.getCaloricMultiplier() == otherMacronutrient.getCaloricMultiplier();
    }

    // take the type from the class name
    public String getMacronutrientType() {
        return this.getClass().getName();
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

    public static boolean isValid(String value) {
        return value.matches(VALIDATION_REGEX);
    }
}