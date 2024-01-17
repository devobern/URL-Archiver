package ch.bfh.model;


/**
 * Enumeration of possible user choices with corresponding command and internationalization keys.
 */
public enum UserChoice {
    OPEN("o"),
    ARCHIVE("a"),
    NEXT("n"),
    HELP("h"),
    CONFIG("c"),
    SHOW_ARCHIVED("s"),
    UPDATE_JOBS("u"),
    QUIT("q");

    private final String command;

    /**
     * Constructs a UserChoice enum value with the given command and internationalization key.
     *
     * @param command the command representing the user choice
     */
    UserChoice(String command) {
        this.command = command;
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

    /**
     * Retrieves the command associated with the user choice.
     *
     * @return the command string
     */
    public String getCommand() {
        return command;
    }
}
