package jimmy.mcgymmy.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.model.ModelManager;

/**
 * Contains unit tests for UndoCommand.
 */
public class UndoCommandTest {
    private class CanUndoModelStub extends ModelManager {
        private boolean hasBeenUndoed = false;
        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            hasBeenUndoed = true;
        }

        public boolean isUndoed() {
            return hasBeenUndoed;
        }
    }

    private class CannotUndoModelStub extends ModelManager {
        @Override
        public boolean canUndo() {
            return false;
        }
    }

    @Test
    public void execute_canUndo_returnsUndoSuccessfulMessage() {
        UndoCommand command = new UndoCommand();
        CanUndoModelStub model = new CanUndoModelStub();
        CommandResult commandResult = command.execute(model);
        assertEquals(new CommandResult(UndoCommand.MESSAGE_UNDO_SUCCESS), commandResult);
        assertTrue(model.isUndoed());
    }

    @Test
    public void execute_cannotUndo_returnsCannotUndoAnyMoreMessage() {
        UndoCommand command = new UndoCommand();
        CommandResult commandResult = command.execute(new CannotUndoModelStub());
        assertEquals(new CommandResult(UndoCommand.MESSAGE_NOT_UNDOABLE), commandResult);
    }
}
