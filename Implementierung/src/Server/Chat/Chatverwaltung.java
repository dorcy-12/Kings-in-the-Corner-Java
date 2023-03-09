package Server.Chat;

import java.rmi.Remote;

public interface Chatverwaltung extends Remote{

    void neue_nachricht(String nachricht, String sender) throws Exception;

    String getChatverlauf() throws Exception;
}
