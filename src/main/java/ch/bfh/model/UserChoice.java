package ch.bfh.model;

public enum UserChoice {
    OPEN("o", "option.open"),
    ARCHIVE("a", "option.archive"),
    NEXT("n", "option.next"),
    HELP("h", "option.help"),
    QUIT("q", "option.quit");

    private final String command;
    private final String i18nKey;

    UserChoice(String command, String i18nKey) {
        this.command = command;
        this.i18nKey = i18nKey;
    }

    public String getCommand() {
        return command;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    public static UserChoice fromCommand(String command) {
        for (UserChoice choice : values()) {
            if (choice.getCommand().equalsIgnoreCase(command)) {
                return choice;
            }
        }
        return null;
    }
}
