package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewCommand object.
 */
public class ViewCommandParser implements Parser<ViewCommand> {

    @Override
    public ViewCommand parse(String args) throws ParseException {
        Index index = ParserUtil.parseIndex(args, ViewCommand.MESSAGE_USAGE);
        return new ViewCommand(index);
    }
}
