package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains unit tests for {@code DeleteCommandParser}.
 */
public class DeleteCommandParserTest {

    private final DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validIndex_success() {
        assertParseSuccess(parser,
                "1",
                new DeleteCommand(Index.fromOneBased(1)));
    }

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser,
                "",
                DeleteCommand.MESSAGE_EMPTY_INPUT + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_onlyWhitespace_failure() {
        assertParseFailure(parser,
                "   ",
                DeleteCommand.MESSAGE_EMPTY_INPUT + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidArgs_failure() {
        assertParseFailure(parser,
                "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndexZero_failure() {
        assertParseFailure(parser,
                "0",
                DeleteCommand.MESSAGE_INVALID_INDEX + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidNegativeIndex_failure() {
        assertParseFailure(parser,
                "-1",
                DeleteCommand.MESSAGE_INVALID_INDEX + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_decimalIndex_failure() {
        assertParseFailure(parser,
                "1.5",
                DeleteCommand.MESSAGE_INVALID_INDEX + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_unexpectedTextAfterIndex_failure() {
        assertParseFailure(parser,
                "1 extra",
                DeleteCommand.MESSAGE_UNEXPECTED_TEXT_AFTER_INDEX + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_onlyStudentIdPrefix_failure() {
        String input = PREFIX_STUDENTID.getPrefix() + ALICE.getStudentId().value;

        assertParseFailure(parser,
                input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyCourseIdPrefix_failure() {
        String input = PREFIX_COURSEID.getPrefix() + ALICE.getCourseId().value;

        assertParseFailure(parser,
                input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyTGroupPrefix_failure() {
        String input = PREFIX_TGROUP.getPrefix() + ALICE.getTGroup().value;

        assertParseFailure(parser,
                input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
    @Test
    public void parse_duplicatePrefix_failure() {
        String input = PREFIX_STUDENTID.getPrefix() + ALICE.getStudentId().value
                + " " + PREFIX_STUDENTID.getPrefix() + "A7654321A"
                + " " + PREFIX_COURSEID.getPrefix() + ALICE.getCourseId().value
                + " " + PREFIX_TGROUP.getPrefix() + ALICE.getTGroup().value;

        ParseException thrown = org.junit.jupiter.api.Assertions.assertThrows(ParseException.class, (
                ) -> parser.parse(input));

        assertTrue(thrown.getMessage().contains(PREFIX_STUDENTID.toString().trim()));
    }

    @Test
    public void parse_barePrefix_failure() {
        String input = PREFIX_STUDENTID.getPrefix().replace("/", "").trim()
                + " " + ALICE.getStudentId().value
                + " " + PREFIX_COURSEID.getPrefix() + ALICE.getCourseId().value
                + " " + PREFIX_TGROUP.getPrefix() + ALICE.getTGroup().value;

        ParseException thrown = org.junit.jupiter.api.Assertions.assertThrows(ParseException.class, (
                ) -> parser.parse(input));

        assertTrue(thrown.getMessage().contains(DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unknownPrefix_failure() {
        String input = "sid/" + ALICE.getStudentId().value
                + " " + PREFIX_COURSEID.getPrefix() + ALICE.getCourseId().value
                + " " + PREFIX_TGROUP.getPrefix() + ALICE.getTGroup().value;

        assertParseFailure(parser,
                input,
                ParserMessages.invalidPrefix("id/, crs/, tg/", DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_indexWithUnknownPrefix_failure() {
        assertParseFailure(parser,
                "1 sid/" + ALICE.getStudentId().value,
                ParserMessages.invalidPrefix("id/, crs/, tg/", DeleteCommand.MESSAGE_USAGE));
    }
}
