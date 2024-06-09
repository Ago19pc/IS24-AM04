package theMain;

import Server.Main;
import run.MainCLI;
import run.MainGUI;

import java.rmi.RemoteException;
import java.util.Scanner;



public class main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("----------[WELCOME TO CODEX NATURALIS]----------");
        System.out.println("[CODEXBOT]: Hi, what would you like to be? Host or Client?");
        procedure();
    }


    private static void procedure() {
        System.out.println("[CODEXBOT]: Type 'host' to be the host or 'client' to be the client.");
        System.out.println("[CODEXBOT]: Type 'gclient' to be the client with GUI or 'tclient' to be the client with TUI.");
        System.out.println("[CODEXBOT]: Type 'exit' to exit the game.");
        System.out.print("> ");
        String input = scanner.nextLine();
        if (input.equals("host")) {
            Main.main();
        } else if (input.equals("client")) {
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
        } else if (input.equals("exit")) {
            System.out.println("[CODEXBOT]: Goodbye!");
            System.exit(0);
        } else if (input.equals("gclient")){
            MainGUI.main();
        } else if (input.equals("tclient")) {
            try {
                MainCLI.main();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else

        {
            System.out.println("[CODEXBOT]: Invalid input. Please try again.");
            procedure();

        }
    }

}