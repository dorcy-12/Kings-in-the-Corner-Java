package Server.Spiel;

import java.io.Serializable;
import java.util.ArrayList;

public class Spieler implements Serializable {
    private String name;

    private ArrayList<String> handkarten;

    private Integer chips;

    private Integer punkte = 0;

    private Boolean amLeben = true;

    public Spieler(String name, ArrayList<String> handkarten, Integer chips) {
        this.name = name;
        this.handkarten = handkarten;
        this.chips = chips;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name =  name;
    }

    public ArrayList<String> getHandkarten() {
        return handkarten;
    }

    public void setHandkarten(ArrayList<String> handkarten) {
        this.handkarten = handkarten;
    }

    public void addHandkarte(String karte) {
        this.handkarten.add(karte);
    }

    public void removeHandkarte(String karte) {
        this.handkarten.remove(karte);
    }


    public Integer getChips() {
        return chips;
    }

    public void setChips(Integer chips) {
        this.chips = chips;
    }

    public Integer getPunkte() {
        return punkte;
    }

    public void setPunkte(Integer punkte) {
        this.punkte = punkte;
    }

    public Boolean getAmLeben() {
        return amLeben;
    }

    public void setAmLeben(Boolean amLeben) {
        this.amLeben = amLeben;
    }
}
