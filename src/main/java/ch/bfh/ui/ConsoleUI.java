package ch.bfh.ui;

import java.util.ResourceBundle;

/**
 * Provides a console-based user interface for displaying messages and options.
 * This class is responsible for outputting predefined messages and options to the console.
 */
public class ConsoleUI {

    /** Resource bundle containing message strings. */
    private final ResourceBundle messages;

    /**
     * Constructs a new ConsoleUI with the provided resource bundle.
     *
     * @param messages the resource bundle containing message strings.
     */
    public ConsoleUI(ResourceBundle messages) {
        this.messages = messages;
    }

    /**
     * Prints a message to the console using the provided key.
     *
     * @param key the key used to retrieve the message from the resource bundle.
     */
    public void printMessage(String key) {
        System.out.print(messages.getString(key));
    }

    /**
     * Prints a formatted message to the console using the provided key and arguments.
     *
     * @param key  the key used to retrieve the message from the resource bundle.
     * @param args arguments to format the message.
     */
    public void printFormattedMessage(String key, Object... args) {
        System.out.printf(messages.getString(key), args);
        System.out.println();
    }

    /**
     * Prints a list of user options to the console.
     */
    public void printOptions() {
        System.out.println(messages.getString("instructions.option.title"));
        System.out.println(messages.getString("instructions.option.o"));
        System.out.println(messages.getString("instructions.option.a"));
        System.out.println(messages.getString("instructions.option.n"));
        System.out.println(messages.getString("instructions.option.q"));
        System.out.println(messages.getString("instructions.option.h"));
        System.out.println();
    }

    /**
     * Prints a separator line to the console.
     */
    public void printSeparator() {
        System.out.println("------------------------------------------------------------------");
    }

    /**
     * Prints a welcome message and ASCII Art to the console.
     * Also outputs instructions and options available to the user.
     */
    public void printWelcomeMessage() {
        // todo: Decide if ASCII Art is cool or not. Also consider bad looking if terminal is small
        System.out.println("██╗   ██╗██████╗ ██╗       █████╗ ██████╗  ██████╗██╗  ██╗██╗██╗   ██╗███████╗██████╗ ");
        System.out.println("██║   ██║██╔══██╗██║      ██╔══██╗██╔══██╗██╔════╝██║  ██║██║██║   ██║██╔════╝██╔══██╗");
        System.out.println("██║   ██║██████╔╝██║█████╗███████║██████╔╝██║     ███████║██║██║   ██║█████╗  ██████╔╝");
        System.out.println("██║   ██║██╔══██╗██║╚════╝██╔══██║██╔══██╗██║     ██╔══██║██║╚██╗ ██╔╝██╔══╝  ██╔══██╗");
        System.out.println("╚██████╔╝██║  ██║███████╗ ██║  ██║██║  ██║╚██████╗██║  ██║██║ ╚████╔╝ ███████╗██║  ██║");
        System.out.println(" ╚═════╝ ╚═╝  ╚═╝╚══════╝ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝");
        System.out.println("                            " + messages.getString("welcome.subtitle")                               );
        System.out.println();

        // Instructions
        System.out.println(messages.getString("welcome.welcome_text").replace("\\n", "\n"));
        System.out.println();
        printOptions();
    }


}