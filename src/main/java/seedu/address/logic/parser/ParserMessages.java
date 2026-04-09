package seedu.address.logic.parser;

/**
 * Centralized messages and small helpers for parser-related user-visible error messages.
 * Parsers should call the helper methods here to produce consistent wording and include
 * the command usage when appropriate.
 */
public final class ParserMessages {
    public static final String MESSAGE_UNEXPECTED_PREAMBLE =
            "Unexpected text before prefixes.";
    private static final String GENERIC_MISSING_PREFIX_VALUE_FORMAT = "Missing value for prefix: %s\n%s\n%s";

    private static final String GENERIC_INVALID_PREFIX_FORMAT =
            "Invalid prefix in command. Allowed prefixes are: %s\n%s";

    private ParserMessages() {}

    /**
     * Produces an 'unexpected preamble' message that includes the command usage.
     */
    public static String unexpectedPreamble(String commandUsage) {
        return MESSAGE_UNEXPECTED_PREAMBLE + "\n" + commandUsage;
    }

    /**
     * Produces a standardized missing-prefix-value message.
     * @param prefix the prefix string (e.g., "crs/")
     * @param detail short detail to explain the value requirement (e.g., "Course ID cannot be empty.")
     * @param commandUsage usage to append for context
     */
    public static String missingPrefixValue(String prefix, String detail, String commandUsage) {
        return String.format(GENERIC_MISSING_PREFIX_VALUE_FORMAT, prefix, detail, commandUsage);
    }

    /**
     * Produces an invalid-prefix message listing allowed prefixes and appending usage.
     * @param allowedPrefixes human-readable allowed prefixes (e.g. "crs/, tg/, p/, and abs./")
     * @param commandUsage command usage to append
     */
    public static String invalidPrefix(String allowedPrefixes, String commandUsage) {
        return String.format(GENERIC_INVALID_PREFIX_FORMAT, allowedPrefixes, commandUsage);
    }
}

