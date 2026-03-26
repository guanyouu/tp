package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedInput = args.trim();

        if (trimmedInput.isEmpty()) {
            throw new ParseException(DeleteCommand.MESSAGE_EMPTY_INPUT
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STUDENTID, PREFIX_COURSEID, PREFIX_TGROUP);

        boolean hasStudentId = argMultimap.getValue(PREFIX_STUDENTID).isPresent();
        boolean hasCourseId = argMultimap.getValue(PREFIX_COURSEID).isPresent();
        boolean hasTGroup = argMultimap.getValue(PREFIX_TGROUP).isPresent();

        boolean isIdentityMode = hasStudentId || hasCourseId || hasTGroup;

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_COURSEID, PREFIX_TGROUP);

        if (isIdentityMode) {
            if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_COURSEID, PREFIX_TGROUP)
                    || !argMultimap.getPreamble().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }

            StudentId studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get());
            CourseId courseId = ParserUtil.parseCourseId(argMultimap.getValue(PREFIX_COURSEID).get());
            TGroup tGroup = ParserUtil.parseTGroup(argMultimap.getValue(PREFIX_TGROUP).get());

            return new DeleteCommand(studentId, courseId, tGroup);
        } else {
            if (trimmedInput.matches("[1-9]\\d*\\s+.+")) {
                throw new ParseException(DeleteCommand.MESSAGE_UNEXPECTED_TEXT_AFTER_INDEX
                        + "\n" + DeleteCommand.MESSAGE_USAGE);
            }

            if (trimmedInput.matches("[1-9]\\d*")) {
                Index index = ParserUtil.parseIndex(trimmedInput);
                return new DeleteCommand(index);
            }

            if (trimmedInput.matches("-?\\d+(\\.\\d+)?")) {
                throw new ParseException(DeleteCommand.MESSAGE_INVALID_INDEX
                        + "\n" + DeleteCommand.MESSAGE_USAGE);
            }

            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Returns true if all the specified prefixes are present in the argument multimap.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        for (Prefix prefix : prefixes) {
            if (argumentMultimap.getValue(prefix).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
