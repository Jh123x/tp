package jimmy.mcgymmy.model.macro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jimmy.mcgymmy.logic.parser.PrimitiveCommandParser;
import jimmy.mcgymmy.model.macro.exceptions.DuplicateMacroException;

/**
 * Immutable container for macros. Ensures a macro's name is not taken before
 * allowing it to be added.
 */
public class MacroList {
    private final Map<String, Macro> macros;
    private final Set<String> commandNames;

    /**
     * Creates a new MacroList
     */
    public MacroList() {
        this.macros = new HashMap<>();
        this.commandNames = new HashSet<>(PrimitiveCommandParser.getRegisteredCommands());
        this.commandNames.add("macro");
    }

    /**
     * Creates a new MacroList with pre-populated values.
     *
     * @param macros list of macros to populate this with.
     * @throws DuplicateMacroException If there are any duplicate macros.
     */
    public MacroList(List<Macro> macros) throws DuplicateMacroException {
        this.macros = new HashMap<>();
        this.commandNames = new HashSet<>(PrimitiveCommandParser.getRegisteredCommands());
        this.commandNames.add("macro");
        for (Macro macro : macros) {
            if (this.isMacroNameTaken(macro.getName())) {
                throw new DuplicateMacroException();
            }
            this.macros.put(macro.getName(), macro);
        }
    }

    public boolean hasMacro(String name) {
        return this.macros.containsKey(name);
    }

    private boolean isMacroNameTaken(String name) {
        return this.hasMacro(name) || commandNames.contains(name);
    }

    /**
     * Returns a new MacroList with the new macro.
     *
     * @param newMacro the macro to add.
     * @throws DuplicateMacroException if the macro's name has already been taken.
     */
    public MacroList withNewMacro(Macro newMacro) throws DuplicateMacroException {
        List<Macro> newList = this.getAsList();
        newList.add(newMacro);
        return new MacroList(newList);
    }

    /**
     * Returns a new MacroList without the specified macro.
     * If the macro does not exist in the MacroList, returns a new copy of the same MacroList.
     *
     * @param macroName the macro to remove.
     */
    public MacroList withoutMacro(String macroName) {
        List<Macro> newList = this.getAsList()
                .stream()
                .filter(macro -> !macro.getName().equals(macroName))
                .collect(Collectors.toList());
        try {
            return new MacroList(newList);
        } catch (DuplicateMacroException e) {
            // this logically should never happen.
            assert false : "removing macro results in duplicate macro";
            throw new RuntimeException(e.getMessage());
        }
    }

    public Macro getMacro(String name) {
        return this.macros.get(name);
    }

    public List<Macro> getAsList() {
        return new ArrayList<>(this.macros.values());
    }

    // NOTE: not overriding Object.equals() because MacroList is immutable.
}
