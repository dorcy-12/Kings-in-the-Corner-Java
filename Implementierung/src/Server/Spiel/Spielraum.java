package Server.Spiel;

import java.util.ArrayList;

/**
 * Die Klasse Spielraum hat als Attribute den Namen, das Passwort, den Host als String und die Spieler in einer Liste als Strings und den Spielraumchat als String.
 * Desweiteren enthält sie Getter, Setter und Konstruktor Methoden.
 */
public class Spielraum {
    private String name;
    private String passwort;
    private String host;
    private ArrayList<String> spieler;

    private String raumChat = "";

    public Spielraum(String name, String passwort, String spieler) {
        this.name = name;
        this.passwort = passwort;
        this.host = spieler;
        this.spieler = new ArrayList<>();
        this.spieler.add(spieler);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getPasswort() {
        return this.passwort;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public void setSpieler(ArrayList<String> spieler) {
        this.spieler = spieler;
    }

    public void addSpieler(String spieler) {
        this.spieler.add(spieler);
    }

    public void removeSpieler(String spieler) {
        this.spieler.remove(spieler);
    }

    public ArrayList<String> getSpieler() {
        return this.spieler;
    }

    public String getRaumChat() {
        return this.raumChat;
    }

    public void setRaumChat(String chat) {
        this.raumChat = chat;
    }
    
}
