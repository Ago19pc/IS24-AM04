package theMain;

import Server.Main;
import Interface.MainCLI;
import Interface.MainGUI;

import java.rmi.RemoteException;
import java.util.Scanner;


/**
 * Main class of the game
 */
public class main {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Starts the game
     * @param args the arguments
     */
    public static void main(String[] args) {
        System.out.println("----------[WELCOME TO CODEX NATURALIS]----------");
        System.out.println("[CODEXBOT]: Hi, what would you like to be? Host or Client?");
        procedure();
    }

    /**
     * Asks the user if he wants to be the host or the client, then starts what he chose
     */
    private static void procedure() {
        System.out.println("[CODEXBOT]: Type 'host' to be the host or 'client' to be the client.");
        System.out.println("[CODEXBOT]: Type 'gclient' to be the client with GUI or 'tclient' to be the client with TUI.");
        System.out.println("[CODEXBOT]: Type 'exit' to exit the game.");
        System.out.print("> ");
        String input = scanner.nextLine();
        switch (input) {
            case "host" -> Main.main();
            case "client" -> {
                System.out.println("[CODEXBOT]: You are now a client.");
                System.out.println("[CODEXBOT]: Would you like to go GUI (g) or TUI (t)?");
                System.out.print("> ");
                String guiOrTui = scanner.nextLine();
                if (guiOrTui.equals("g")) {
                    MainGUI.main();
                } else {
                    try {
                        MainCLI.main();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case "exit" -> {
                System.out.println("[CODEXBOT]: Goodbye!");
                System.exit(0);
            }
            case "gclient" -> MainGUI.main();
            case "tclient" -> {
                try {
                    MainCLI.main();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> {
                System.out.println("[CODEXBOT]: Invalid input. Please try again.");
                procedure();
            }
        }
    }

}