package theMain;

import java.util.Scanner;

public class main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("----------[WELCOME TO CODEX NATURALIS]----------");
        System.out.println("[CODEXBOT]: Hi, what would you like to be? Host or Client?");
        System.out.println("[CODEXBOT]: Type 'host' to be the host or 'client' to be the client.");
        System.out.println("[CODEXBOT]: Type 'exit' to exit the game.");
        System.out.print("> ");
        boolean isHost = false;
        String input = scanner.nextLine();
        if (input.equals("host")) { isHost = true;} else isHost = false;



    }
}
