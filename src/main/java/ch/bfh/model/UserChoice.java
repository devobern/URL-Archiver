package ch.bfh.model;


/**
 * Enumeration of possible user choices with corresponding command and internationalization keys.
 */
public enum UserChoice {
    OPEN("o", "option.open"),
    ARCHIVE("a", "option.archive"),
    NEXT("n", "option.next"),
    HELP("h", "option.help"),
    QUIT("q", "option.quit");

    private final String command;
    private final String i18nKey;

    /**
     * Constructs a UserChoice enum value with the given command and internationalization key.
     *
     * @param command the command representing the user choice
     * @param i18nKey the internationalization key associated with the choice
     */
    UserChoice(String command, String i18nKey) {
        this.command = command;
        this.i18nKey = i18nKey;
    }

    /**
     * Retrieves the command associated with the user choice.
     *
     * @return the command string
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the UserChoice enum value associated with the given command string.
     *
     * @param command the command string to look up
     * @return the corresponding UserChoice value or null if not found
     */
    public static UserChoice fromCommand(String command) {
        for (UserChoice choice : values()) {
            if (choice.getCommand().equalsIgnoreCase(command)) {
                return choice;
            }
        }
        return null;
    }
}
