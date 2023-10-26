package ch.bfh;

import ch.bfh.controller.CLIController;

import ch.bfh.helper.URLArchiver;
import ch.bfh.helper.URLExtractor;
import ch.bfh.model.URLArchiverModel;
import ch.bfh.view.ConsoleView;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale locale = Locale.forLanguageTag("en-US");
        URLArchiverModel model = new URLArchiverModel();
        ConsoleView consoleView = new ConsoleView(locale);
        URLExtractor extractor = new URLExtractor();
        URLArchiver archiver = new URLArchiver();



        CLIController cliController = new CLIController(model, consoleView, extractor, archiver);
        cliController.start(args);
    }
}