package Client;

import javafx.beans.property.SimpleStringProperty;

public class ClientPunktekonto {
    private SimpleStringProperty spieler;
    private SimpleStringProperty gewonneneSpiele;
    private SimpleStringProperty gespielteSpiele;
    private SimpleStringProperty punkte;

    public ClientPunktekonto(String spieler, String gewonneneSpiele, String gespielteSpiele, String punkte) {
        this.spieler = new SimpleStringProperty(spieler);
        this.gewonneneSpiele = new SimpleStringProperty(gewonneneSpiele);
        this.gespielteSpiele = new SimpleStringProperty(gespielteSpiele);
        this.punkte = new SimpleStringProperty(punkte);
    }

    public String getSpieler() {
        return spieler.get();
    }

    public void setSpieler(String spieler) {
        this.spieler = new SimpleStringProperty(spieler);
    }

    public String getGewonneneSpiele() {
        return gewonneneSpiele.get();
    }

    public void setGewonneneSpiele(String gewonneneSpiele) {
        this.gewonneneSpiele = new SimpleStringProperty(gewonneneSpiele);
    }

    public String getGespielteSpiele() {
        return gespielteSpiele.get();
    }

    public void setGespielteSpiele(String gespielteSpiele) {
        this.gespielteSpiele = new SimpleStringProperty(gespielteSpiele);
    }

    public String getPunkte() {
        return punkte.get();
    }

    public void setPunkte(String punkte) {
        this.punkte = new SimpleStringProperty(punkte);
    }
}
