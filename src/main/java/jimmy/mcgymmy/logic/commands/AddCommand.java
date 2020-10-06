package jimmy.mcgymmy.logic.commands;

import static java.util.Objects.requireNonNull;

import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.ParserUtil;
import jimmy.mcgymmy.logic.parser.parameter.OptionalParameter;
import jimmy.mcgymmy.logic.parser.parameter.Parameter;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.food.Carbohydrate;
import jimmy.mcgymmy.model.food.Fat;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.model.food.Protein;

/**
 * Adds a food to the address book.
 */
public class AddCommand extends Command {
    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_SUCCESS = "New food added: %1$s";
    public static final String MESSAGE_DUPLICATE_FOOD = "This food already exists in the address book";

    private Parameter<Name> nameParameter = this.addParameter(
            "name",
            "n",
            "Name of food to add",
            "Chicken Rice",
            ParserUtil::parseName
    );
    private OptionalParameter<Protein> proteinParameter = this.addOptionalParameter(
            "protein value",
            "p",
            "Protein value of food (g)",
            "10",
            ParserUtil::parseProtein
    );
    private OptionalParameter<Fat> fatParameter = this.addOptionalParameter(
            "fat value",
            "f",
            "Fat value of food (g)",
            "10",
            ParserUtil::parseFat
    );
    private OptionalParameter<Carbohydrate> carbParameter = this.addOptionalParameter(
            "carb value",
            "c",
            "Carbohydrate value of food (g)",
            "10",
            ParserUtil::parseCarb
    );

    void setParameters(Parameter<Name> nameParameter, OptionalParameter<Protein> proteinParameter,
                       OptionalParameter<Fat> fatParameter, OptionalParameter<Carbohydrate> carbParameter) {
        this.nameParameter = nameParameter;
        this.proteinParameter = proteinParameter;
        this.fatParameter = fatParameter;
        this.carbParameter = carbParameter;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        // rewriting this class as an example, address and tags not implemented.
        Name newName = nameParameter.consume();
        Protein newProtein = this.proteinParameter.getValue().orElse(new Protein(0));
        Fat newFat = this.fatParameter.getValue().orElse(new Fat(0));
        Carbohydrate newCarb = this.carbParameter.getValue().orElse(new Carbohydrate(0));
        Food toAdd = new Food(newName, newProtein, newFat, newCarb);

        if (model.hasFood(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_FOOD);
        }

        model.addFood(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}