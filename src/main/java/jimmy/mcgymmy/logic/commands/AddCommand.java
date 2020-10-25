package jimmy.mcgymmy.logic.commands;

import static java.util.Objects.requireNonNull;

import jimmy.mcgymmy.logic.parser.ParserUtil;
import jimmy.mcgymmy.logic.parser.parameter.OptionalParameter;
import jimmy.mcgymmy.logic.parser.parameter.Parameter;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.date.Date;
import jimmy.mcgymmy.model.food.Carbohydrate;
import jimmy.mcgymmy.model.food.Fat;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.model.food.Protein;
import jimmy.mcgymmy.model.tag.Tag;

/**
 * Adds a food to mcgymmy.
 */
public class AddCommand extends Command {
    public static final String COMMAND_WORD = "add";
    public static final String SHORT_DESCRIPTION = "Add a new food item to McGymmy.";
    public static final String MESSAGE_SUCCESS = "New food added: \n%1$s";

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
    private OptionalParameter<Date> dateParameter = this.addOptionalParameter(
            "date",
            "d",
            "Date on which the food is consumed",
            "20-04-2020",
            ParserUtil::parseDate
    );
    private OptionalParameter<Tag> tagParameter = this.addOptionalParameter(
            "tag",
            "t",
            "Tag associated with the Food",
            "Lunch",
            ParserUtil::parseTag
    );

    void setParameters(Parameter<Name> nameParameter, OptionalParameter<Protein> proteinParameter,
                       OptionalParameter<Fat> fatParameter, OptionalParameter<Carbohydrate> carbParameter,
                       OptionalParameter<Tag> tagParameter, OptionalParameter<Date> dateParameter) {
        this.nameParameter = nameParameter;
        this.proteinParameter = proteinParameter;
        this.fatParameter = fatParameter;
        this.carbParameter = carbParameter;
        this.tagParameter = tagParameter;
        this.dateParameter = dateParameter;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        // rewriting this class as an example, tags not implemented.
        Name newName = nameParameter.consume();
        Protein newProtein = this.proteinParameter.getValue().orElse(new Protein(0));
        Fat newFat = this.fatParameter.getValue().orElse(new Fat(0));
        Carbohydrate newCarb = this.carbParameter.getValue().orElse(new Carbohydrate(0));
        Date newDate = this.dateParameter.getValue().orElse(Date.currentDate());
        Food toAdd = new Food(newName, newProtein, newFat, newCarb, newDate);

        if (this.tagParameter.getValue().isPresent()) {
            Tag newTag = this.tagParameter.getValue().get();
            toAdd = toAdd.addTag(newTag);
        }

        model.addFood(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}
