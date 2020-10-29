package jimmy.mcgymmy.testutil;

import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.food.Food;

/**
 * A utility class to help with building McGymmy objects.
 * Example usage: <br>
 * {@code McGymmy ab = new McGymmyBuilder().withFood("Chicken Rice").build();}
 */
public class McGymmyBuilder {

    private final McGymmy mcGymmy;

    public McGymmyBuilder() {
        mcGymmy = new McGymmy();
    }

    public McGymmyBuilder(McGymmy mcGymmy) {
        this.mcGymmy = mcGymmy;
    }

    /**
     * Adds a new {@code Food} to the {@code McGymmy} that we are building.
     */
    public McGymmyBuilder withFood(Food food) {
        mcGymmy.addFood(food);
        return this;
    }

    public McGymmy build() {
        return mcGymmy;
    }
}
