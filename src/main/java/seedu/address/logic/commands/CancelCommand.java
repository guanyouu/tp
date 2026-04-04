package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.ConfirmationManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Cancels the currently pending command.
 */
public class CancelCommand extends Command {

    public static final String COMMAND_WORD = "no";

    public static final String MESSAGE_NO_PENDING_CONFIRMATION =
            "There is no pending action to cancel.";

    public static final String MESSAGE_CANCEL_SUCCESS =
            "Delete operation cancelled.";

    private final ConfirmationManager confirmationManager;

    /**
     * Creates a cancel command
     * @param confirmationManager
     */
    public CancelCommand(ConfirmationManager confirmationManager) {
        requireNonNull(confirmationManager);
        this.confirmationManager = confirmationManager;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!confirmationManager.hasPendingCommand()) {
            throw new CommandException(MESSAGE_NO_PENDING_CONFIRMATION);
        }

        confirmationManager.clearPendingCommand();
        return new CommandResult(MESSAGE_CANCEL_SUCCESS);
    }
}
