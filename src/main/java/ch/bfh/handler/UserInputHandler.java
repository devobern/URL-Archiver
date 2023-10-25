package ch.bfh.handler;

import ch.bfh.view.ConsoleView;

import java.util.Scanner;

/**
 * Responsible for handling user input interactions, specifically related to obtaining paths from users.
 * Leverages the {@code ConsoleUI} for user interactions.
 */
public class UserInputHandler {

    private final ConsoleView consoleView;

    /**
     * Initializes a new instance of the {@code UserInputHandler} class.
     *
     * @param consoleView The user interface for providing feedback and messages.
     */
    public UserInputHandler(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }

    /**
     * Prompts the user to provide a file or directory path using the console.
     *
     * @return The path entered by the user.
     */
    public String promptUserForPath() {
        consoleView.printMessage("path.prompt");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}