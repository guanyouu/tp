package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;

/**
 * Deletes a person identified using its displayed index
 * or exact student details from the address book,
 * after user confirmation is given.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a student identified either by the index number used in the displayed student list, "
            + "or by exact student details.\n"
            + "Parameters: (a) INDEX (must be a positive integer) "
            + "or (b) id/STUDENT_ID crs/COURSE_ID tg/TUTORIAL_GROUP\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "Example: " + COMMAND_WORD + " id/A1234567X crs/CS2103T tg/T01";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    public static final String MESSAGE_PERSON_NOT_FOUND_BY_DETAILS =
            "No student matching the given student ID, course ID, and tutorial group was found.";

    public static final String MESSAGE_EMPTY_INPUT =
            "No student specified. Please provide the index number or student ID, course ID, and tutorial group.";

    public static final String MESSAGE_INVALID_INDEX =
            "Invalid index number";

    public static final String MESSAGE_UNEXPECTED_TEXT_AFTER_INDEX =
            "Unexpected text after index.";

    public static final String MESSAGE_CONFIRM_DELETE =
            "Are you sure you want to delete %s? Type 'yes' to confirm or 'no' to cancel.";

    private final Index targetIndex;
    private final StudentId targetStudentId;
    private final CourseId targetCourseId;
    private final TGroup targetTGroup;

    /**
     * Creates a DeleteCommand to delete a person by index.
     *
     * @param targetIndex Index of the person in the filtered person list.
     */
    public DeleteCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.targetStudentId = null;
        this.targetCourseId = null;
        this.targetTGroup = null;
    }

    /**
     * Creates a DeleteCommand to delete a person by exact student details.
     *
     * @param targetStudentId Student ID of the person to delete.
     * @param targetCourseId Course ID of the person to delete.
     * @param targetTGroup Tutorial group of the person to delete.
     */
    public DeleteCommand(StudentId targetStudentId, CourseId targetCourseId, TGroup targetTGroup) {
        requireNonNull(targetStudentId);
        requireNonNull(targetCourseId);
        requireNonNull(targetTGroup);
        this.targetIndex = null;
        this.targetStudentId = targetStudentId;
        this.targetCourseId = targetCourseId;
        this.targetTGroup = targetTGroup;
    }

    /**
     * Resolves and returns the person to delete.
     * Index-based deletion uses the currently filtered list.
     * Details-based deletion searches the full address book.
     */
    public Person getPersonToDelete(Model model) throws CommandException {
        requireNonNull(model);

        Person personToDelete;

        if (targetIndex != null) {
            List<Person> lastShownList = model.getFilteredPersonList();

            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personToDelete = lastShownList.get(targetIndex.getZeroBased());
        } else {
            List<Person> fullPersonList = model.getFullPersonList();
            personToDelete = null;

            for (Person person : fullPersonList) {
                boolean hasMatchingStudentId = person.getStudentId().equals(targetStudentId);
                boolean hasMatchingCourseId = person.getCourseId().equals(targetCourseId);
                boolean hasMatchingTGroup = person.getTGroup().equals(targetTGroup);

                if (hasMatchingStudentId && hasMatchingCourseId && hasMatchingTGroup) {
                    personToDelete = person;
                    break;
                }
            }

            if (personToDelete == null) {
                throw new CommandException(MESSAGE_PERSON_NOT_FOUND_BY_DETAILS);
            }
        }

        return personToDelete;
    }

    /**
     * Creates and returns the command to execute after delete confirmation is given.
     */
    public Command getConfirmedCommand(Model model) throws CommandException {
        Person personToDelete = getPersonToDelete(model);
        return new ConfirmedDeleteCommand(personToDelete);
    }

    /**
     * Returns the delete confirmation message for the resolved target person.
     */
    public String getConfirmationMessage(Model model) throws CommandException {
        Person personToDelete = getPersonToDelete(model);
        return String.format(MESSAGE_CONFIRM_DELETE, personToDelete.getName());
    }

    /**
     * DeleteCommand should be intercepted by LogicManager to handle confirmation first.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException("Delete command should be handled through confirmation flow.");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;

        if (targetIndex != null && otherDeleteCommand.targetIndex != null) {
            return targetIndex.equals(otherDeleteCommand.targetIndex);
        }

        return targetIndex == null
                && otherDeleteCommand.targetIndex == null
                && targetStudentId.equals(otherDeleteCommand.targetStudentId)
                && targetCourseId.equals(otherDeleteCommand.targetCourseId)
                && targetTGroup.equals(otherDeleteCommand.targetTGroup);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetStudentId", targetStudentId)
                .add("targetCourseId", targetCourseId)
                .add("targetTGroup", targetTGroup)
                .toString();
    }

    /**
     * Returns true if this delete command targets a person by index.
     */
    public boolean isDeleteByIndex() {
        return targetIndex != null;
    }

    /**
     * Returns the target index if this command deletes by index.
     */
    public Index getTargetIndex() {
        return targetIndex;
    }

    /**
     * Returns the target student ID for detail-based deletion.
     */
    public StudentId getTargetStudentId() {
        return targetStudentId;
    }

    /**
     * Returns the target course ID for detail-based deletion.
     */
    public CourseId getTargetCourseId() {
        return targetCourseId;
    }

    /**
     * Returns the target tutorial group for detail-based deletion.
     */
    public TGroup getTargetTGroup() {
        return targetTGroup;
    }
}
