package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Contains unit tests for {@code CancelCommand}.
 */
public class CancelCommandTest {

    @Test
    public void execute_directExecution_throwsCommandException() {
        CancelCommand cancelCommand = new CancelCommand();
        Model model = new ModelManager();

        CommandException thrown = assertThrows(CommandException.class, (
                ) -> cancelCommand.execute(model));

        assertEquals("Cancel command should be handled through confirmation flow.",
                thrown.getMessage());
    }
}
