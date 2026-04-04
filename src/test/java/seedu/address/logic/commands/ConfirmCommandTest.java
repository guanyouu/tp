package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.ConfirmationManager;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests and unit tests for {@code ConfirmCommand}.
 */
public class ConfirmCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_pendingCommandExists_success() {
        ConfirmationManager confirmationManager = new ConfirmationManager();
        confirmationManager.setPendingCommand(new ConfirmedDeleteCommand(ALICE));

        ConfirmCommand confirmCommand = new ConfirmCommand(confirmationManager);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(ALICE));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(ALICE);

        assertCommandSuccess(confirmCommand, model, expectedMessage, expectedModel);
        assertFalse(confirmationManager.hasPendingCommand());
    }

    @Test
    public void execute_noPendingCommand_throwsCommandException() {
        ConfirmationManager confirmationManager = new ConfirmationManager();
        ConfirmCommand confirmCommand = new ConfirmCommand(confirmationManager);

        assertCommandFailure(confirmCommand, model, ConfirmCommand.MESSAGE_NO_PENDING_CONFIRMATION);
        assertFalse(confirmationManager.hasPendingCommand());
    }

    @Test
    public void equals() {
        ConfirmationManager confirmationManager = new ConfirmationManager();
        ConfirmCommand confirmCommand = new ConfirmCommand(confirmationManager);

        // same object -> returns true
        assertTrue(confirmCommand.equals(confirmCommand));

        // different types -> returns false
        assertFalse(confirmCommand.equals(1));

        // null -> returns false
        assertFalse(confirmCommand.equals(null));
    }
}
