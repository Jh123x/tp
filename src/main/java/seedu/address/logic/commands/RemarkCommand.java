package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * Changes the remark of an existing person in the address book.
 */
public class RemarkCommand extends Command {
    public static final String COMMAND_WORD = "remark";
    public static final String MESSAGE_REMARK_PERSON_SUCCESS = "Remarked %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the remark of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "r/ [REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "r/ Likes to swim.";

    public final Remark remark;
    public final Index index;

    /**
     * Creates an RemarkCommand to add the specified {@code Person}
     */
    public RemarkCommand(Index index, Remark remark) {
        requireAllNonNull(index, remark);
        this.index = index;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToRemark = lastShownList.get(index.getZeroBased());
        Person remarkedPerson = createRemarkedPerson(personToRemark, remark);
        model.setPerson(personToRemark, remarkedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_REMARK_PERSON_SUCCESS, remarkedPerson));

    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToRemark}
     */
    private static Person createRemarkedPerson(Person personToRemark, Remark remark) {
        assert personToRemark != null;
        return new Person(personToRemark.getName(), personToRemark.getPhone(), personToRemark.getEmail(),
                personToRemark.getAddress(), personToRemark.getTags(), remark);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        // state check
        RemarkCommand e = (RemarkCommand) other;
        return index.equals(e.index)
                && remark.equals(e.remark);
    }
}
