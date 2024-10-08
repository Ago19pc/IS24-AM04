package Client.View;

import Client.Controller.ClientController;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Enums.Face;

import java.util.Objects;

/**
 * This class represents the state of the CLI when the game is ongoing
 */
public class GameState implements CLIState{
    private final CLI cli;
    private final ClientController controller;

    /**
     * Constructor for the GameState. Welcomes the user to the game telling him about the help command
     * @param cli the CLI
     * @param controller the client controller
     */
    public GameState(CLI cli, ClientController controller){
        this.cli = cli;
        this.controller = controller;
        cli.printOnNewLine("Partita iniziata. Digita <help> per visualizzare i comandi disponibili.");
        cli.printPromptLine();
    }

    @Override
    public void decode(String[] args){
        switch(args[0]){
            case "help":
                cli.printOnNewLine("-------------------");
                cli.printOnNewLine(" Lista dei comandi");
                cli.printOnNewLine("-------------------");
                cli.printOnNewLine("");
                cli.printOnNewLine("  help: mostra la lista dei comandi");
                cli.printOnNewLine("");
                cli.printOnNewLine("  chat <messaggio>: invia un messaggio nella chat");
                cli.printOnNewLine("");
                cli.printOnNewLine("  chooseCard <numero>: scegli il numero della carta da selezizonare. Se vuoi la prima carta digita 1, etc.");
                cli.printOnNewLine("");
                cli.printOnNewLine("  hand: mostra le carte nella tua mano");
                cli.printOnNewLine("");
                cli.printOnNewLine("  placeCard <faccia> <x> <y>: piazza la carta selezionata nella posizione x, y");
                cli.printOnNewLine("  Le facce disponibili sono: FRONT, BACK");
                cli.printOnNewLine("");
                cli.printOnNewLine("  drawCard <mazzo> (opzionale <posizione a terra>): pesca una carta dal mazzo specificato. Se <posizione a terra> è specificata, pesca la carta dalla posizione specificata, altrimenti pesca dal mazzo");
                cli.printOnNewLine("  I mazzi disponibili sono: GOLD, RESOURCE");
                cli.printOnNewLine("  Le posizioni a terra disponibili sono: 1, 2");
                cli.printOnNewLine("");
                cli.printOnNewLine("  manuscript (opzionale <giocatore>): Mostra il manoscritto di <giocatore>Se <giocatore> non è specificato, mostra il tuo manoscritto");
                cli.printOnNewLine("");
                cli.printOnNewLine("  showCard <x> <y> (opzionale <giocatore>): Mostra la carta in posizione <x> <y> del manoscritto di <giocatore>, partendo dalla posizione 0,0. Se <giocatore> non è specificato, mostra le tue carte");
                cli.printOnNewLine("");
                cli.printOnNewLine("");
                cli.printOnNewLine("  achievements: Mostra gli obiettivi comuni e il tuo obiettivo segreto");
                cli.printOnNewLine("");
                cli.printPromptLine();
                break;
            case "chat":
                StringBuilder message = new StringBuilder();
                for(int i = 1; i < args.length; i++){
                    message.append(args[i]).append(" ");
                }
                controller.sendChatMessage(message.toString());
                break;
            case "chooseCard":
                if(args.length != 2){
                    cli.printOnNewLine("Utilizzo corretto: chooseCard <numero>");
                    return;
                }
                int cardNumber = Integer.parseInt(args[1]) - 1;
                if (Objects.requireNonNull(controller.getGameState()) == Server.Enums.GameState.PLACE_CARD) {
                    controller.setChosenHandCard(cardNumber);
                    cli.printOnNewLine("Carta selezionata: " + controller.getHand().get(cardNumber));
                } else {
                    cli.printOnNewLine("C'è un tempo e un luogo per ogni cosa! Ma non ora...");
                }
                break;
            case "hand":
                cli.displayHand();
                break;
            case "placeCard":
                if(args.length != 4){
                    cli.printOnNewLine("Utilizzo corretto: placeCard <faccia> <x> <y>");
                    return;
                }
                if(controller.getChosenHandCard() == null){
                    cli.printOnNewLine("Devi selezionare una carta della tua mano");
                    cli.printPromptLine();
                    return;
                }
                if(!args[1].equalsIgnoreCase("FRONT") && !args[1].equalsIgnoreCase("BACK")){
                    cli.printOnNewLine("Faccia non valida. Le facce disponibili sono: FRONT, BACK");
                    return;
                }
                int x = Integer.parseInt(args[2]);
                int y = Integer.parseInt(args[3]);
                Face face = Face.valueOf(args[1].toUpperCase());
                controller.askPlayCard(controller.getChosenHandCard(), face, x, y);
                break;
            case "drawCard":
                if(args.length < 2 || args.length > 3){
                    cli.printOnNewLine("Utilizzo corretto: drawCard <mazzo> (opzionale <posizione a terra>)");
                    return;
                }
                DeckPosition position = DeckPosition.DECK;
                if(args.length == 3){
                    if(args[2].equals("1")){
                        position = DeckPosition.FIRST_CARD;
                    } else if(args[2].equals("2")){
                        position = DeckPosition.SECOND_CARD;
                    } else {
                        cli.printOnNewLine("Posizione non valida. Le posizioni a terra disponibili sono: 1, 2");
                        return;
                    }
                }
                if(!args[1].equalsIgnoreCase("GOLD") && !args[1].equalsIgnoreCase("RESOURCE")){
                    cli.printOnNewLine("Mazzo non valido. I mazzi disponibili sono: GOLD, RESOURCE");
                    return;
                }
                Decks deck = Decks.valueOf(args[1].toUpperCase());
                controller.askDrawCard(deck, position);
                break;
            case "manuscript":
                if(args.length > 2){
                    cli.printOnNewLine("Utilizzo corretto: manuscript (opzionale <giocatore>)");
                    return;
                }
                if(args.length == 1){
                    cli.displayManuscript(controller.getMyName());
                } else {
                    cli.displayManuscript(args[1]);
                }
                break;
            case "showCard":
                if(args.length < 3 || args.length > 4){
                    cli.printOnNewLine("Utilizzo corretto: showCard <x> <y> (opzionale <giocatore>)");
                    return;
                }
                int xCoord = Integer.parseInt(args[1]);
                int yCoord = Integer.parseInt(args[2]);
                if(args.length == 3){
                    cli.displayCard(controller.getMyName(), xCoord, yCoord);
                } else {
                    cli.displayCard(args[3], xCoord, yCoord);
                }
                break;
            case "achievements":
                if(args.length != 1){
                    cli.printOnNewLine("Utilizzo corretto: achievements");
                    return;
                }
                cli.displayCommonAchievements();
                cli.displaySecretAchievement();
                cli.printPromptLine();
                break;
            default:
                cli.printOnNewLine("Comando non valido. Digita <help> per visualizzare i comandi disponibili.");
                cli.printPromptLine();
        }

    }
}
