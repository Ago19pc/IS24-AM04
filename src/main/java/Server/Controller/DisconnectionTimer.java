package Server.Controller;

import Server.Connections.GeneralServerConnectionHandler;
import Server.Exception.PlayerNotFoundByNameException;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class DisconnectionTimer {
    Timer t;
    public DisconnectionTimer(Controller controller, GeneralServerConnectionHandler connectionHandler, String id, int time) {
        t = new Timer();
        t.scheduleAtFixedRate(new CheckOnlineTask(connectionHandler, id), 0, 100);
        t.schedule(new DisconnectionTask(controller, connectionHandler, id), time * 1000);
    }

    class CheckOnlineTask extends TimerTask {
        private GeneralServerConnectionHandler connectionHandler;
        private String id;
        public CheckOnlineTask(GeneralServerConnectionHandler connectionHandler, String id) {
            this.connectionHandler = connectionHandler;
            this.id = id;
        }
        @Override
        public void run() {
            if(!connectionHandler.isInDisconnectedList(id)) {
                t.cancel();
            }
        }
    }

    class DisconnectionTask extends TimerTask {
        private Controller controller;
        private GeneralServerConnectionHandler connectionHandler;
        private String id;
        public DisconnectionTask(Controller controller, GeneralServerConnectionHandler connectionHandler, String id) {
            this.controller = controller;
            this.connectionHandler = connectionHandler;
            this.id = id;
        }
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
