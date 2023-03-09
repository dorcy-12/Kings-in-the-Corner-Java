package Server.Spiel;

import java.rmi.Remote;
import java.util.ArrayList;

public interface Spielverwaltung extends Remote{
    public void spielStarten(String spielraumname, ArrayList<String> spieler) throws Exception;

    public void karteZiehen(String spielraumname, String name) throws Exception;

    public void karteSpielen(String spielraumname, String name, String karte, String stapel) throws Exception;

    public void stapelVerschieben(String spielraumname, String ausgangsstapel, String zielstapel) throws Exception;

    public boolean amZug(String spielraumname, String name) throws Exception;

    public Boolean zahleChip(String spielraumname, String name, Integer anzahlCoins) throws  Exception;


    public void handkartenSortieren(String spielraumname, String name, Boolean filter) throws Exception;

    void neue_spiel_nachricht(String nachricht, String sender, String empfaenger) throws Exception;

    String getSpielChatverlauf(String empfaenger) throws Exception;

    void spielVerlassen(String spielraumname, String name) throws  Exception;

    ArrayList<String> getHandkarten(String spielraumname, String name) throws Exception;

    ArrayList<ArrayList<String>> getStapel(String spielraumname) throws Exception;

    Integer getChips(String spielraumname, String name) throws Exception;

    Integer getChipsImTopf(String spielraumname) throws Exception;

    Integer getPunkte(String spielraumname, String name) throws Exception;

    String getPunkteAlle(String spielraumname) throws Exception;

    Boolean spielGestartet(String spielraumname) throws Exception;

    Boolean spielVorbei(String spielraumname) throws Exception;

    Boolean nachziehstapelLeer(String spielraumname) throws  Exception;

    ArrayList<Spieler> getSpielSpieler(String spielraumname) throws Exception;

    void spielLoeschen(String spielraumname) throws Exception;

    void setSpielVorbei(String spielraumname) throws Exception;

}
