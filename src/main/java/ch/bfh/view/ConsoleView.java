package ch.bfh.view;

import ch.bfh.helper.I18n;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Console-based view to interact with the user.
 */
public class ConsoleView {
    private final ResourceBundle messages;

    private boolean isArchiving = false;

    private final String OPTIONS = "(o/a/n/q/h)";

    /**
     * Initializes the console view with the specified locale.
     *
     * @param locale the locale for internationalization.
     */
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
        System.out.println(messages.getString("option.title"));
        System.out.println("[o]\t" + messages.getString("option.open"));
        System.out.println("[a]\t" + messages.getString("option.archive"));
        System.out.println("[n]\t" + messages.getString("option.next"));
        System.out.println("[q]\t" + messages.getString("option.quit"));
        System.out.println("[h]\t" + messages.getString("option.help"));
        System.out.println();
    }

    /**
     * Prompts the user to select an option from the given list.
     */
    public void promptUserForOption(){
        System.out.print(messages.getString("options.prompt") + " " + OPTIONS + ":");
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
        System.out.print(messages.getString("path.prompt"));
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
        System.out.println("\nArchiving complete");
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
