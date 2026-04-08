package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.CancelCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    /**
     * Stores the command awaiting user confirmation.
     */
    private Command pendingConfirmationCommand;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        this.addressBookParser = new AddressBookParser();
        this.pendingConfirmationCommand = null;
    }

    /**
     * Returns true if there is a command awaiting user confirmation.
     */
    private boolean hasPendingConfirmationCommand() {
        return pendingConfirmationCommand != null;
    }

    /**
     * Stores the given command as the pending command awaiting confirmation.
     */
    private void setPendingConfirmationCommand(Command command) {
        pendingConfirmationCommand = command;
    }

    /**
     * Clears any pending confirmation command.
     */
    private void clearPendingConfirmationCommand() {
        pendingConfirmationCommand = null;
    }

    /**
     * Saves the current address book state to storage.
     *
     * @throws CommandException if the save operation fails
     */
    private void saveAddressBook() throws CommandException {
        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        String trimmedCommandText = commandText.trim();

        if (hasPendingConfirmationCommand()) {
            if (trimmedCommandText.equalsIgnoreCase(ConfirmCommand.COMMAND_WORD)) {
                Command commandToExecute = pendingConfirmationCommand;
                clearPendingConfirmationCommand();
                CommandResult commandResult = commandToExecute.execute(model);
                saveAddressBook();
                return commandResult;
            }

            if (trimmedCommandText.equalsIgnoreCase(CancelCommand.COMMAND_WORD)) {
                clearPendingConfirmationCommand();
                return new CommandResult(CancelCommand.MESSAGE_CANCEL_SUCCESS);
            }

            clearPendingConfirmationCommand();
        }

        Command command = addressBookParser.parseCommand(commandText);

        if (command instanceof DeleteCommand) {
            DeleteCommand deleteCommand = (DeleteCommand) command;
            setPendingConfirmationCommand(deleteCommand.getConfirmedCommand(model));
            return new CommandResult(deleteCommand.getConfirmationMessage(model));
        }

        CommandResult commandResult = command.execute(model);
        saveAddressBook();
        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
