package ch.bfh;

import ch.bfh.handler.CommandLineHandler;
import ch.bfh.handler.FileInputHandler;
import ch.bfh.handler.UrlActionHandler;
import ch.bfh.ui.ConsoleUI;

import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) {
        ResourceBundle messages = ResourceBundle.getBundle("messages");
        ConsoleUI consoleUI = new ConsoleUI(messages);
        consoleUI.printWelcomeMessage();

        FileInputHandler fileInputHandler = new FileInputHandler(consoleUI);
        CommandLineHandler commandLineHandler = new CommandLineHandler(fileInputHandler);
        commandLineHandler.handleArgs(args);

        UrlActionHandler urlActionHandler = new UrlActionHandler(consoleUI);
        urlActionHandler.handleActions();
    }
}