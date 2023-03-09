package Server.Spiel;

/**
 * Die Klasse Punktekonto enthät als Attribute den Namen des jewiligen Spielers/Nutzers, die Anzahl der gewonnenen, gespielten Spiele des Spielers
 * und der Punkte des Spielers.
 * Die Klasse enthält entsprechende Getter, Setter und Konstruktor Methoden.
 */
public class Punktekonto {
    private String name;
    private int gespielte_spiele;
    private int gewonnene_spiele;
    private int punkte;

    public Punktekonto (String name, int gespielte_spiele, int gewonnene_spiele, int punkte) {
        this.name = name;
        this.gespielte_spiele = gespielte_spiele;
        this.gewonnene_spiele = gewonnene_spiele;
        this.punkte = punkte;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGespielte_spiele() {
        return gespielte_spiele;
    }

    public void setGespielte_spiele(int gespielte_spiele) {
        this.gespielte_spiele = gespielte_spiele;
    }

    public int getGewonnene_spiele() {
        return gewonnene_spiele;
    }

    public void setGewonnene_spiele(int gewonnene_spiele) {
        this.gewonnene_spiele = gewonnene_spiele;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }
}
