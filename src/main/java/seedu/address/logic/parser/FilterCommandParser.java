package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ABSENCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROGRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import java.util.Optional;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.Progress;
import seedu.address.model.person.TGroup;

/**
 * Parses input arguments and creates a new FilterCommand object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    static final String MESSAGE_INVALID_PREFIX =
            "Invalid prefix in filter command. Allowed prefixes are: crs/, tg/, p/, and abs/.\n"
                    + FilterCommand.MESSAGE_USAGE;
    static final String MESSAGE_UNEXPECTED_PREAMBLE =
            "Unexpected text before prefixes.\n" + FilterCommand.MESSAGE_USAGE;
    static final String MESSAGE_EMPTY_COURSE_ID =
            "Missing value for prefix: crs/\nCourse ID cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    static final String MESSAGE_EMPTY_TGROUP =
            "Missing value for prefix: tg/\nTutorial group cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    static final String MESSAGE_EMPTY_PROGRESS =
            "Missing value for prefix: p/\nProgress cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    static final String MESSAGE_EMPTY_ABSENCE =
            "Missing value for prefix: abs/\nAbsence count cannot be empty.\n" + FilterCommand.MESSAGE_USAGE;
    static final String MESSAGE_NO_FILTERS =
            "At least one filter must be provided.\n" + FilterCommand.MESSAGE_USAGE;
    static final String MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH =
            "It looks like you used a prefix without the trailing '/' or value. "
                    + "Make sure to use the form: <prefix>/<value> (e.g., abs/2).\n"
                    + FilterCommand.MESSAGE_USAGE;

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_PROGRESS, PREFIX_ABSENCE);

        validateInput(args, argMultimap);

        Optional<CourseId> courseId = parseCourseId(argMultimap);
        Optional<TGroup> tGroup = parseTGroup(argMultimap);
        Optional<Progress> progress = parseProgress(argMultimap);
        Optional<Integer> absenceCount = parseAbsenceCount(argMultimap);

        return new FilterCommand(new FilterMatchesPredicate(courseId, tGroup, progress, absenceCount));
    }

    /**
     * Validates the overall structure of the filter command input.
     */
    private void validateInput(String args, ArgumentMultimap argMultimap) throws ParseException {

        checkForBarePrefixes(argMultimap);
        checkForUnknownPrefixTokens(args);
        checkForUnexpectedPreamble(argMultimap);
        ensureAtLeastOneFilterPresent(argMultimap);
        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_PROGRESS, PREFIX_ABSENCE);
        checkForMissingValues(argMultimap);

    }

    /**
     * Checks for invalid prefixes and unexpected text before valid prefixes.
     */
    private void checkForUnknownPrefixTokens(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return;
        }

        for (String part : trimmedArgs.split("\\s+")) {
            if (part.contains("/")
                    && !part.startsWith(PREFIX_COURSEID.getPrefix())
                    && !part.startsWith(PREFIX_TGROUP.getPrefix())
                    && !part.startsWith(PREFIX_PROGRESS.getPrefix())
                    && !part.startsWith(PREFIX_ABSENCE.getPrefix())) {
                throw new ParseException(MESSAGE_INVALID_PREFIX);
            }
        }
    }

    /**
     * Checks for unexpected preamble text before valid prefixes.
     */
    private void checkForUnexpectedPreamble(ArgumentMultimap argMultimap) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty()) {
            return;
        }

        // Any remaining unexpected preamble text is treated as an unexpected preamble.
        throw new ParseException(MESSAGE_UNEXPECTED_PREAMBLE);
    }

    /**
     * Checks for bare prefix tokens in the preamble (prefix name without '/') and
     * throws a helpful message suggesting the correct '<prefix>/<value>' form.
     */
    private void checkForBarePrefixes(ArgumentMultimap argMultimap) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty()) {
            return;
        }

        String[] barePrefixes = new String[] {
                PREFIX_COURSEID.getPrefix().endsWith("/")
                        ? PREFIX_COURSEID.getPrefix().substring(0, PREFIX_COURSEID.getPrefix().length() - 1)
                        : PREFIX_COURSEID.getPrefix(),
                PREFIX_TGROUP.getPrefix().endsWith("/")
                        ? PREFIX_TGROUP.getPrefix().substring(0, PREFIX_TGROUP.getPrefix().length() - 1)
                        : PREFIX_TGROUP.getPrefix(),
                PREFIX_PROGRESS.getPrefix().endsWith("/")
                        ? PREFIX_PROGRESS.getPrefix().substring(0, PREFIX_PROGRESS.getPrefix().length() - 1)
                        : PREFIX_PROGRESS.getPrefix(),
                PREFIX_ABSENCE.getPrefix().endsWith("/")
                        ? PREFIX_ABSENCE.getPrefix().substring(0, PREFIX_ABSENCE.getPrefix().length() - 1)
                        : PREFIX_ABSENCE.getPrefix()
        };

        for (String token : preamble.split("\\s+")) {
            for (String bare : barePrefixes) {
                if (token.equalsIgnoreCase(bare)) {
                    throw new ParseException(MESSAGE_POSSIBLE_PREFIX_MISSING_SLASH);
                }
            }
        }
    }

    /**
     * Checks for missing values after prefixes.
     */
    private void checkForMissingValues(ArgumentMultimap argMultimap) throws ParseException {
        if (hasBlankValue(argMultimap, PREFIX_COURSEID)) {
            throw new ParseException(MESSAGE_EMPTY_COURSE_ID);
        }

        if (hasBlankValue(argMultimap, PREFIX_TGROUP)) {
            throw new ParseException(MESSAGE_EMPTY_TGROUP);
        }

        if (hasBlankValue(argMultimap, PREFIX_PROGRESS)) {
            throw new ParseException(MESSAGE_EMPTY_PROGRESS);
        }

        if (hasBlankValue(argMultimap, PREFIX_ABSENCE)) {
            throw new ParseException(MESSAGE_EMPTY_ABSENCE);
        }
    }

    /**
     * Ensures that at least one filter field is present in the argument multimap.
     */
    private void ensureAtLeastOneFilterPresent(ArgumentMultimap argMultimap) throws ParseException {
        boolean anyPresent = argMultimap.getValue(PREFIX_COURSEID).isPresent()
                || argMultimap.getValue(PREFIX_TGROUP).isPresent()
                || argMultimap.getValue(PREFIX_PROGRESS).isPresent()
                || argMultimap.getValue(PREFIX_ABSENCE).isPresent();

        if (!anyPresent) {
            throw new ParseException(MESSAGE_NO_FILTERS);
        }
    }

    /**
     * Returns the parsed course ID if present.
     */
    private Optional<CourseId> parseCourseId(ArgumentMultimap argMultimap) throws ParseException {
        return parseOptional(argMultimap, PREFIX_COURSEID, ParserUtil::parseCourseId);
    }

    /**
     * Returns the parsed tutorial group if present.
     */
    private Optional<TGroup> parseTGroup(ArgumentMultimap argMultimap) throws ParseException {
        return parseOptional(argMultimap, PREFIX_TGROUP, ParserUtil::parseTGroup);
    }

    /**
     * Returns the parsed progress value if present.
     */
    private Optional<Progress> parseProgress(ArgumentMultimap argMultimap) throws ParseException {
        return parseOptional(argMultimap, PREFIX_PROGRESS, ParserUtil::parseProgress);
    }

    /**
     * Returns the parsed absence count if present.
     */
    private Optional<Integer> parseAbsenceCount(ArgumentMultimap argMultimap) throws ParseException {
        return parseOptional(argMultimap, PREFIX_ABSENCE, ParserUtil::parseAbsenceCount);
    }

    /**
     * Generic helper to parse an optional value using a parser that may throw ParseException.
     */
    private interface ThrowingParser<R> {
        R parse(String s) throws ParseException;
    }

    private <R> Optional<R> parseOptional(ArgumentMultimap argMultimap, Prefix prefix,
                                          ThrowingParser<R> parser) throws ParseException {
        Optional<String> value = argMultimap.getValue(prefix);
        return value.isPresent() ? Optional.of(parser.parse(value.get())) : Optional.empty();
    }

    /**
     * Returns true if the given prefix is present but its value is blank.
     */
    private boolean hasBlankValue(ArgumentMultimap argMultimap, Prefix prefix) {
        return argMultimap.getValue(prefix)
                .map(v -> v.trim().isEmpty())
                .orElse(false);
    }
}
