package ch.bfh;

import ch.bfh.handler.FileInputHandler;
import ch.bfh.ui.ConsoleUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConsoleUI.printWelcomeMessage();
        FileInputHandler fileInputHandler = new FileInputHandler();

        // If a command line argument is provided, process it; otherwise, prompt the user for input.
        if (args.length > 0) {
            fileInputHandler.processCommandLineInput(args[0]);
        } else {
            fileInputHandler.promptUserForInput();
        }

        Scanner scanner = new Scanner(System.in);
        String archivedURL = null;
        String choice;
        do {
            // Mock method/logic to fetch the extracted URL. todo: Replace with actual method.
            String extractedURL = "www.example.ch";

            ConsoleUI.printSeparator();
            ConsoleUI.printFormattedMessage("info.extracted_url", extractedURL);
            if(archivedURL != null){
                ConsoleUI.printFormattedMessage("info.archived_url", archivedURL);
            }
            ConsoleUI.printMessage("instructions.options.prompt");
            choice = scanner.nextLine();

            switch (choice.toLowerCase()) {
                case "o":
                    // todo: Logic to open URL
                    if(archivedURL != null){
                        ConsoleUI.printFormattedMessage("action.opening", archivedURL);
                    } else {
                        ConsoleUI.printFormattedMessage("action.opening", extractedURL);
                    }
                    break;

                case "a":
                    ConsoleUI.printFormattedMessage("action.archiving", extractedURL);
                    // todo: Logic to archive URL
                    archivedURL = "www.archived-example.ch";
                    break;

                case "n":
                    // Skip to next URL; todo: can be combined with fetch logic if needed
                    archivedURL = null;
                    ConsoleUI.printFormattedMessage("action.next");
                    break;

                case "h":
                    ConsoleUI.printOptions();
                    break;

                case "q":
                    ConsoleUI.printMessage("action.quit");
                    System.exit(0);
                    break;

                default:
                    ConsoleUI.printFormattedMessage("action.invalid");
            }

        } while (true);
    }


}