package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.ConfirmationManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Confirms and executes the currently pending command.
 */
public class ConfirmCommand extends Command {

    public static final String COMMAND_WORD = "yes";

    public static final String MESSAGE_NO_PENDING_CONFIRMATION =
            "There is no pending action to confirm.";

    private final ConfirmationManager confirmationManager;

    /**
     * creates a confirm command
     * @param confirmationManager
     */
    public ConfirmCommand(ConfirmationManager confirmationManager) {
        requireNonNull(confirmationManager);
        this.confirmationManager = confirmationManager;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!confirmationManager.hasPendingCommand()) {
            throw new CommandException(MESSAGE_NO_PENDING_CONFIRMATION);
        }

        Command commandToExecute = confirmationManager.getPendingCommand();
        confirmationManager.clearPendingCommand();
        return commandToExecute.execute(model);
    }
}
