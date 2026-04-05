package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Views details of a student in the address book.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views the detailed attendance remarks of the student identified by the index number used in the "
            + "displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_VIEW_PERSON_SUCCESS = "Viewing student: %1$s";

    private static final Logger logger = LogsCenter.getLogger(ViewCommand.class);

    private final Index targetIndex;

    /**
     * Creates a ViewCommand to view the person at the specified {@code targetIndex}.
     *
     * @param targetIndex index of the person in the filtered person list to view
     */
    public ViewCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        int zeroBasedIndex = targetIndex.getZeroBased();

        if (zeroBasedIndex >= lastShownList.size()) {
            logger.warning(() -> String.format(
                    "ViewCommand failed: index %d is out of bounds. Filtered list size: %d",
                    targetIndex.getOneBased(), lastShownList.size()));
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToView = lastShownList.get(zeroBasedIndex);
        assert personToView != null : "The person to view should not be null";

        logger.fine(() -> String.format(
                "Viewing student at index %d: %s",
                targetIndex.getOneBased(), personToView.getName()));

        return new CommandResult(
                String.format(MESSAGE_VIEW_PERSON_SUCCESS, Messages.format(personToView)),
                personToView);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ViewCommand otherViewCommand)) {
            return false;
        }

        return targetIndex.equals(otherViewCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
