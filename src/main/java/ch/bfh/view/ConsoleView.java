package ch.bfh.view;

import ch.bfh.helper.I18n;

import java.util.Locale;
import java.util.Scanner;

/**
 * Console-based view to interact with the user.
 */
public class ConsoleView {
    private final static String OPTIONS = "(o/a/s/u/n/q/c/h)";
    private boolean isArchiving = false;

    /**
     * Initializes the console view with the specified locale.
     *
     * @param locale the locale for internationalization.
     */
    public ConsoleView(Locale locale) {
        I18n.getResourceBundle(locale);
    }

    /**
     * Prints a message to the console using the provided key.
     *
     * @param key the key used to retrieve the message from the resource bundle.
     */
    public void printMessage(String key) {
        System.out.print(I18n.getString(key));
    }

    /**
     * Prints the message of the provided exception to the console.
     * This method is used to display the message of an exception to the user,
     * which can be useful for debugging or informing the user of the nature of an error.
     *
     * @param exception the exception whose message is to be printed.
     */
    public void printMessage(Exception exception) {
        System.out.println(exception.getMessage());
    }


    /**
     * Prints a formatted message to the console using the provided key and arguments.
     *
     * @param key  the key used to retrieve the message from the resource bundle.
     * @param args arguments to format the message.
     */
    public void printFormattedMessage(String key, Object... args) {
        System.out.println(I18n.getString(key, args));
    }

    /**
     * Prints a list of user options to the console.
     */
    public void printOptions() {
        System.out.println(I18n.getString("option.title"));
        System.out.println("[o]\t" + I18n.getString("option.open"));
        System.out.println("[a]\t" + I18n.getString("option.archive"));
        System.out.println("[s]\t" + I18n.getString("option.show_archived"));
        System.out.println("[u]\t" + I18n.getString("option.update_jobs"));
        System.out.println("[n]\t" + I18n.getString("option.next"));
        System.out.println("[q]\t" + I18n.getString("option.quit"));
        System.out.println("[c]\t" + I18n.getString("option.config"));
        System.out.println("[h]\t" + I18n.getString("option.help"));
        System.out.println();
    }

    /**
     * Prompts the user to select an option from the given list.
     */
    public void promptUserForOption() {
        System.out.print(I18n.getString("options.prompt") + " " + OPTIONS + ":");
    }

    /**
     * Prints a separator line to the console.
     */
    public void printSeparator() {
        System.out.println("-".repeat(100) + "\n");
    }

    /**
     * Prompts the user to provide a file or directory path using the console.
     *
     * @return The path entered by the user.
     */
    public String promptUserForPath() {
        System.out.print(I18n.getString("path.prompt"));
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Starts the archiving indicator in a separate thread.
     * Displays a rotating symbol in the console to indicate that
     * the archiving process is ongoing. This method should be called
     * at the beginning of the archiving process.
     */
    public void startArchivingIndicator() {
        isArchiving = true;
        new Thread(() -> {
            String[] states = {"|", "/", "-", "\\"};
            int i = 0;
            while (isArchiving) {
                System.out.print("\rArchiving " + states[i++ % states.length]);
                try {
                    Thread.sleep(200);  // Update interval
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    /**
     * Stops the archiving indicator.
     * This method sets the flag to stop the rotating symbol in the console
     * and should be called when the archiving process is complete.
     */
    public void stopArchivingIndicator() {
        isArchiving = false;
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
                \t\t\t\t\t\t\t""" + I18n.getString("welcome.subtitle"));
        // Instructions
        System.out.println(I18n.getString("welcome.welcome_text").replace("\\n", "\n"));
        System.out.println();
        printOptions();
    }
}
