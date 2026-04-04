package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a specific person after confirmation has been given.
 */
public class ConfirmedDeleteCommand extends Command {

    private final Person personToDelete;

    /**
     * Creates a confirmed delete command
     * @param personToDelete
     */
    public ConfirmedDeleteCommand(Person personToDelete) {
        requireNonNull(personToDelete);
        this.personToDelete = personToDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.deletePerson(personToDelete);
        return new CommandResult(String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ConfirmedDeleteCommand)) {
            return false;
        }
        ConfirmedDeleteCommand otherCommand = (ConfirmedDeleteCommand) other;
        return personToDelete.equals(otherCommand.personToDelete);
    }
}
