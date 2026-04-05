package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ABSENCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROGRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import java.util.Arrays;
import java.util.Optional;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.Progress;
import seedu.address.model.person.TGroup;

/**
 * Parses input arguments and creates a new FilterCommand object.
 * Logic involves validating the presence of prefixes, ensuring no illegal preambles,
 * and parsing specific values into an appropriate predicate.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    // Error Messages (made public for use in tests)
    public static final String MESSAGE_INVALID_PREFIX =
            "Invalid prefix in filter command. Allowed prefixes are: crs/, tg/, p/, and abs./\n"
                    + FilterCommand.MESSAGE_USAGE;
    public static final String MESSAGE_UNEXPECTED_PREAMBLE =
            "Unexpected text before prefixes.\n" + FilterCommand.MESSAGE_USAGE;
    public static final String MESSAGE_EMPTY_COURSE_ID =
            "Missing value for prefix: crs/\nCourse ID cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    public static final String MESSAGE_EMPTY_TGROUP =
            "Missing value for prefix: tg/\nTutorial group cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    public static final String MESSAGE_EMPTY_PROGRESS =
            "Missing value for prefix: p/\nProgress cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    public static final String MESSAGE_EMPTY_ABSENCE =
            "Missing value for prefix: abs/\nAbsence count cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    public static final String MESSAGE_NO_FILTERS =
            "At least one filter must be provided.\n" + FilterCommand.MESSAGE_USAGE;
    public static final String MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH =
            "It looks like you used a prefix without the trailing '/' or value. "
                    + "Make sure to use the form: <prefix>/<value> (e.g., abs/2).\n"
                    + FilterCommand.MESSAGE_USAGE;
    /**
     * The list of prefixes that this parser is able to handle.
     */
    private static final Prefix[] FILTER_PREFIXES = {
        PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_PROGRESS, PREFIX_ABSENCE
    };


    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     *
     * @param args The raw user input.
     * @return A FilterCommand object.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    @Override
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, FILTER_PREFIXES);

        validateInput(args, argMultimap);

        Optional<CourseId> courseId = parseCourseId(argMultimap);
        Optional<TGroup> tGroup = parseTGroup(argMultimap);
        Optional<Progress> progress = parseProgress(argMultimap);
        Optional<Integer> absenceCount = parseAbsenceCount(argMultimap);

        return new FilterCommand(new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount));
    }

    /**
     * Orchestrates various structural and logical validation checks on the tokenized input.
     *
     * @param args        The raw input string.
     * @param argMultimap The map of tokens to values.
     * @throws ParseException if validation fails.
     */
    private void validateInput(String args, ArgumentMultimap argMultimap) throws ParseException {
        checkForBarePrefixes(argMultimap);
        checkForUnknownPrefixTokens(args);
        checkForUnexpectedPreamble(argMultimap);
        ensureAtLeastOneFilterPresent(argMultimap);
        argMultimap.verifyNoDuplicatePrefixesFor(FILTER_PREFIXES);
        checkForMissingValues(argMultimap);
    }

    /**
     * Detects tokens that appear to be prefixes but lack the required trailing slash.
     */
    private void checkForBarePrefixes(ArgumentMultimap argMultimap) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty()) {
            return;
        }

        for (String token : preamble.split("\\s+")) {
            for (Prefix p : FILTER_PREFIXES) {
                String bare = p.getPrefix().replace("/", "");
                if (token.equalsIgnoreCase(bare)) {
                    throw new ParseException(MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH);
                }
            }
        }
    }

    /**
     * Checks if any provided token containing a slash does not match our allowed prefixes.
     */
    private void checkForUnknownPrefixTokens(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return;
        }

        for (String part : trimmedArgs.split("\\s+")) {
            if (!part.contains("/")) {
                continue;
            }
            boolean matchesAny = Arrays.stream(FILTER_PREFIXES)
                    .anyMatch(prefix -> part.startsWith(prefix.getPrefix()));
            if (!matchesAny) {
                throw new ParseException(MESSAGE_INVALID_PREFIX);
            }
        }
    }

    /**
     * Ensures that there is no text before the first valid prefix.
     */
    private void checkForUnexpectedPreamble(ArgumentMultimap argMultimap) throws ParseException {
        if (!argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_PREAMBLE);
        }
    }

    /**
     * Validates that prefixes present in the command actually contain non-blank values.
     */
    private void checkForMissingValues(ArgumentMultimap argMultimap) throws ParseException {
        if (isPrefixValueBlank(argMultimap, PREFIX_COURSEID)) {
            throw new ParseException(MESSAGE_EMPTY_COURSE_ID);
        }
        if (isPrefixValueBlank(argMultimap, PREFIX_TGROUP)) {
            throw new ParseException(MESSAGE_EMPTY_TGROUP);
        }
        if (isPrefixValueBlank(argMultimap, PREFIX_PROGRESS)) {
            throw new ParseException(MESSAGE_EMPTY_PROGRESS);
        }
        if (isPrefixValueBlank(argMultimap, PREFIX_ABSENCE)) {
            throw new ParseException(MESSAGE_EMPTY_ABSENCE);
        }
    }

    /**
     * Ensures that the user has provided at least one criteria to filter by.
     */
    private void ensureAtLeastOneFilterPresent(ArgumentMultimap argMultimap) throws ParseException {
        boolean anyPresent = Arrays.stream(FILTER_PREFIXES)
                .anyMatch(prefix -> argMultimap.getValue(prefix).isPresent());
        if (!anyPresent) {
            throw new ParseException(MESSAGE_NO_FILTERS);
        }
    }

    /**
     * Parses the CourseId field.
     */
    private Optional<CourseId> parseCourseId(ArgumentMultimap argMultimap) throws ParseException {
        return argMultimap.getValue(PREFIX_COURSEID).isPresent()
                ? Optional.of(ParserUtil.parseCourseId(argMultimap.getValue(PREFIX_COURSEID).get()))
                : Optional.empty();
    }

    /**
     * Parses the Tutorial Group field.
     */
    private Optional<TGroup> parseTGroup(ArgumentMultimap argMultimap) throws ParseException {
        return argMultimap.getValue(PREFIX_TGROUP).isPresent()
                ? Optional.of(ParserUtil.parseTGroup(argMultimap.getValue(PREFIX_TGROUP).get()))
                : Optional.empty();
    }

    /**
     * Parses the Progress status field.
     */
    private Optional<Progress> parseProgress(ArgumentMultimap argMultimap) throws ParseException {
        return argMultimap.getValue(PREFIX_PROGRESS).isPresent()
                ? Optional.of(ParserUtil.parseProgress(argMultimap.getValue(PREFIX_PROGRESS).get()))
                : Optional.empty();
    }

    /**
     * Parses the Absence count field.
     */
    private Optional<Integer> parseAbsenceCount(ArgumentMultimap argMultimap) throws ParseException {
        return argMultimap.getValue(PREFIX_ABSENCE).isPresent()
                ? Optional.of(ParserUtil.parseAbsenceCount(argMultimap.getValue(PREFIX_ABSENCE).get()))
                : Optional.empty();
    }

    /**
     * Returns true if the given prefix is present but has a blank value.
     */
    private boolean isPrefixValueBlank(ArgumentMultimap argMultimap, Prefix prefix) {
        return argMultimap.getValue(prefix)
                .map(v -> v.trim().isEmpty())
                .orElse(false);
    }
}
