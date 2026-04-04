package seedu.address.logic;

import seedu.address.logic.commands.Command;

/**
 * Stores the current command awaiting user confirmation.
 */
public class ConfirmationManager {

    private Command pendingCommand;

    /**
     * Returns true if there is a pending command awaiting confirmation.
     */
    public boolean hasPendingCommand() {
        return pendingCommand != null;
    }

    /**
     * Stores the command awaiting confirmation.
     */
    public void setPendingCommand(Command pendingCommand) {
        this.pendingCommand = pendingCommand;
    }

    /**
     * Returns the pending command.
     */
    public Command getPendingCommand() {
        return pendingCommand;
    }

    /**
     * Clears the pending command.
     */
    public void clearPendingCommand() {
        this.pendingCommand = null;
    }
}
