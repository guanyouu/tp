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
    public void parse_unknownPrefixAfterValidPrefix_failure() {
        assertParseFailure(parser, " crs/CS2103T group/T01", FilterCommandParser.MESSAGE_INVALID_PREFIX);
    }

    @Test
    public void parse_unknownPrefixAfterTGroup_failure() {
        assertParseFailure(parser, " tg/T01 bad/value", FilterCommandParser.MESSAGE_INVALID_PREFIX);
    }

    @Test
    public void parse_unknownPrefixAfterProgress_failure() {
        assertParseFailure(parser, " p/ON_TRACK bad/value", FilterCommandParser.MESSAGE_INVALID_PREFIX);
    }

    @Test
    public void parse_unknownPrefixAfterAbsence_failure() {
        assertParseFailure(parser, " abs/3 bad/value", FilterCommandParser.MESSAGE_INVALID_PREFIX);
    }

    @Test
    public void parse_missingCourseIdValue_failure() {
        assertParseFailure(parser, " crs/", FilterCommandParser.MESSAGE_EMPTY_COURSE_ID);
    }

    @Test
    public void parse_missingTGroupValue_failure() {
        assertParseFailure(parser, " tg/", FilterCommandParser.MESSAGE_EMPTY_TGROUP);
    }

    @Test
    public void parse_missingProgressValue_failure() {
        assertParseFailure(parser, " p/", FilterCommandParser.MESSAGE_EMPTY_PROGRESS);
    }

    @Test
    public void parse_missingAbsenceValue_failure() {
        assertParseFailure(parser, " abs/", FilterCommandParser.MESSAGE_EMPTY_ABSENCE);
    }

    @Test
    public void parse_unexpectedTextBeforePrefixes_failure() {
        assertParseFailure(parser, " hello crs/CS2103T", FilterCommandParser.MESSAGE_UNEXPECTED_PREAMBLE);
    }

    @Test
    public void parse_unexpectedTextBeforeBothPrefixes_failure() {
        assertParseFailure(parser, " hello crs/CS2103T tg/T01",
                FilterCommandParser.MESSAGE_UNEXPECTED_PREAMBLE);
    }

    @Test
    public void parse_barePrefixWithoutSlash_failure() {
        assertParseFailure(parser, " abs", FilterCommandParser.MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH);
        assertParseFailure(parser, " crs", FilterCommandParser.MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH);
        assertParseFailure(parser, " p", FilterCommandParser.MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH);
        assertParseFailure(parser, " tg", FilterCommandParser.MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH);
    }

    @Test
    public void parse_invalidCourseId_failure() {
        assertParseFailure(parser, " crs/!", CourseId.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTGroup_failure() {
        assertParseFailure(parser, " tg/!", TGroup.MESSAGE_CONSTRAINTS);
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
    public void parse_negativeAbsence_failure() {
        assertParseFailure(parser, " abs/-1", MESSAGE_INVALID_ABSENCE_COUNT);
    }

    @Test
    public void parse_duplicateCoursePrefix_failure() {
        assertParseFailure(parser, " crs/CS2103T crs/CS2103T",
                "Multiple values specified for the following single-valued field(s): crs/");
    }

    @Test
    public void parse_duplicateTGroupPrefix_failure() {
        assertParseFailure(parser, " tg/T01 tg/T02",
                "Multiple values specified for the following single-valued field(s): tg/");
    }

    @Test
    public void parse_duplicateProgressPrefix_failure() {
        assertParseFailure(parser, " p/ON_TRACK p/AT_RISK",
                "Multiple values specified for the following single-valued field(s): p/");
    }

    @Test
    public void parse_duplicateAbsencePrefix_failure() {
        assertParseFailure(parser, " abs/2 abs/3",
                "Multiple values specified for the following single-valued field(s): abs/");
    }

    @Test
    public void parse_duplicateMixedPrefix_failure() {
        assertParseFailure(parser, " crs/CS2103T tg/T01 crs/CS2040",
                "Multiple values specified for the following single-valued field(s): crs/");
    }

    @Test
    public void parse_bothPrefixesMissingValues_failure() {
        assertParseFailure(parser, " crs/ tg/", FilterCommandParser.MESSAGE_EMPTY_COURSE_ID);
    }

    @Test
    public void parse_invalidCourseIdWithValidTGroup_failure() {
        assertParseFailure(parser, " crs/! tg/T01", CourseId.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTGroupWithValidCourseId_failure() {
        assertParseFailure(parser, " crs/CS2103T tg/!", TGroup.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidProgressWithValidCourseId_failure() {
        assertParseFailure(parser, " crs/CS2103T prog/INVALID", FilterCommandParser.MESSAGE_INVALID_PREFIX);
    }

    @Test
    public void parse_validArgsReversedOrder_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.of(new CourseId("CS2103T")),
                        Optional.of(new TGroup("T01")),
                        Optional.empty(),
                        Optional.empty()));
        assertParseSuccess(parser, " tg/T01 crs/CS2103T", expectedCommand);
    }

    @Test
    public void parse_validCourseOnly_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.of(new CourseId("CS2103T")),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()));
        assertParseSuccess(parser, " crs/CS2103T", expectedCommand);
    }

    @Test
    public void parse_validTGroupOnly_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.empty(),
                        Optional.of(new TGroup("T01")),
                        Optional.empty(),
                        Optional.empty()));
        assertParseSuccess(parser, " tg/T01", expectedCommand);
    }

    @Test
    public void parse_validProgressOnly_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(Progress.ON_TRACK),
                        Optional.empty()));
        assertParseSuccess(parser, " p/ON_TRACK", expectedCommand);
    }

    @Test
    public void parse_validAbsenceOnly_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(3)));
        assertParseSuccess(parser, " abs/3", expectedCommand);
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
    public void parse_validCourseAndProgress_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.of(new CourseId("CS2103T")),
                        Optional.empty(),
                        Optional.of(Progress.ON_TRACK),
                        Optional.empty()));
        assertParseSuccess(parser, " crs/CS2103T p/ON_TRACK", expectedCommand);
    }

    @Test
    public void parse_validCourseAndAbsence_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.of(new CourseId("CS2103T")),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(2)));
        assertParseSuccess(parser, " crs/CS2103T abs/2", expectedCommand);
    }

    @Test
    public void parse_validTGroupAndProgress_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.empty(),
                        Optional.of(new TGroup("T01")),
                        Optional.of(Progress.ON_TRACK),
                        Optional.empty()));
        assertParseSuccess(parser, " tg/T01 p/ON_TRACK", expectedCommand);
    }

    @Test
    public void parse_validProgressAndAbsence_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new FilterMatchesPredicate(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(Progress.ON_TRACK),
                        Optional.of(2)));
        assertParseSuccess(parser, " p/ON_TRACK abs/2", expectedCommand);
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