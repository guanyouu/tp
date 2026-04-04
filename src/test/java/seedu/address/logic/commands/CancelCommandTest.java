package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.ConfirmationManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests and unit tests for {@code CancelCommand}.
 */
public class CancelCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_pendingCommandExists_success() {
        ConfirmationManager confirmationManager = new ConfirmationManager();
        confirmationManager.setPendingCommand(new ConfirmedDeleteCommand(ALICE));

        CancelCommand cancelCommand = new CancelCommand(confirmationManager);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(cancelCommand, model, CancelCommand.MESSAGE_CANCEL_SUCCESS, expectedModel);
        assertFalse(confirmationManager.hasPendingCommand());
    }

    @Test
    public void execute_noPendingCommand_throwsCommandException() {
        ConfirmationManager confirmationManager = new ConfirmationManager();
        CancelCommand cancelCommand = new CancelCommand(confirmationManager);

        assertCommandFailure(cancelCommand, model, CancelCommand.MESSAGE_NO_PENDING_CONFIRMATION);
        assertFalse(confirmationManager.hasPendingCommand());
    }

    @Test
    public void equals() {
        ConfirmationManager confirmationManager = new ConfirmationManager();
        CancelCommand cancelCommand = new CancelCommand(confirmationManager);

        // same object -> returns true
        assertTrue(cancelCommand.equals(cancelCommand));

        // different types -> returns false
        assertFalse(cancelCommand.equals(1));

        // null -> returns false
        assertFalse(cancelCommand.equals(null));
    }
}
