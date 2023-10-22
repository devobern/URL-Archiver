package ch.bfh.ui;

import java.util.ResourceBundle;

public class ConsoleUI {
    public static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public static void printMessage(String key) {
        System.out.print(messages.getString(key));
    }

    public static void printFormattedMessage(String key, Object... args) {
        System.out.printf(messages.getString(key), args);
        System.out.println();
    }

    public static void printOptions(){
        System.out.println(messages.getString("instructions.option.title"));
        System.out.println(messages.getString("instructions.option.o"));
        System.out.println(messages.getString("instructions.option.a"));
        System.out.println(messages.getString("instructions.option.n"));
        System.out.println(messages.getString("instructions.option.q"));
        System.out.println(messages.getString("instructions.option.h"));
        System.out.println();
    }

    public static void printSeparator(){
        System.out.println("------------------------------------------------------------------");
    }

    public static void printWelcomeMessage() {
        // todo: Decide if ASCII Art is cool or not. Also consider bad looking if terminal is small
        System.out.println("██╗   ██╗██████╗ ██╗       █████╗ ██████╗  ██████╗██╗  ██╗██╗██╗   ██╗███████╗██████╗ ");
        System.out.println("██║   ██║██╔══██╗██║      ██╔══██╗██╔══██╗██╔════╝██║  ██║██║██║   ██║██╔════╝██╔══██╗");
        System.out.println("██║   ██║██████╔╝██║█████╗███████║██████╔╝██║     ███████║██║██║   ██║█████╗  ██████╔╝");
        System.out.println("██║   ██║██╔══██╗██║╚════╝██╔══██║██╔══██╗██║     ██╔══██║██║╚██╗ ██╔╝██╔══╝  ██╔══██╗");
        System.out.println("╚██████╔╝██║  ██║███████╗ ██║  ██║██║  ██║╚██████╗██║  ██║██║ ╚████╔╝ ███████╗██║  ██║");
        System.out.println(" ╚═════╝ ╚═╝  ╚═╝╚══════╝ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝");
        System.out.println("                            " + messages.getString("welcome.subtitle")                               );
        System.out.println();

        // Instructions
        System.out.println(messages.getString("welcome.welcome_text").replace("\\n", "\n"));
        System.out.println();
        printOptions();
    }


}