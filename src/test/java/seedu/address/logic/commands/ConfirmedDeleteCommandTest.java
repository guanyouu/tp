package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ConfirmedDeleteCommand}.
 */
public class ConfirmedDeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validPerson_success() {
        Person personToDelete = ALICE;
        ConfirmedDeleteCommand confirmedDeleteCommand = new ConfirmedDeleteCommand(personToDelete);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        assertCommandSuccess(confirmedDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        ConfirmedDeleteCommand deleteAliceCommand = new ConfirmedDeleteCommand(ALICE);
        ConfirmedDeleteCommand deleteAliceCommandCopy = new ConfirmedDeleteCommand(ALICE);
        ConfirmedDeleteCommand deleteBensonCommand = new ConfirmedDeleteCommand(BENSON);

        // same object -> returns true
        assertTrue(deleteAliceCommand.equals(deleteAliceCommand));

        // same values -> returns true
        assertTrue(deleteAliceCommand.equals(deleteAliceCommandCopy));

        // different types -> returns false
        assertFalse(deleteAliceCommand.equals(1));

        // null -> returns false
        assertFalse(deleteAliceCommand.equals(null));

        // different persons -> returns false
        assertFalse(deleteAliceCommand.equals(deleteBensonCommand));
    }
}
