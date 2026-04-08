package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Confirms the currently pending command.
 */
public class ConfirmCommand extends Command {

    public static final String COMMAND_WORD = "yes";

    public static final String MESSAGE_NO_PENDING_CONFIRMATION =
            "There is no pending action to confirm.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException("ConfirmCommand should be handled by LogicManager.");
    }
}
