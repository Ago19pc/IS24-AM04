package Server.Messages;

import java.io.Serializable;

public interface GeneralMessage extends Serializable {
    public abstract void printData();

    public boolean equals(GeneralMessage other);

    public void serverExecute();
    public void clientExecute();



}
