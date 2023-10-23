package ch.bfh.handler;

import ch.bfh.ui.ConsoleUI;

import java.util.Scanner;

/**
 * Responsible for handling user input interactions, specifically related to obtaining paths from users.
 * Leverages the {@code ConsoleUI} for user interactions.
 */
public class UserInputHandler {

    private final ConsoleUI consoleUI;

    /**
     * Initializes a new instance of the {@code UserInputHandler} class.
     *
     * @param consoleUI The user interface for providing feedback and messages.
     */
    public UserInputHandler(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    /**
     * Prompts the user to provide a file or directory path using the console.
     *
     * @return The path entered by the user.
     */
    public String promptUserForPath() {
        consoleUI.printMessage("path.prompt");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}