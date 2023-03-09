package Server.Spiel;

import java.rmi.Remote;
import java.util.ArrayList;

public interface Leaderboard extends Remote{
    void neues_punktekonto(String name) throws Exception;

    void rename_punktekonto(String name, String neuer_name) throws Exception;

    void punktekonto_loeschen(String name) throws Exception;

    ArrayList<Integer> get_punktekonto(String name) throws Exception;

    String top_10_ausgeben() throws Exception;

    void bestenliste_anpassen(ArrayList<Spieler> spieler) throws Exception;
}
