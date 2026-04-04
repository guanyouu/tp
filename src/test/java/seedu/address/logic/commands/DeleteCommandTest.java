package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.ConfirmationManager;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws CommandException {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ConfirmationManager confirmationManager = new ConfirmationManager();
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, confirmationManager);

        CommandResult commandResult = deleteCommand.execute(model);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE,
                personToDelete.getName());

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertTrue(confirmationManager.hasPendingCommand());
        assertTrue(model.getFilteredPersonList().contains(personToDelete));
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexUnfilteredList_returnsConfirmationMessage() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ConfirmationManager confirmationManager = new ConfirmationManager();
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, confirmationManager);

        CommandResult commandResult = null;
        try {
            commandResult = deleteCommand.execute(model);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }

        String expectedMessage = String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE,
                personToDelete.getName());

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertTrue(confirmationManager.hasPendingCommand());
        assertTrue(model.getFilteredPersonList().contains(personToDelete));
    }

    @Test
    public void execute_validIndexFilteredList_returnsConfirmationMessage() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ConfirmationManager confirmationManager = new ConfirmationManager();
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, confirmationManager);

        CommandResult commandResult = null;
        try {
            commandResult = deleteCommand.execute(model);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }

        String expectedMessage = String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE,
                personToDelete.getName());

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertTrue(confirmationManager.hasPendingCommand());
        assertTrue(model.getFilteredPersonList().contains(personToDelete));
    }

    @Test
    public void execute_validDetailsUnfilteredList_returnsConfirmationMessage() {
        ConfirmationManager confirmationManager = new ConfirmationManager();
        DeleteCommand deleteCommand = new DeleteCommand(
                new StudentId(ALICE.getStudentId().value),
                new CourseId(ALICE.getCourseId().value),
                new TGroup(ALICE.getTGroup().value),
                confirmationManager);

        CommandResult commandResult = null;
        try {
            commandResult = deleteCommand.execute(model);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }

        String expectedMessage = String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE,
                ALICE.getName());

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertTrue(confirmationManager.hasPendingCommand());
        assertTrue(model.getFilteredPersonList().contains(ALICE));
    }

    @Test
    public void execute_invalidDetailsUnfilteredList_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(
                new StudentId("A0000000Z"),
                new CourseId("CS9999"),
                new TGroup("T99"));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_PERSON_NOT_FOUND_BY_DETAILS);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);
        DeleteCommand deleteByDetails = new DeleteCommand(
                new StudentId(ALICE.getStudentId().value),
                new CourseId(ALICE.getCourseId().value),
                new TGroup(ALICE.getTGroup().value));
        DeleteCommand deleteBySameDetails = new DeleteCommand(
                new StudentId(ALICE.getStudentId().value),
                new CourseId(ALICE.getCourseId().value),
                new TGroup(ALICE.getTGroup().value));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // same detail values -> returns true
        assertTrue(deleteByDetails.equals(deleteBySameDetails));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different targets -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
        assertFalse(deleteFirstCommand.equals(deleteByDetails));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex
                + ", targetStudentId=null, targetCourseId=null, targetTGroup=null}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
