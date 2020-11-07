package jimmy.mcgymmy.model.food;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

/**
 * Represents Protein in food item in McGymmy.
 */
public class Protein extends Macronutrient {
    private static final int PROTEIN_MULTIPLIER = 4;
    private static final int DEFAULT_PROTEIN = 0;
    public static final String MESSAGE_CONSTRAINTS = "Protein" + Macronutrient.MESSAGE_CONSTRAINTS;

    /**
     * @param amount A valid amount.
     */
    public Protein(int amount) throws IllegalValueException {
        super(amount, PROTEIN_MULTIPLIER);
    }

    /**
     * Create a new Protein using the default value.
     * Mostly boilerplate code.
     * @return new Protein with default value.
     */
    public static Protein newDefault() {
        try {
            return new Protein(DEFAULT_PROTEIN);
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
