package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TGROUP;

import java.util.Arrays;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_STUDENTID, PREFIX_EMAIL,
                        PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_TELE);

        Prefix[] allowedPrefixes = {
            PREFIX_NAME, PREFIX_STUDENTID, PREFIX_EMAIL,
            PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_TELE
        };
        String allowedReadable = "n/, id/, e/, crs/, tg/, tel/";

        Index index;

        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(pe.getMessage()));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_STUDENTID, PREFIX_EMAIL,
            PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_TELE);

        ParserValidators.ensureAtLeastOnePrefixPresent(argMultimap, allowedPrefixes, EditCommand.MESSAGE_USAGE);

        ParserValidators.checkForUnknownPrefixTokens(args, allowedPrefixes,
                allowedReadable, EditCommand.MESSAGE_USAGE);

        ParserValidators.checkForMissingValues(argMultimap,
                new Prefix[] {
                    PREFIX_NAME, PREFIX_STUDENTID, PREFIX_EMAIL, PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_TELE },
                new String[] {
                    "n/", "id/", "crs/", "tg/", "e/", "tel/" },
                new String[] {
                    "Name cannot be empty.", "Student ID cannot be empty.",
                    "Course ID cannot be empty.", "Tutorial group cannot be empty.",
                    "Email cannot be empty.", "Telephone cannot be empty." },
                EditCommand.MESSAGE_USAGE);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_STUDENTID).isPresent()) {
            editPersonDescriptor.setStudentId(ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_COURSEID).isPresent()) {
            editPersonDescriptor.setCourseId(ParserUtil.parseCourseId(argMultimap.getValue(PREFIX_COURSEID).get()));
        }
        if (argMultimap.getValue(PREFIX_TGROUP).isPresent()) {
            editPersonDescriptor.setTGroup(ParserUtil.parseTGroup(argMultimap.getValue(PREFIX_TGROUP).get()));
        }
        if (argMultimap.getValue(PREFIX_TELE).isPresent()) {
            editPersonDescriptor.setTele(ParserUtil.parseTele(argMultimap.getValue(PREFIX_TELE).get()));
        }



        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Validates the presence of at least one filter prefix and checks for unknown prefixes.
     */
    private void ensureAtLeastOneFieldPresent(ArgumentMultimap argMultimap) throws ParseException {
        Prefix[] allowedPrefixes = {
            PREFIX_NAME, PREFIX_STUDENTID, PREFIX_EMAIL,
            PREFIX_COURSEID, PREFIX_TGROUP, PREFIX_TELE
        };

        boolean anyPresent = Arrays.stream(allowedPrefixes)
                .anyMatch(prefix -> argMultimap.getValue(prefix).isPresent());
        if (!anyPresent) {
            throw new ParseException("At least one field to edit must be provided.\n" + EditCommand.MESSAGE_USAGE);
        }
    }

}
