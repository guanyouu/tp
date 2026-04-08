package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Contains unit tests for {@code ConfirmCommand}.
 */
public class ConfirmCommandTest {

    @Test
    public void execute_directExecution_throwsCommandException() {
        ConfirmCommand confirmCommand = new ConfirmCommand();
        Model model = new ModelManager();

        CommandException thrown = assertThrows(CommandException.class, (
            )-> confirmCommand.execute(model));

        assertEquals("ConfirmCommand should be handled by LogicManager.", thrown.getMessage());
    }
}
