package jimmy.mcgymmy.model.food;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

/**
 * Represents Carbodydrates in food item in McGymmy.
 */
public class Carbohydrate extends Macronutrient {
    private static final int CARBOHYDRATE_MULTIPLIER = 4;
    private static final int DEFAULT_CARBOHYDRATE = 0;
    public static final String MESSAGE_CONSTRAINTS = "Carbohydrate" + Macronutrient.MESSAGE_CONSTRAINTS;

    /**
     * @param amount A valid amount.
     */
    public Carbohydrate(int amount) throws IllegalValueException {
        super(amount, CARBOHYDRATE_MULTIPLIER);
    }

    /**
     * Create a new Carbohydrate using the default value.
     * Mostly boilerplate code.
     * @return new Carbohydrate with default value.
     */
    public static Carbohydrate newDefault() {
        try {
            return new Carbohydrate(DEFAULT_CARBOHYDRATE);
        } catch (IllegalValueException e) {
            assert false : "Error in creation of default";
            throw new IllegalArgumentException("Error in creation using default values.");
        }
    }

    @Override
    String getMessageConstraint() {
        return MESSAGE_CONSTRAINTS;
    }
}
