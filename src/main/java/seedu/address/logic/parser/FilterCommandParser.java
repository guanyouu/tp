package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import java.util.Optional;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.TGroup;

/**
 * Parses input arguments and creates a new FilterCommand object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COURSEID, PREFIX_TGROUP);

        checkForExtraneousText(args, argMultimap);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_COURSEID, PREFIX_TGROUP);
        checkForMissingValues(argMultimap);
        checkForUnexpectedTrailingText(argMultimap);
        Optional<CourseId> courseId = argMultimap.getValue(PREFIX_COURSEID).isPresent()
                ? Optional.of(ParserUtil.parseCourseId(argMultimap.getValue(PREFIX_COURSEID).get()))
                : Optional.empty();

        Optional<TGroup> tGroup = argMultimap.getValue(PREFIX_TGROUP).isPresent()
                ? Optional.of(ParserUtil.parseTGroup(argMultimap.getValue(PREFIX_TGROUP).get()))
                : Optional.empty();

        checkAtLeastOneFilter(courseId, tGroup);

        return new FilterCommand(new FilterMatchesPredicate(courseId, tGroup));
    }

    /**
     * Checks for extraneous text and invalid prefixes in the input and throws
     * ParseException if found.
     */
    private void checkForExtraneousText(String args, ArgumentMultimap argMultimap) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return;
        }

        String[] parts = trimmedArgs.split("\\s+");
        for (String part : parts) {
            // Catches unknown prefixes like mod/CS2103T, group/T01, x/abc
            if (part.contains("/") && !part.startsWith("crs/") && !part.startsWith("tg/")) {
                throw new ParseException("Invalid prefix in filter command: " + part
                        + "\nAllowed prefixes are: crs/ and tg/\n" + FilterCommand.MESSAGE_USAGE);
            }
        }

        String preamble = argMultimap.getPreamble().trim();
        if (!preamble.isEmpty()) {
            throw new ParseException("Unexpected text before prefixes: \"" + preamble
                    + "\"\nOnly these prefixes are allowed: crs/ and tg/\n" + FilterCommand.MESSAGE_USAGE);
        }
    }

    /**
     * Checks for missing values after prefixes and throws ParseException if found.
     */
    private void checkForMissingValues(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getValue(PREFIX_COURSEID).isPresent()
                && argMultimap.getValue(PREFIX_COURSEID).get().trim().isEmpty()) {
            throw new ParseException("Missing value for prefix: crs/"
                    + "\nCourse ID cannot be empty.\n" + FilterCommand.MESSAGE_USAGE);
        }

        if (argMultimap.getValue(PREFIX_TGROUP).isPresent()
                && argMultimap.getValue(PREFIX_TGROUP).get().trim().isEmpty()) {
            throw new ParseException("Missing value for prefix: tg/"
                    + "\nTutorial ID cannot be empty.\n" + FilterCommand.MESSAGE_USAGE);
        }
    }

    /**
     * Checks that at least one filter is provided.
     */
    private void checkAtLeastOneFilter(Optional<CourseId> courseId, Optional<TGroup> tGroup)
            throws ParseException {
        if (courseId.isEmpty() && tGroup.isEmpty()) {
            throw new ParseException("Invalid command format! At least one filter must be provided.\n"
                    + FilterCommand.MESSAGE_USAGE);
        }
    }
    private void checkForUnexpectedTrailingText(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getValue(PREFIX_COURSEID).isPresent()) {
            String courseIdValue = argMultimap.getValue(PREFIX_COURSEID).get().trim();
            if (courseIdValue.contains(" ")) {
                throw new ParseException("Unexpected text after course ID.\n" + FilterCommand.MESSAGE_USAGE);
            }
        }

        if (argMultimap.getValue(PREFIX_TGROUP).isPresent()) {
            String tGroupValue = argMultimap.getValue(PREFIX_TGROUP).get().trim();
            if (tGroupValue.contains(" ")) {
                throw new ParseException("Unexpected text after tutorial group.\n" + FilterCommand.MESSAGE_USAGE);
            }
        }
    }
}
