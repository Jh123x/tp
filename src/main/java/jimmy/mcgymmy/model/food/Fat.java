package jimmy.mcgymmy.model.food;


import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

/**
 * Represents Fats in food item in McGymmy.
 */
public class Fat extends Macronutrient {
    private static final int FAT_MULTIPLIER = 9;
    private static final int DEFAULT_FAT = 0;
    public static final String MESSAGE_CONSTRAINTS = "Fat" + Macronutrient.MESSAGE_CONSTRAINTS;

    /**
     * Creates a new Fat.
     *
     * @param amount A valid amount.
     */
    public Fat(int amount) throws IllegalValueException {
        super(amount, FAT_MULTIPLIER);
    }

    /**
     * Creates a new Fat using the default value.
     * Mostly boilerplate code.
     * @return new Fat with default value.
     */
    public static Fat newDefault() {
        try {
            return new Fat(DEFAULT_FAT);
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
