package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
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
    public void getPersonToDelete_validIndexUnfilteredList_success() throws Exception {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        assertEquals(personToDelete, deleteCommand.getPersonToDelete(model));
        assertTrue(model.getFilteredPersonList().contains(personToDelete));
    }

    @Test
    public void getPersonToDelete_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, (
                ) -> deleteCommand.getPersonToDelete(model));
    }

    @Test
    public void getConfirmationMessage_validIndexUnfilteredList_success() throws Exception {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE, personToDelete.getName());

        assertEquals(expectedMessage, deleteCommand.getConfirmationMessage(model));
        assertTrue(model.getFilteredPersonList().contains(personToDelete));
    }

    @Test
    public void getConfirmationMessage_validIndexFilteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE, personToDelete.getName());

        assertEquals(expectedMessage, deleteCommand.getConfirmationMessage(model));
        assertTrue(model.getFilteredPersonList().contains(personToDelete));
    }

    @Test
    public void getConfirmationMessage_validDetailsUnfilteredList_success() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(
                new StudentId(ALICE.getStudentId().value),
                new CourseId(ALICE.getCourseId().value),
                new TGroup(ALICE.getTGroup().value));

        String expectedMessage = String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE, ALICE.getName());

        assertEquals(expectedMessage, deleteCommand.getConfirmationMessage(model));
        assertTrue(model.getFilteredPersonList().contains(ALICE));
    }

    @Test
    public void getPersonToDelete_invalidDetailsUnfilteredList_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(
                new StudentId("A0000000Z"),
                new CourseId("CS9999"),
                new TGroup("T99"));

        assertThrows(CommandException.class, DeleteCommand.MESSAGE_PERSON_NOT_FOUND_BY_DETAILS, (
                ) -> deleteCommand.getPersonToDelete(model));
    }

    @Test
    public void getConfirmedCommand_validIndex_success() throws Exception {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        Command confirmedCommand = deleteCommand.getConfirmedCommand(model);

        assertEquals(new ConfirmedDeleteCommand(personToDelete), confirmedCommand);
    }

    @Test
    public void execute_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        assertThrows(CommandException.class,
                "Delete command should be handled through confirmation flow.", (
                    ) -> deleteCommand.execute(model));
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
}
