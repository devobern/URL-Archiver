package ch.bfh.controller;

import ch.bfh.view.ConsoleView;

import java.util.Scanner;

/**
 * Handles URL-related actions and interactions with the user via the {@code ConsoleUI}.
 * Responsible for managing the user's choices regarding URL actions such as open, archive, next, etc.
 */
public class UrlActionController {

    private final ConsoleView consoleView;
    private String archivedURL;

    /**
     * Initializes a new instance of the {@code UrlActionHandler} class.
     *
     * @param consoleView The user interface for providing feedback and messages.
     */
    public UrlActionController(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }

    /**
     * Handles the URL actions based on user input and displays appropriate messages.
     */
    public void handleActions() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            // Mock method/logic to fetch the extracted URL. todo: Replace with actual method.
            String extractedURL = "www.example.ch";

            consoleView.printSeparator();
            consoleView.printFormattedMessage("info.extracted_url", extractedURL);
            if (archivedURL != null) {
                consoleView.printFormattedMessage("info.archived_url", archivedURL);
            }
            consoleView.printMessage("instructions.options.prompt");
            String choice = scanner.nextLine();

            // todo: ENUM verwenden damit auch die Optionen internationalisierbar sind
            switch (choice.toLowerCase()) {
                case "o":
                    handleOpen(extractedURL);
                    break;
                case "a":
                    handleArchive(extractedURL);
                    break;
                case "n":
                    handleNext();
                    break;
                case "h":
                    consoleView.printOptions();
                    break;
                case "q":
                    handleQuit();
                    running = false;
                    break;
                default:
                    consoleView.printFormattedMessage("action.invalid");
            }
        }
    }

    /**
     * Handles the user's choice to open a URL.
     *
     * @param url The URL to be opened.
     */
    private void handleOpen(String url) {
        // Logic to open URL
        String targetUrl = (archivedURL != null) ? archivedURL : url;
        consoleView.printFormattedMessage("action.opening", targetUrl);
    }

    /**
     * Handles the user's choice to archive a URL.
     *
     * @param url The URL to be archived.
     */
    private void handleArchive(String url) {
        consoleView.printFormattedMessage("action.archiving", url);
        archivedURL = "www.archived-example.ch"; // Mock; todo: replace with actual logic.
    }

    /**
     * Handles the user's choice to move to the next URL.
     */
    private void handleNext() {
        archivedURL = null;
        consoleView.printFormattedMessage("action.next");
    }

    /**
     * Handles the user's choice to quit the application.
     */
    private void handleQuit() {
        consoleView.printMessage("action.quit");
    }
}