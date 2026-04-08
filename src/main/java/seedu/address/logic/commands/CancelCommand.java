package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Cancels a pending delete operation.
 */
public class CancelCommand extends Command {

    public static final String COMMAND_WORD = "no";

    public static final String MESSAGE_CANCEL_SUCCESS =
            "Delete operation cancelled.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException("Cancel command should be handled through confirmation flow.");
    }
}
