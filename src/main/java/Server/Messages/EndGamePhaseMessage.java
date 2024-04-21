package Server.Messages;

import java.io.Serializable;

public class EndGamePhaseMessage implements Serializable, GeneralMessage {

        @Override
        public void printData() {
            System.out.println("End Game Phase");
        }

        public boolean equals(GeneralMessage other){
            System.out.println("EndGamePhaseMessage equals still to be implemented.");
            return true;
        }


}
