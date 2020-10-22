package jimmy.mcgymmy.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.logic.macro.Macro;
import jimmy.mcgymmy.logic.macro.MacroList;
import jimmy.mcgymmy.logic.macro.exceptions.DuplicateMacroException;

/**
 * An Immutable MacroList that is serializable to JSON format.
 */
@JsonRootName(value = "macrolist")
class JsonSerializableMacroList {

    private final List<JsonAdaptedMacro> macros = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableMacroList} with the given food.
     */
    @JsonCreator
    public JsonSerializableMacroList(@JsonProperty("macros") List<JsonAdaptedMacro> macros) {
        this.macros.addAll(macros);
    }

    /**
     * Converts a given {@code MacroList} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableMcGymmy}.
     */
    public JsonSerializableMacroList(MacroList source) {
        macros.addAll(source.getAsList().stream().map(JsonAdaptedMacro::new).collect(Collectors.toList()));
    }

    /**
     * Converts this MacroList into the model's {@code MacroList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public MacroList toModelType() throws IllegalValueException {
        MacroList macroList = new MacroList();
        for (JsonAdaptedMacro jsonAdaptedMacro : macros) {
            try {
                Macro macro = jsonAdaptedMacro.toMacro();
                macroList.addMacro(macro);
            } catch (DuplicateMacroException e) {
                throw new IllegalValueException(e.getMessage());
            }
        }
        return macroList;
    }

}
