package Server.Spiel;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Bot implements Runnable {
    private Thread t;
    private Registry registry;

    private Spielverwaltung spiel;

    private Spielraumverwaltung spielraum;

    private Leaderboard leaderboard;

    private String name;
    private String raumName;

    private ArrayList<String> handkarten;

    private Integer chips;

    private Boolean lebt = true;

    String koenig = null;


    private Boolean amZug = false;

    /**
     * Konstrukor des Bots erstellt einen neuen Bot
     * @param name Name des Bots
     * @param raumName Name des Raums in dem der Bot erzeugt wird
     * @param handkarten Handkarten, die der Bot hat
     * @param chips Anzahl CHips, die der Bot besitzt
     * @throws Exception
     */
    public Bot(String name, String raumName, ArrayList<String> handkarten, Integer chips) throws Exception {
        System.out.println("Created " + name + "in raum "+ raumName);
        t = new Thread(this, name);

        this.name = name;
        this.raumName = raumName;
        this.handkarten = handkarten;
        this.chips = chips;

        try {
            this.registry = LocateRegistry.getRegistry();
            this.spiel = (Spielverwaltung) registry.lookup("Spielverwaltung");
            this.spielraum = (Spielraumverwaltung) registry.lookup("Spielraumverwaltung");
            this.leaderboard = (Leaderboard) registry.lookup("Leaderboard");
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    /**
     * schaut ob auf der Hand sich Könige befinden und gibt dies zurueck falls diese existiernen, null sonst
     * @return einen koenig oder null
     */
    public String koenigAufHand() {
        for (String karte : handkarten) {
            if (karte.contains("König")) {
                return karte;
            }

        }
        return null;
    }

    /**
     * schaut ob man karte auf obKarte spielen kann
     * @param karte obere Karte
     * @param obKarte untere Karte
     * @return true falls karte auf ob Karte spielbar, false sonst
     */
    boolean karteSpielenMoeglich(String karte, String obKarte) {
        if (obKarte.contains("Herz") || obKarte.contains("Karo")) {
            if (!(karte.contains("Pik") || karte.contains("Kreuz"))) {
                return false;
            }
        } else {
            if (!(karte.contains("Herz") || karte.contains("Karo"))) {
                return false;
            }
        }

        if (obKarte.contains("Koenig")) {
            if (karte.contains("Dame")) {
                return true;
            }
        } else if (obKarte.contains("Dame")) {
            if (karte.contains("Bube")) {
                return true;
            }
        } else if (obKarte.contains("Bube")) {
            if (karte.contains("10")) {
                return true;
            }
        } else if (obKarte.contains("10")) {
            if (karte.contains("9")) {
                return true;
            }
        } else if (obKarte.contains("9")) {
            if (karte.contains("8")) {
                return true;
            }
        } else if (obKarte.contains("8")) {
            if (karte.contains("7")) {
                return true;
            }
        } else if (obKarte.contains("7")) {
            if (karte.contains("6")) {
                return true;
            }
        } else if (obKarte.contains("6")) {
            if (karte.contains("5")) {
                return true;
            }
        } else if (obKarte.contains("5")) {
            if (karte.contains("4")) {
                return true;
            }
        } else if (obKarte.contains("4")) {
            if (karte.contains("3")) {
                return true;
            }
        } else if (obKarte.contains("3")) {
            if (karte.contains("2")) {
                return true;
            }
        } else if (obKarte.contains("2")) {
            if (karte.contains("Ass")) {
                return true;
            }
        }
        return false;
    }

    /**
     * gibt für einen bestimmten index den Namen des Stapels zurueck
     * @param index index von dem Stapel
     * @return Name des Stapels an position index
     */
    public String getStapelName(int index){
        if (index == 0){
            return "obenLinks";
        } else if (index == 1) {
            return "oben";
        }else if (index == 2) {
            return "obenRechts";
        }else if (index == 3) {
            return "links";
        }else if (index == 4) {
            return "rechts";
        }else if (index == 5) {
            return "untenLinks";
        }else if (index == 6) {
            return "unten";
        }else if (index == 7) {
            return "untenRechts";
        }
        //Fehler zu viele Stapel
        return null;
    }

    /**
     * prüft ob der Stapel verschiebbar ist und verschiebt diesen dann
     * @param index index des zu veschiebenden Stapels
     * @param von Stapel der zu veschieben ist
     * @param alleStapel alle Stapel im spiel
     * @throws Exception falls spiel.stapelVerschieben eine Exception wirft
     */
    public void verschiebe(Integer index, ArrayList<String> von, ArrayList<ArrayList<String>> alleStapel) throws Exception {
        if (von.size() == 0) return;
        for (int i = 0; i < 8; i++) {
            if (!(alleStapel.get(i).size() == 0)) {
                String vonKarte = von.get(1);
                String nachKarte = alleStapel.get(i).get(0);
                if (karteSpielenMoeglich(vonKarte, nachKarte)) {
                    spiel.stapelVerschieben(raumName, getStapelName(index), getStapelName(i));
                }
            }
        }
    }

    /**
     * spielt die Karte karte falls diese spielbar ist
     * @param karte eine Handkarte
     * @param alleStapel alle sich im spiel befindenden Karten
     * @throws Exception falls spiel.karteSpielen eine Exception wirft
     */
    public void spieleKarte(String karte, ArrayList<ArrayList<String>> alleStapel) throws Exception {
        for (int i = 0; i < 8; i++) {
            if (alleStapel.get(i).size() == 0) {
                if (!(i == 0 || i == 2 || i == 5 || i == 7)) {
                    spiel.karteSpielen(raumName, name, karte, getStapelName(i));
                    return;
                }
            } else {
                String nachKarte = alleStapel.get(i).get(0);
                if (karteSpielenMoeglich(karte, nachKarte)) {
                    spiel.karteSpielen(raumName, name, karte, getStapelName(i));
                    return;
                }
            }
        }
    }

    /**
     * Main Methode des Bots
     *
     * Falls am Zug:
     *      1. Spiele alle Koenige von der Hand
     *      2. Verschiebe alle verschiebbaren Stapel
     *      3. Spiele alle spielbaren Karten
     *      4. ziehe eine Karte und beende den Zug
     * sonst:
     *      warte bis amZug
     *
     * Bot exsistiert nur solange dieser im Raum ist
     */
    @Override
    public synchronized void run() {
        while (lebt) {

            try {
                if (!spielraum.istInRaum(raumName,name)) {
                    leaderboard.punktekonto_loeschen(name);
                    lebt = false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                amZug = spiel.amZug(raumName, name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if(amZug){
                try {
                    handkarten = spiel.getHandkarten(raumName, name);
                    ArrayList<ArrayList<String>> alleStapel;

                    koenig = koenigAufHand();
                    alleStapel = spiel.getStapel(raumName);

                    System.out.println("spiele Könige");
                    while (koenig != null) {
                        // StapelObenLinks
                        if (alleStapel.get(0).size() == 0) {
                            spiel.karteSpielen(raumName, name, koenig, "obenLinks");

                            // StapelObenRechts
                        } else if ((alleStapel.get(2).size() == 0)) {
                            spiel.karteSpielen(raumName, name, koenig, "obenRechts");

                            // StapelUntenLinks
                        } else if ((alleStapel.get(5).size() == 0)) {
                            spiel.karteSpielen(raumName, name, koenig, "untenLinks");

                            // StapelUntenRechts
                        } else if ((alleStapel.get(7).size() == 0)) {
                            spiel.karteSpielen(raumName, name, koenig, "untenRechts");
                        }

                        handkarten = spiel.getHandkarten(raumName, name);
                        koenig = koenigAufHand();
                    }

                    System.out.println("verschiebe Karten");
                    for(int i = 0; i < 8; i++) {
                        if (!(i == 0 || i == 2 || i == 5 || i == 7)) {
                            alleStapel = spiel.getStapel(raumName);
                            verschiebe(i, alleStapel.get(i), alleStapel);
                        }
                    }

                    System.out.println("spiele Karten");

                    for (int i = 0; i < handkarten.size(); i++){
                        spieleKarte(handkarten.get(i), spiel.getStapel(raumName));
                    }

                    handkarten = spiel.getHandkarten(raumName, name);
                    System.out.println(handkarten.size());
                    spiel.karteZiehen(raumName, name);
                } catch (Exception e) {
                }
            }else{
                try {
                    wait(500);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());;
                }
            }
        }
    }


    /**
     * startet den Bot
     */
    public void start() {
        t.start();
    }
}

