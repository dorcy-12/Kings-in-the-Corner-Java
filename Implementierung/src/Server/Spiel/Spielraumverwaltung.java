package Server.Spiel;

import Server.Zugriffsverwaltung.Account;

import java.lang.reflect.Array;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Spielraumverwaltung extends Remote{

    void spielraum_erstellen(String spieler, String name, String passwort, String passwortw) throws Exception;

    void spieler_entfernen(String spieler, String spielraum) throws Exception;

    void spielraum_beitreten(String spieler,Boolean isLobby, String spielraum, String passwort) throws Exception;

    void bot_hinzufuegen(String name, String spielraum) throws Exception;

    void bot_entfernen(String spielraumname) throws Exception;

    void spielraum_verlassen(String name, String spielraum) throws Exception;

    ArrayList<String> getRaumSpieler(String raumname) throws Exception;

    void spiel_starten(String spielraum) throws Exception;

    void neue_spielraum_nachricht(String nachricht, String sender, String empfaenger) throws Exception;

    String getSpielraumChatverlauf(String empfaenger) throws Exception;

    Boolean isHost(String name, String spielraum) throws Exception;

    Boolean istInRaum(String spielraumname, String name) throws Exception;

    Boolean raumVoll(String spielraumname) throws Exception;

}
