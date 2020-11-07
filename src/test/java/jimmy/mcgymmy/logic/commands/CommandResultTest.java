package jimmy.mcgymmy.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class CommandResultTest {

    private static final Float DIFFERENT_TYPE = 0.5f;
    private static final boolean DEFAULT_EXIT = false;
    private static final boolean DIFFERENT_EXIT = true;
    private static final String TEST_FEEDBACK = "feedback";
    private static final String DIFFERENT_TEST_FEEDBACK = "different";
    private static final CommandResult COMMAND_RESULT = new CommandResult(TEST_FEEDBACK);
    private static final CommandResult COMMAND_RESULT_2 = new CommandResult(TEST_FEEDBACK);
    private static final CommandResult COMMAND_RESULT_3 = new CommandResult(TEST_FEEDBACK, DEFAULT_EXIT);
    private static final CommandResult DIFFERENT_EXIT_CODE = new CommandResult(TEST_FEEDBACK, DIFFERENT_EXIT);
    private static final CommandResult DIFFERENT_COMMAND_RESULT = new CommandResult(DIFFERENT_TEST_FEEDBACK);

    @Test
    public void getFeedbackToUserTest() {

        // same object -> Same feedback message
        assertEquals(COMMAND_RESULT.getFeedbackToUser(), COMMAND_RESULT.getFeedbackToUser());

        // different object but Same feedback message (Regardless of exit code and constructor)
        assertEquals(COMMAND_RESULT.getFeedbackToUser(), TEST_FEEDBACK);
        assertEquals(DIFFERENT_COMMAND_RESULT.getFeedbackToUser(), DIFFERENT_TEST_FEEDBACK);
        assertEquals(COMMAND_RESULT.getFeedbackToUser(), COMMAND_RESULT_2.getFeedbackToUser());
        assertEquals(COMMAND_RESULT.getFeedbackToUser(), COMMAND_RESULT_3.getFeedbackToUser());
        assertEquals(COMMAND_RESULT.getFeedbackToUser(), DIFFERENT_EXIT_CODE.getFeedbackToUser());
        assertEquals(DIFFERENT_COMMAND_RESULT.getFeedbackToUser(), DIFFERENT_COMMAND_RESULT.getFeedbackToUser());

        // different object and different feedback message
        assertNotEquals(COMMAND_RESULT.getFeedbackToUser(), DIFFERENT_TEST_FEEDBACK);
        assertNotEquals(COMMAND_RESULT.getFeedbackToUser(), DIFFERENT_COMMAND_RESULT.getFeedbackToUser());
    }

    @Test
    public void isMessageTest() {
        // same object -> same exit code
        assertEquals(COMMAND_RESULT.isExit(), COMMAND_RESULT.isExit());

        // different object but Same exit code (Regardless of exit code and constructor)
        assertEquals(COMMAND_RESULT.isExit(), DEFAULT_EXIT);
        assertEquals(DIFFERENT_EXIT_CODE.isExit(), DIFFERENT_EXIT);
        assertEquals(COMMAND_RESULT.isExit(), COMMAND_RESULT_2.isExit());
        assertEquals(COMMAND_RESULT.isExit(), COMMAND_RESULT_3.isExit());
        assertEquals(COMMAND_RESULT.isExit(), DIFFERENT_COMMAND_RESULT.isExit());

        // different object different exit code
        assertNotEquals(COMMAND_RESULT.isExit(), DIFFERENT_EXIT);
        assertNotEquals(DIFFERENT_EXIT_CODE.isExit(), DEFAULT_EXIT);
        assertNotEquals(COMMAND_RESULT.isExit(), DIFFERENT_EXIT_CODE.isExit());
    }

    @Test
    public void equals() {

        // same values -> returns true
        assertEquals(COMMAND_RESULT_2, COMMAND_RESULT);
        assertEquals(COMMAND_RESULT_3, COMMAND_RESULT);

        // same object -> returns true
        assertEquals(COMMAND_RESULT, COMMAND_RESULT);

        // null -> returns false
        assertNotEquals(COMMAND_RESULT, null);

        // different types -> returns false
        assertNotEquals(COMMAND_RESULT, DIFFERENT_TYPE);

        // different feedbackToUser value -> returns false
        assertNotEquals(DIFFERENT_COMMAND_RESULT, COMMAND_RESULT);

        // different exit value -> returns false
        assertNotEquals(DIFFERENT_EXIT_CODE, COMMAND_RESULT);
    }

    @Test
    public void hashcode() {

        // same object -> same hashcode
        assertEquals(COMMAND_RESULT.hashCode(), COMMAND_RESULT.hashCode());

        // same values -> returns same hashcode
        assertEquals(COMMAND_RESULT.hashCode(), COMMAND_RESULT_2.hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(COMMAND_RESULT.hashCode(), DIFFERENT_COMMAND_RESULT.hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(COMMAND_RESULT.hashCode(), DIFFERENT_EXIT_CODE.hashCode());
    }
}
