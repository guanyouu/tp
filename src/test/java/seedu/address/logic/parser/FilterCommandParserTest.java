package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.Progress;
import seedu.address.model.person.TGroup;

/**
 * Contains unit tests for {@link FilterCommandParser}.
 */
public class FilterCommandParserTest {

    private static final String MESSAGE_INVALID_ABSENCE_COUNT =
            "Absence count must be a non-negative integer.";
    private static final String MESSAGE_INVALID_PROGRESS =
            "Invalid progress value. Allowed values are: on_track, needs_attention, at_risk, clear.";

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser, "", FilterCommandParser.MESSAGE_NO_FILTERS);
    }

    @Test
    public void parse_blankArgs_failure() {
        assertParseFailure(parser, "   ", FilterCommandParser.MESSAGE_NO_FILTERS);
    }

    @Test
    public void parse_textOnly_failure() {
        assertParseFailure(parser, " hello", FilterCommandParser.MESSAGE_UNEXPECTED_PREAMBLE);
    }

    @Test
    public void parse_invalidPrefix_failure() {
        assertParseFailure(parser, " x/CS2103T", FilterCommandParser.MESSAGE_INVALID_PREFIX);
    }

    @Test
    public void parse_missingCourseIdValue_failure() {
        assertParseFailure(parser, " crs/", FilterCommandParser.MESSAGE_EMPTY_COURSE_ID);
    }

    @Test
    public void parse_barePrefixWithoutSlash_failure() {
        assertParseFailure(parser, " abs", FilterCommandParser.MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH);
        assertParseFailure(parser, " crs", FilterCommandParser.MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH);
    }

    @Test
    public void parse_invalidCourseId_failure() {
        assertParseFailure(parser, " crs/!", CourseId.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidProgress_failure() {
        assertParseFailure(parser, " p/INVALID", MESSAGE_INVALID_PROGRESS);
    }

    @Test
    public void parse_nonNumericAbsence_failure() {
        assertParseFailure(parser, " abs/abc", MESSAGE_INVALID_ABSENCE_COUNT);
    }

    @Test
    public void parse_duplicateCoursePrefix_failure() {
        assertParseFailure(parser, " crs/CS2103T crs/CS2103T",
                "Multiple values specified for the following single-valued field(s): crs/");
    }

    @Test
    public void parse_validCourseAndTGroup_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.of(new CourseId("CS2103T")),
                        Optional.of(new TGroup("T01")),
                        Optional.empty(),
                        Optional.empty()));
        assertParseSuccess(parser, " crs/CS2103T tg/T01", expectedCommand);
    }

    @Test
    public void parse_validAllFilters_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.of(new CourseId("CS2103T")),
                        Optional.of(new TGroup("T01")),
                        Optional.of(Progress.ON_TRACK),
                        Optional.of(3)));
        assertParseSuccess(parser, " crs/CS2103T tg/T01 p/ON_TRACK abs/3", expectedCommand);
    }

    @Test
    public void parse_validArgsWithExtraSpaces_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.of(new CourseId("CS2103T")),
                        Optional.of(new TGroup("T01")),
                        Optional.empty(),
                        Optional.of(1)));
        assertParseSuccess(parser, "  crs/CS2103T   tg/T01   abs/1  ", expectedCommand);
    }
}
