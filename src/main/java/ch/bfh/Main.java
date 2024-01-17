package ch.bfh;

import ch.bfh.controller.CLIController;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        // Set locale
        Locale locale = Locale.forLanguageTag("en-US");

        // Create CLIController
        CLIController cliController = new CLIController(locale);

        // Start CLIController aka the program
        cliController.start(args);
    }
}