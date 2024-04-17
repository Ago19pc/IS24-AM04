package Server.Messages;

public class EndGamePhaseMessage implements GeneralMessage {

        @Override
        public void printData() {
            System.out.println("End Game Phase");
        }

        public boolean equals(GeneralMessage other){
            System.out.println("EndGamePhaseMessage equals still to be implemented.");
            return true;
        }


}
