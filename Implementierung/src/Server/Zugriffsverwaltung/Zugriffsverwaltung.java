package Server.Zugriffsverwaltung;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Zugriffsverwaltung extends Remote {
    
    void spieler_anmelden(String benutzername, String passwort) throws Exception;

    void spieler_registrieren(String benutzername, String passwort, String passwortw) throws Exception;

    void spieler_bearbeiten(String benutzername, String neuer_benutzername, String passwort, String passwortw) throws Exception;

    void spieler_loeschen(String benutzername, String passwort) throws Exception;

    void spieler_abmelden(String benutzername) throws RemoteException;

    void lobby_entfernen(String benutzername) throws Exception;

    void lobby_hinzufuegen(String benutzername) throws Exception;

    Boolean is_lobby(String benutzername) throws Exception;

}
