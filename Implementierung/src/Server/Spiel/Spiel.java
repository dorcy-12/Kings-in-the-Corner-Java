package Server.Spiel;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Spiel {

    private String spielraumname;
    private ArrayList<String> nachziehstapel;

    private Integer chipsImTopf = 0;

    private String spielChat = "";

    private Boolean rundeVorei = false;

    private Boolean spielVorbei = false;

    private ArrayList<String> StapelObenLinks = new ArrayList<String>();

    private ArrayList<String> StapelOben = new ArrayList<String>();

    private ArrayList<String> StapelObenRechts = new ArrayList<String>();

    private ArrayList<String> StapelMitteLinks = new ArrayList<String>();

    private ArrayList<String> StapelMitteRechts = new ArrayList<String>();

    private ArrayList<String> StapelUntenLinks = new ArrayList<String>();

    private ArrayList<String> StapelUnten = new ArrayList<String>();

    private ArrayList<String> StapelUntenRechts = new ArrayList<String>();

    private ArrayList<Spieler> Spieler = new ArrayList<Spieler>();

    public Spiel(String spielraumname, ArrayList<Spieler> spieler, ArrayList<String> nachziehstapel) {
        this.spielraumname = spielraumname;
        this.Spieler = spieler;
        this.nachziehstapel = nachziehstapel;
        StapelOben.add(nachziehstapel.get(0));
        StapelOben.add(nachziehstapel.get(0));
        nachziehstapel.remove(0);
        StapelMitteLinks.add(nachziehstapel.get(0));
        StapelMitteLinks.add(nachziehstapel.get(0));
        nachziehstapel.remove(0);
        StapelMitteRechts.add(nachziehstapel.get(0));
        StapelMitteRechts.add(nachziehstapel.get(0));
        nachziehstapel.remove(0);
        StapelUnten.add(nachziehstapel.get(0));
        StapelUnten.add(nachziehstapel.get(0));
        nachziehstapel.remove(0);
    }

    public String getSpielraumname() {
        return spielraumname;
    }

    public void setSpielraumname(String spielraumname) {
        this.spielraumname = spielraumname;
    }

    public ArrayList<String> getNachziehstapel() {
        return nachziehstapel;
    }

    public  void setNachziehstapel(ArrayList<String> nachziehstapel) {
        this.nachziehstapel = nachziehstapel;
    }

    public ArrayList<String> getStapelObenLinks() {
        return StapelObenLinks;
    }

    public void setStapelObenLinks(ArrayList<String> stapelObenLinks) {
        this.StapelObenLinks = stapelObenLinks;
    }

    public ArrayList<String> getStapelOben() {
        return StapelOben;
    }

    public  void setStapelOben(ArrayList<String> stapelOben) {
        this.StapelOben = stapelOben;
    }

    public ArrayList<String> getStapelObenRechts() {
        return StapelObenRechts;
    }

    public  void setStapelObenRechts(ArrayList<String> stapelObenRechts) {
        this.StapelObenRechts = stapelObenRechts;
    }

    public ArrayList<String> getStapelMitteLinks() {
        return StapelMitteLinks;
    }

    public  void setStapelMitteLinks(ArrayList<String> stapelMitteLinks) {
        this.StapelMitteLinks = stapelMitteLinks;
    }

    public ArrayList<String> getStapelMitteRechts() {
        return StapelMitteRechts;
    }

    public  void setStapelMitteRechts(ArrayList<String> stapelMitteRechts) {
        this.StapelMitteRechts = stapelMitteRechts;
    }

    public ArrayList<String> getStapelUntenLinks() {
        return StapelUntenLinks;
    }

    public  void setStapelUntenLinks(ArrayList<String> stapelUntenLinks) {
        this.StapelUntenLinks = stapelUntenLinks;
    }

    public ArrayList<String> getStapelUnten() {
        return StapelUnten;
    }

    public  void setStapelUnten(ArrayList<String> stapelUnten) {
        this.StapelUnten = stapelUnten;
    }

    public ArrayList<String> getStapelUntenRechts() {
        return StapelUntenRechts;
    }

    public  void setStapelUntenRechts(ArrayList<String> stapelUntenRechts) {
        this.StapelUntenRechts = stapelUntenRechts;
    }

    public ArrayList<Spieler> getSpieler() {
        return Spieler;
    }

    public  void setSpieler(ArrayList<Spieler> spieler) {
        this.Spieler = spieler;
    }

    public Integer getChipsImTopf() {
        return chipsImTopf;
    }

    public void setChipsImTopf(Integer chipsImTopf) {
        this.chipsImTopf = chipsImTopf;
    }

    public String getSpielChat() {
        return spielChat;
    }

    public void setSpielChat(String spielChat) {
        this.spielChat = spielChat;
    }

    public void stapelNeu(ArrayList<String> stapel) {
        this.nachziehstapel = stapel;
        StapelOben.clear();
        StapelOben.add(nachziehstapel.get(0));
        StapelOben.add(nachziehstapel.get(0));
        nachziehstapel.remove(0);
        StapelMitteLinks.clear();
        StapelMitteLinks.add(nachziehstapel.get(0));
        StapelMitteLinks.add(nachziehstapel.get(0));
        nachziehstapel.remove(0);
        StapelMitteRechts.clear();
        StapelMitteRechts.add(nachziehstapel.get(0));
        StapelMitteRechts.add(nachziehstapel.get(0));
        nachziehstapel.remove(0);
        StapelUnten.clear();
        StapelUnten.add(nachziehstapel.get(0));
        StapelUnten.add(nachziehstapel.get(0));
        nachziehstapel.remove(0);
        StapelObenLinks.clear();
        StapelObenRechts.clear();
        StapelUntenLinks.clear();
        StapelUntenRechts.clear();
    }

    public Boolean getRundeVorei() {
        return rundeVorei;
    }

    public void setRundeVorei(Boolean rundeVorei) {
        this.rundeVorei = rundeVorei;
    }

    public Boolean getSpielVorbei() {
        return spielVorbei;
    }

    public void setSpielVorbei(Boolean spielVorbei) {
        this.spielVorbei = spielVorbei;
    }

    public void shuffleStapel(ArrayList<String> karten) {
        this.nachziehstapel.addAll(karten);
        Collections.shuffle(this.nachziehstapel);
    }
}
