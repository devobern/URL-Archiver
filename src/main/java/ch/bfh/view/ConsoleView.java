package ch.bfh.view;

import ch.bfh.helper.I18n;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ConsoleView {
    private final ResourceBundle messages;

    public ConsoleView(Locale locale) {
        this.messages = I18n.getResourceBundle(locale);
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
     * Prompts the user to provide a file or directory path using the console.
     *
     * @return The path entered by the user.
     */
    public String promptUserForPath() {
        System.out.println(messages.getString("path.prompt"));
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Prints a welcome message and ASCII Art to the console.
     * Also outputs instructions and options available to the user.
     */
    public void printWelcomeMessage() {
        System.out.println("""
                ██╗   ██╗██████╗ ██╗       █████╗ ██████╗  ██████╗██╗  ██╗██╗██╗   ██╗███████╗██████╗ 
                ██║   ██║██╔══██╗██║      ██╔══██╗██╔══██╗██╔════╝██║  ██║██║██║   ██║██╔════╝██╔══██╗
                ██║   ██║██████╔╝██║█████╗███████║██████╔╝██║     ███████║██║██║   ██║█████╗  ██████╔╝
                ██║   ██║██╔══██╗██║╚════╝██╔══██║██╔══██╗██║     ██╔══██║██║╚██╗ ██╔╝██╔══╝  ██╔══██╗
                ╚██████╔╝██║  ██║███████╗ ██║  ██║██║  ██║╚██████╗██║  ██║██║ ╚████╔╝ ███████╗██║  ██║
                 ╚═════╝ ╚═╝  ╚═╝╚══════╝ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝
                \t\t\t\t\t\t\t""" + messages.getString("welcome.subtitle"));
        // Instructions
        System.out.println(messages.getString("welcome.welcome_text").replace("\\n", "\n"));
        System.out.println();
        printOptions();
    }
}
