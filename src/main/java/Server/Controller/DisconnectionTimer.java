package Server.Controller;

import Server.Connections.GeneralServerConnectionHandler;
import Server.Exception.PlayerNotFoundByNameException;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is used to manage the timer for the disconnection of a player.
 */
public class DisconnectionTimer {
    /**
     * The timer
     */
    Timer t;

    /**
     * Manages the disconnection timer: every 0.1 seconds checks if the player is still disconnected, and after the given time removes the player from the game.
     * @param controller the controller of the server
     * @param connectionHandler the connection handler of the server
     * @param id the id of the disconnected player
     * @param time the time in seconds after which the player will be removed
     */
    public DisconnectionTimer(Controller controller, GeneralServerConnectionHandler connectionHandler, String id, int time) {
        t = new Timer();
        t.scheduleAtFixedRate(new CheckOnlineTask(connectionHandler, id), 0, 100);
        t.schedule(new DisconnectionTask(controller, connectionHandler, id), time * 1000);
    }

    /**
     * Task that checks if the player is still disconnected
     */
    class CheckOnlineTask extends TimerTask {
        private GeneralServerConnectionHandler connectionHandler;
        private String id;

        /**
         * Constructor
         * @param connectionHandler the connection handler
         * @param id the id of the player of which to check the online status
         */
        public CheckOnlineTask(GeneralServerConnectionHandler connectionHandler, String id) {
            this.connectionHandler = connectionHandler;
            this.id = id;
        }
        /**
         * If the client is no longer in the disconnected list, the timer is cancelled.
         */
        @Override
        public void run() {
            if(!connectionHandler.isInDisconnectedList(id)) {
                t.cancel();
            }
        }
    }

    /**
     * Task that removes the player from the game
     */
    class DisconnectionTask extends TimerTask {
        private Controller controller;
        private GeneralServerConnectionHandler connectionHandler;
        private String id;

        /**
         * Constructor
         * @param controller the controller
         * @param connectionHandler the connection handler
         * @param id the id of the player to remove
         */
        public DisconnectionTask(Controller controller, GeneralServerConnectionHandler connectionHandler, String id) {
            this.controller = controller;
            this.connectionHandler = connectionHandler;
            this.id = id;
        }

        /**
         * If the player is still disconnected, it is removed from the game. Also cancels the timer.
         */
        @Override
        public void run() {
            if(connectionHandler.isInDisconnectedList(id)) {
                try {
                    controller.removePlayer(controller.getPlayerByName(connectionHandler.getPlayerNameByID(id)));
                } catch (PlayerNotFoundByNameException e) {
                    e.printStackTrace();
                }
            }
            t.cancel();
        }
    }

}
