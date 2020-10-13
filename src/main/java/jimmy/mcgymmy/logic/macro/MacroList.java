package jimmy.mcgymmy.logic.macro;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jimmy.mcgymmy.logic.macro.exceptions.DuplicateMacroException;

/**
 * TODO: 1. save list in file/serialize? 2. add default shortcut macros.
 * Container for macros. Ensures a macro's name is not taken before
 * allowing it to be added.
 */
public class MacroList {
    private final Map<String, Macro> macros;
    private final Set<String> commandNames;

    /**
     * creates a new MacroList
     * @param commandNames set of command names that have been taken.
     */
    public MacroList(Set<String> commandNames) {
        this.macros = new HashMap<>();
        this.commandNames = commandNames;
    }

    public boolean hasMacro(String name) {
        return this.macros.containsKey(name);
    }

    private boolean isMacroNameTaken(String name) {
        return this.hasMacro(name) || commandNames.contains(name);
    }

    /**
     * Adds a macro to the MacroList silently (no ui interaction).
     * @param newMacro the macro to add.
     * @throws DuplicateMacroException if the macro's name has already been taken.
     */
    public void addMacro(Macro newMacro) throws DuplicateMacroException {
        if (this.isMacroNameTaken(newMacro.getName())) {
            throw new DuplicateMacroException();
        }
        this.macros.put(newMacro.getName(), newMacro);
    }

    public Macro getMacro(String name) {
        return this.macros.get(name);
    }
}
