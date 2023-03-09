package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

import Server.Zugriffsverwaltung.*;
import Server.Spiel.*;
import Server.Chat.*;

/**
 * @author Ryan Antipow, Fabian Dietrich, Dorcy Hakizimana, Hendrik Wolf, Yannic Schopfer
 */

/**
 * Die Server-Klasse implementiert die Zugriffsverwaltung, die Spielraumverwaltung, den Chatverwaltung und das Leaderboard.
 * Er erstellt das rmi-Registry, bindet die verschiedenen ServerInstanzen an das Registry
 * und stellt diese zur Verfügung damit der Client Zugriff darauf hat.
 */

public class Server implements Zugriffsverwaltung, Spielraumverwaltung, Chatverwaltung, Leaderboard, Spielverwaltung {

    private ArrayList<Account> accounts = new ArrayList<Account>();
    private ArrayList<Account> angemeldet = new ArrayList<Account>();
    private ArrayList<String> lobby = new ArrayList<String>();
    private ArrayList<Spielraum> spielraeume = new ArrayList<Spielraum>();

    private ArrayList<Spiel> spiele = new ArrayList<Spiel>();
    private ArrayList<Punktekonto> bestenliste = new ArrayList<Punktekonto>();

    private String[] checkwords;

    private String lobbyChat = "";

    public Server() throws IOException {
        super();

        List<String> listOfStrings
                = new ArrayList<String>();

        BufferedReader bf = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/Server/Chat/checkwords.txt")));

        String line = bf.readLine();

        while (line != null) {
            listOfStrings.add(line);
            line = bf.readLine();
        }

        // closing bufferreader object
        bf.close();

        // storing the data in arraylist to array
        String[] array
                = listOfStrings.toArray(new String[0]);

        this.checkwords = array;

    }

    /**
     * Gibt die Liste der laufenden Spiele auf dem Server aus.
     *
     * @return Liste der laufenden Spiele.
     */
    ArrayList<Spiel> getSpiele() {
        return this.spiele;
    }

    /**
     * Setzt die Liste der laufenden Spiele auf dem Server.
     *
     * @param spiele Liste mit Spielen
     */
    void setSpiele(ArrayList<Spiel> spiele) {
        this.spiele = spiele;
    }

    /**
     * Gibt die Liste der registrierten Spielräume Spiele auf dem Server aus.
     *
     * @return Liste der registrierten Spielräume.
     */
    ArrayList<Spielraum> getSpielraeume() {
        return this.spielraeume;
    }

    /**
     * Setzt die Liste der registrierten Spielräume auf dem Server.
     *
     * @param spielraeume Liste aus Spielräumen.
     */
    void setSpielraeume(ArrayList<Spielraum> spielraeume) {
        this.spielraeume = spielraeume;
    }

    /**
     * Gibt die Liste der registrierten Accounts auf dem Server aus.
     *
     * @return Liste der registrierten Accounts.
     */
    ArrayList<Account> getAccounts() {
        return this.accounts;
    }

    /**
     * Setzt die Liste der registrierten Accounts auf dem Server.
     *
     * @param accounts Liste von Accounts.
     */
    void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * Gibt die Liste der Spieler in der Lobby des Servers aus.
     *
     * @return Liste der Spieler in der Lobby.
     */
    ArrayList<String> getLobby() {
        return this.lobby;
    }

    /**
     * Setzt die Liste der der Spieler in der Lobby des Servers.
     *
     * @param lobby Liste von Namen.
     */
    void setLobby(ArrayList<String> lobby) {
        this.lobby = lobby;
    }

    /**
     * Gibt die Liste der angemeldeten Spieler auf dem Server aus.
     *
     * @return Liste der angemeldeten Spieler.
     */
    ArrayList<Account> getAngemeldet() {
        return this.angemeldet;
    }

    /**
     * Setzt die der angemeldeten Spieler auf dem Server.
     *
     * @param angemeldet Liste von Accounts.
     */
    void setAngemeldet(ArrayList<Account> angemeldet) {
        this.angemeldet = angemeldet;
    }

    /**
     * Gibt die Liste der registrierten Punktekontos auf dem Server aus.
     *
     * @return Liste der registrierten Punktekontos.
     */
    ArrayList<Punktekonto> getBestenliste() {
        return this.bestenliste;
    }

    /**
     * Setzt die Liste der registrierten Punktekontos auf dem Server.
     *
     * @param bestenliste Liste von Punktekontos.
     */
    void setBestenliste(ArrayList<Punktekonto> bestenliste) {
        this.bestenliste = bestenliste;
    }

    //Implementierung der Zugriffsverwaltung
    //

    /**
     * Meldet Spieler an.
     * Das heißt es wird geprüft, ob die Benutzer bereits angemeldet sind und wenn das so ist, wird eine Exception geworfen.
     * Der Spieler wird für die Anmeldung nach einer Eingabe des Benutzernamen und des Passwortes gefragt.
     * Daraufhin wird für noch nicht angemeldete Benutzer der Account in eine Arrayliste der schon angemeldeten Spieler hinzugefügt
     * und die Benutzernamen werden inn eine Arrayliste namens Lobby hinzugefügt.
     * Wenn das Passwort falsch ist oder der Benutzername schon vergeben wird eine Exception geworfen.
     *
     * @param benutzername Der Name des anzumeldenden Nutzers
     * @param passwort     Das Passwort des anzumeldenden Nutzers
     * @throws Exception "Passwort Falsch"
     * @throws Exception "Benutzername nicht vergeben"
     * @throws Exception "Benutzer bereits angemeldet"
     */
    @Override
    public synchronized void spieler_anmelden(String benutzername, String passwort) throws Exception {
        for (Account acc : this.angemeldet) {
            if (acc.getBenutzername().equals(benutzername)) {
                throw new Exception("Benutzer bereits angemeldet.");
            }
        }
        for (Account acc : this.accounts) {
            if (acc.getBenutzername().equals(benutzername)) {
                if (acc.getPasswort().equals(passwort)) {
                    this.angemeldet.add(acc);
                    this.lobby.add(benutzername);
                    return;
                } else {
                    throw new Exception("Passwort falsch.");
                }
            }
        }
        throw new Exception("Benutzername nicht vergeben.");
    }

    /**
     * Die Methode registriert Spieler und erwartet die Eingabe von einem Benutzernamen
     * und 2 Passworten und prüft, ob diese übereinstimmen.
     * Des Weiteren prüft die den Nutzernamen und das Passwort auf unerlaubte Teilworte.
     * Für die Registrierung wird man nach einem Benutzernamen und entsprechendem Passwort
     * das man zweimal eingeben muss gefragt.
     * Falls die Passwörter nicht übereinstimmen oder der Nutzername bzw das Passwort unerlaubte Teilworte enthalten
     * wird eine Exception geworfen.
     *
     * @param benutzername Name des zu registrierenden Spielers
     * @param passwort     Passwort des zu registrierenden Spielers
     * @param passwortw    Das zu wiederholende Passwort des zu registrierenden Spielers
     * @throws Exception "Benutzername Unerlaubtes Teilwort."
     * @throws Exception "Passwort Unerlaubtes Teilwort."
     * @throws Exception "Benutzername schon vergeben."
     * @throws Exception "Passwörter stimmen nicht überein."
     * @throws Exception "Account registriert."
     */
    @Override
    public synchronized void spieler_registrieren(String benutzername, String passwort, String passwortw) throws Exception {
        for (String badWord : this.checkwords) {
            if (benutzername.toLowerCase().contains(badWord)) {
                throw new Exception("Benutzername Unerlaubtes Teilwort.");
            }
        }

        for (String badWord : this.checkwords) {
            if (passwort.toLowerCase().contains(badWord)) {
                throw new Exception("Passwort Unerlaubtes Teilwort.");
            }
        }

        boolean vergeben = false;
        for (Account acc : this.accounts) {
            if (acc.getBenutzername().equals(benutzername)) {
                vergeben = true;
            }
        }

        if (vergeben) {
            throw new Exception("Benutzername schon vergeben.");
        } else if (!passwort.equals(passwortw)) {
            throw new Exception("Passwörter stimmen nicht überein.");
        } else {
            Account new_acc = new Account(benutzername, passwort);
            this.accounts.add(new_acc);
            System.out.println("Account registriert.");
        }

    }

    /**
     * Lässt den Spieler das Passwort oder den Benutzernamen ändern.
     * Dafür wird nach neuem Nutzername und Passwort des jeweiligen Spielers gefragt.
     * Es wird geprüft, ob das neue Passwort oder der neue Benutzername unerlaubte Teilworte enthält, der Benutzername schon vergeben ist
     * oder die Benutzerdaten nicht übereinstimmen.
     * Sollten der Nutzername oder das Passwort unerlaubte Teilworte enthalten
     * oder die Benutzerdaten nicht übereinstimmen werden Exceptions geworfen.
     *
     * @param benutzername       Benutzername der geändert werden kann
     * @param neuer_benutzername geänderter Benutzername
     * @param passwort           neues Passwort
     * @param passwortw          neues Passwort Wiederholung
     * @throws Exception "Passwort Unerlaubtes Teilwort."
     * @throws Exception "Benutzername schon vergeben."
     * @throws Exception "Passwörter stimmen nicht überein."
     */
    @Override
    public synchronized void spieler_bearbeiten(String benutzername, String neuer_benutzername, String passwort, String passwortw) throws Exception {
        for (String badWord : this.checkwords) {
            if (neuer_benutzername.toLowerCase().contains(badWord)) {
                throw new Exception("Benutzername Unerlaubtes Teilwort.");
            }
        }

        for (String badWord : this.checkwords) {
            if (passwort.toLowerCase().contains(badWord)) {
                throw new Exception("Passwort Unerlaubtes Teilwort.");
            }
        }

        boolean vergeben = false;
        if (!benutzername.equals(neuer_benutzername)) {
            for (Account acc : this.accounts) {
                if (acc.getBenutzername().equals(neuer_benutzername)) {
                    vergeben = true;
                }
            }
        }
        if (vergeben) {
            throw new Exception("Benutzername schon vergeben.");
        } else {
            for (Account acc : this.accounts) {
                if (acc.getBenutzername().equals(benutzername)) {
                    if (!passwort.equals(passwortw)) {
                        throw new Exception("Passwörter stimmen nicht überein.");
                    } else {
                        acc.setBenutzername(neuer_benutzername);
                        acc.setPasswort(passwort);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Die Benutzerdaten der Spieler werden gelöscht (Account wird aus Liste der angemeldeten, der allgemeinen und der in der Lobby
     * befindlichen Accounts entfernt).
     * Dafür wird nach Nutzername und Passwort des jeweiligen Spielers gefragt.
     * Es wird geprüft, ob das richtige Passwort eingegeben wurde.
     * Wenn das nicht der Fall ist, wird eine Exception geworfen.
     * Nutzername und Passwort werden vorher abgefragt.
     *
     * @param benutzername der Benutzername des zu löschenden Accounts
     * @param passwort     das Passwort des zu löschenden Accounts
     * @throws Exception "Passwort falsch"
     */
    @Override
    public synchronized void spieler_loeschen(String benutzername, String passwort) throws Exception {
        for (Account acc : this.accounts) {
            if (acc.getBenutzername().equals(benutzername)) {
                if (!acc.getPasswort().equals(passwort)) {
                    throw new Exception("Passwort falsch.");
                } else {
                    this.accounts.remove(acc);
                    this.angemeldet.remove(acc);
                    this.lobby.remove(acc);
                    return;
                }
            }
        }
    }

    /**
     * Meldet den Spieler ab (Account wird aus Liste der angemeldeten und in der Lobby befindlichen Acocounts entfernt).
     *
     * @param benutzername Benutzername eines Spielers der sich anmeldet
     * @throws RemoteException
     */
    @Override
    public synchronized void spieler_abmelden(String benutzername) throws RemoteException {
        for (Account acc : this.accounts) {
            if (acc.getBenutzername().equals(benutzername)) {
                this.angemeldet.remove(acc);
                this.lobby.remove(acc);
                return;
            }
        }
    }

    /**
     * Spieler werden aus der Lobby entfernt damit sie dem Spielraum hinzugefügt werden können.
     * Account wird aus Liste der in der Lobby befindlichen Acocounts entfernt.
     * Wenn der Spielername nicht in der Lobbyliste vorhanden ist, wird eine Exception geworfen.
     *
     * @param benutzername Name eines Spielers, der aus der Lobby entfernt werden soll.
     * @throws Exception "Spieler ist nicht in Lobby."
     */
    @Override
    public void lobby_entfernen(String benutzername) throws Exception {
        for (String acc : this.lobby) {
            if (acc.equals(benutzername)) {
                this.lobby.remove(acc);
                return;
            }
        }
        throw new Exception("Spieler ist nicht in Lobby");
    }

    /**
     * Spieler wird der Lobby hinzugefügt (Account wird in die Liste der in der Lobby befindlichen Acocounts hinzugefügt).
     *
     * @param benutzername Name eines Spielers der zur Lobby hinzugefügt wird
     * @throws Exception
     */
    @Override
    public void lobby_hinzufuegen(String benutzername) throws Exception {
        this.lobby.add(benutzername);
    }

    /**
     * Überprüft ob sich Spieler in der Lobby befindet.
     *
     * @param benutzername
     * @return
     * @throws Exception
     */
    @Override
    public Boolean is_lobby(String benutzername) throws Exception {
        for (String acc : this.lobby) {
            if (acc.equals(benutzername)) {
                return true;
            }
        }
        return false;
    }


    //Implementierung Spielraumverwaltung
    //

    /**
     * Es wird ein Spielraum erstellt und in eine Liste der Spielräume hinzugefügt.
     * Es wird als Eingabe ein Spielraumname und ein optionales Passwort, das man zweimal eingibt
     * und das übereinstimmen muss, erwartet.
     * Es wird geprüft, ob Spielraumname oder Passwort unerlaubte Teilworte enthalten und ob die
     * Passwörter übereinstimmen.
     * Enthält der Spielraumname oder das Passwort unerlaubte Teilwort oder stimmen die Passworte nicht überin
     * werden entsprechende Exceptions geworfen.
     *
     * @param spieler
     * @param name      Name des zu erstellenden Spielraums
     * @param passwort  Passwort des Spielraums
     * @param passwortw Wiederholung des Passwortes des Spielruams
     * @throws Exception "Spielraumname Unerlaubtes Teilwort.")
     * @throws Exception "Passwort Unerlaubtes Teilwort."
     * @throws Exception "Spielraumname vergeben."
     * @throws Exception "Passwörter stimmen nicht überein."
     */
    @Override
    public void spielraum_erstellen(String spieler, String name, String passwort, String passwortw) throws Exception {
        for (String badWord : this.checkwords) {
            if (name.toLowerCase().contains(badWord)) {
                throw new Exception("Spielraumname Unerlaubtes Teilwort.");
            }
        }

        for (String badWord : this.checkwords) {
            if (passwort.toLowerCase().contains(badWord)) {
                throw new Exception("Passwort Unerlaubtes Teilwort.");
            }
        }

        for (Spielraum raum : this.spielraeume) {
            if (raum.getName().equals(name)) {
                throw new Exception("Spielraumname vergeben.");
            }
        }
        if (!passwort.equals(passwortw)) {
            throw new Exception("Passwörter stimmen nicht überein.");
        } else {
            Spielraum new_raum = new Spielraum(name, passwort, spieler);
            this.spielraeume.add(new_raum);
            System.out.println("Spielraum " + name + " erstellt.");
            return;
        }
    }


    /**
     * Entfernt einen Spieler aus dem Spielraum.
     * Es wird geprüft ob Spieler in dem entsprechenden Spielraum sind und ob der Spielraum existiert.
     * Ist eines der beiden Sachen nicht der Fall werden Exceptions geworfen.
     *
     * @param spieler   der Name vom Spieler der entfernt werden soll
     * @param spielraum Der Name vom Spielraum aus dem der Spieler entfernt wird
     * @throws RemoteException
     * @throws Exception       "Spieler nicht in diesem Spielraum."
     * @throws Exception       "Spielraum existiert nicht."
     */
    @Override
    public void spieler_entfernen(String spieler, String spielraum) throws RemoteException, Exception {
        for (Spielraum raum : this.spielraeume) {
            if (raum.getName().equals(spielraum)) {
                for (String raumsp : raum.getSpieler()) {
                    if (raumsp.equals(spieler)) {
                        raum.removeSpieler(raumsp);
                        System.out.println("Spieler " + spieler + " aus Raum " + spielraum + " entfernt.");
                        return;
                    }
                }
                throw new Exception("Spieler nicht in diesem Spielraum.");
            }
        }
        throw new Exception("Spielraum existiert nicht.");
    }


    /**
     * Lässt einen Spieler in den Spielraum beitreten.
     * Es wird nach Eingabe des Spielraumpasswortes gefragt.
     * Des Weiteren wird geprüft ob der Spieler der beitreten soll sich in der Lobby
     * befindet und ob eingegebene Passwort korrekt ist sowie, ob der Spielraum existiert.
     * Ist eines dieser Sachen nicht der Fall werden Exceptions geworfen.
     *
     * @param spieler   Name vom Spieler der hinzugefügt werden soll
     * @param isLobby   Wahrheitswerte der aussagt, ob dich ein Spieler in der Lobby befindet
     * @param spielraum Name vom Spielraum in den der Spieler beitritt
     * @param passwort
     * @throws Exception "Spieler ist nicht in Lobby oder existiert nicht."
     * @throws Exception "Spielraum existiert nicht."
     */
    @Override
    public void spielraum_beitreten(String spieler, Boolean isLobby, String spielraum, String passwort)
            throws Exception {
        for (Spielraum raum : this.spielraeume) {
            if (raum.getName().equals(spielraum)) {
                if (raum.getSpieler().size() < 4) {
                    if (isLobby) {
                        if (raum.getPasswort().equals(passwort)) {
                            raum.addSpieler(spieler);
                            System.out.println("Spieler " + spieler + " ist Raum " + spielraum + " beigetreten.");
                            return;
                        } else {
                            throw new Exception("Passwort falsch.");
                        }
                    }
                    throw new Exception("Spieler ist nicht in Lobby oder existiert nicht.");
                } else {
                    throw new Exception("Spielraum voll.");
                }
            }
        }
        throw new Exception("Spielraum existiert nicht.");
    }

    /**
     * Fügt einen Bot hinzu.
     *
     * @param name      Name des hinzuzufügenden Bots
     * @param spielraum Name des Spielraums.
     * @throws RemoteException
     * @throws Exception
     */
    @Override
    public void bot_hinzufuegen(String name, String spielraum) throws RemoteException, Exception {
        for (Spielraum raum : this.spielraeume) {
            if (raum.getName().equals(spielraum)) {
                if (raum.getSpieler().size() < 4) {
                    raum.addSpieler(name);
                }else{
                    throw new Exception("Spielraum voll");
                }
            }
        }
    }

    @Override
    public void bot_entfernen(String spielraumname) {
        for (Spielraum raum: this.spielraeume) {
            if (raum.getName().equals(spielraumname)) {
                ArrayList<String> spielerList = raum.getSpieler();
                for (String sp: spielerList) {
                    if (sp.contains("Bot")) {
                        spielerList.remove(sp);
                        raum.setSpieler(spielerList);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public Boolean istInRaum(String spielraumname, String name) {
        for (Spielraum raum: this.spielraeume) {
            if (raum.getName().equals(spielraumname)) {
                for (String spieler: raum.getSpieler()) {
                    if (spieler.equals(name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Sorgt dafür das Spieler den Spielraum verlassen kann.
     *
     * @param name      Name des Spielers der den Spielraum verlassen will
     * @param spielraum Name des Spielraumes der verlassen werden soll
     * @throws Exception
     */
    @Override
    public void spielraum_verlassen(String name, String spielraum) throws Exception {
        for (Spielraum raum : this.spielraeume) {
            if (raum.getName().equals(spielraum)) {
                for (String raumsp : raum.getSpieler()) {
                    if (raumsp.equals(name)) {
                        raum.removeSpieler(raumsp);
                        if (raum.getSpieler().size() == 0) {
                            this.spielraeume.remove(raum);
                            return;
                        }
                        if (raum.getHost().equals(name)) {
                            int rnd = new Random().nextInt(raum.getSpieler().size());
                            raum.setHost(raum.getSpieler().get(rnd));
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * Gibt eine Liste zurück die die Namen der Spieler eines Spielraums enthält.
     *
     * @param raumname Der Name des Spielraums in dem die Spieler spielen
     * @return Eine Arrayliste mit den Namen der Spieler die sich im Spielraum befinden
     * @throws Exception
     */
    @Override
    public ArrayList<String> getRaumSpieler(String raumname) throws Exception {
        for (Spielraum raum : this.spielraeume) {
            if (raum.getName().equals(raumname)) {
                return raum.getSpieler();
            }
        }
        return null;
    }

    /**
     * Startet das Spiel.
     * Prüft ob genug Spieler im Spielraum sind und ob der Spielraum existiert.
     * Ist eines dieser Sachen nicht der Fall werden Exceptions geworfen.
     *
     * @param spielraum Spielraumname von dem aus das Spiel gestartet werden soll
     * @throws RemoteException
     * @throws Exception       "Nicht genug Spieler in Spielraum."
     * @throws Exception       "Nachricht enthält unlerbaubte Teilworte."
     */
    @Override
    public void spiel_starten(String spielraum) throws RemoteException, Exception {
        for (Spielraum raum : this.spielraeume) {
            if (raum.getName().equals(spielraum)) {
                if (raum.getSpieler().size() >= 2) {
                    System.out.println("Spiel gestartet.");
                    return;
                } else {
                    throw new Exception("Nicht genug Spieler in Spielraum.");
                }
            }
        }
        throw new Exception("Spielraum existiert nicht.");
    }

    /**
     * Fügt Nachricht und Sender zu String zusammen und ordnet es in Zeilen.
     * Diese werden dann im Raumchat gespeichert.
     * Diese Methode ist für den Chat im Spielraum.
     * Prüft Nachrichten auf unerlaubte Teilworte.
     * Sind diese enthalten wird eine Exception geworfen.
     *
     * @param nachricht  Nachricht eines Spielers
     * @param sender     Name von diesem Spielers
     * @param empfaenger In diesem Fall die Namen des Spielraums
     * @throws Exception "Nachricht enthält unlerbaubte Teilworte."
     */
    @Override
    public synchronized void neue_spielraum_nachricht(String nachricht, String sender, String empfaenger) throws Exception {
        for (String badWord : this.checkwords) {
            if (nachricht.toLowerCase().contains(badWord)) {
                throw new Exception("Nachricht enthält unerlaubte Teilworte.");
            }
        }

        for (Spielraum raum : this.spielraeume) {
            if (raum.getName().equals(empfaenger)) {
                String[] lines = raum.getRaumChat().split("\r\n|\r|\n");
                if (lines[0].equals("")) {
                    raum.setRaumChat(sender + ": " + nachricht);
                } else if (lines.length < 20) {
                    String chat = raum.getRaumChat();
                    raum.setRaumChat(chat + "\n" + sender + ": " + nachricht);
                } else {
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(lines));
                    list.add(sender + ": " + nachricht);
                    list.remove(0);
                    String str = list.get(0);
                    list.remove(0);
                    for (String s : list) {
                        str = str + "\n" + s;
                    }
                    raum.setRaumChat(str);
                }
            }
        }
        notifyAll();
    }


    /**
     * Gibt den Spielraumchat entsprechend in einem geordneten String zurück
     *
     * @param empfaenger In diesem Fall der Name des Spielraums
     * @return Spielraumchat
     * @throws Exception
     */
    @Override
    public String getSpielraumChatverlauf(String empfaenger) throws Exception {
        for (Spielraum raum : this.spielraeume) {
            if (raum.getName().equals(empfaenger)) {
                return raum.getRaumChat();
            }
        }
        return "";
    }

    /**
     * Fügt Nachricht und Sender zu String zusammen und ordnet es in Zeilen.
     * Diese werden dann im Lobby gespeichert.
     * Diese Methode ist für den Chat in der Lobby.
     * Prüft Nachrichten auf unerlaubte Teilworte.
     * Sind diese enthalten wird eine Exception geworfen.
     *
     * @param nachricht Nachricht eines Spielers
     * @param sender    Name von diesem Spieler
     * @throws Exception "Unerlaubtes Teilwort."
     */
    @Override
    public synchronized void neue_nachricht(String nachricht, String sender) throws Exception {
        for (String badWord : this.checkwords) {
            if (nachricht.toLowerCase().contains(badWord)) {
                throw new Exception("Unerlaubtes Teilwort.");
            }
        }

        String[] lines = this.lobbyChat.split("\r\n|\r|\n");
        if (lines[0].equals("")) {
            this.lobbyChat = sender + ": " + nachricht;
        } else if (lines.length < 20) {
            this.lobbyChat = this.lobbyChat + "\n" + sender + ": " + nachricht;
        } else {
            ArrayList<String> list = new ArrayList<>(Arrays.asList(lines));
            list.add(sender + ": " + nachricht);
            list.remove(0);
            String str = list.get(0);
            list.remove(0);
            for (String s : list) {
                str = str + "\n" + s;
            }
            this.lobbyChat = str;
        }
        notifyAll();
    }

    /**
     * Gibt den Lobbychat entsprechend in einem geordneten String zurück.
     *
     * @return Lobbychat
     * @throws Exception
     */
    @Override
    public String getChatverlauf() throws Exception {
        return this.lobbyChat;
    }

    /**
     * Anlegen und Speichern eines Punktekontos eines Spielers in einer Bestenliste.
     *
     * @param name Name des Spielers für den das Punktekonto angelegt werden soll
     * @throws Exception
     */
    @Override
    public void neues_punktekonto(String name) throws Exception {
        this.bestenliste.add(new Punktekonto(name, 0, 0, 0));
    }

    /**
     * Benennt den Namen eines Punktekontos um.
     *
     * @param name Der Name des zu umbenennenden Punktekontos.
     * @param neuer_name Der neue Name für das Punktekonto.
     */
    @Override
    public void rename_punktekonto(String name, String neuer_name) throws Exception {
        for (Punktekonto konto : this.bestenliste) {
            if (konto.getName().equals(name)) {
                konto.setName(neuer_name);
            }
        }
    }

    /**
     * Löschen eines Punktekontos eines Spielers.
     *
     * @param name Name des Spielers dessen Punktekonto gelöscht wird
     * @throws Exception
     */
    @Override
    public void punktekonto_loeschen(String name) throws Exception {
        ArrayList<Punktekonto> kontoliste = new ArrayList<>(this.bestenliste);
        for (Punktekonto konto : kontoliste) {
            if (konto.getName().equals(name)) {
                this.bestenliste.remove(konto);
            }
        }
    }

    /**
     * Ausgabe der Punkte, der gewonnenen Spieler und der gespielten Spiele von einem Spieler in einer Liste.
     *
     * @param name
     * @return Arraylist der Punkte, der gewonnenen Spieler und der gespielten Spiele von einem Spieler
     * @throws Exception
     */
    @Override
    public ArrayList<Integer> get_punktekonto(String name) throws Exception {
        for (Punktekonto konto : this.bestenliste) {
            if (konto.getName().equals(name)) {
                ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(konto.getGespielte_spiele());
                list.add(konto.getGewonnene_spiele());
                list.add(konto.getPunkte());
                return list;
            }
        }
        return null;
    }

    /**
     * Ausgabe der 10 Spieler mit den meisten Punkten
     *
     * @return Übersichtlicher String mit den besten 10 Spielern nach Punkten
     * @throws Exception
     */
    @Override
    public String top_10_ausgeben() throws Exception {
        ArrayList<Punktekonto> list = new ArrayList<>(this.bestenliste);

        Collections.sort(list, new Comparator<Punktekonto>() {
            @Override
            public int compare(Punktekonto p1, Punktekonto p2) {
                return p2.getPunkte() - p1.getPunkte();
            }
        });
        String ausgabe = list.get(0).getName() + ": " + list.get(0).getPunkte();
        if (list.size() == 1) {
            return ausgabe;
        } else if (list.size() < 10) {
            List<Punktekonto> top10 = list.subList(1, list.size());
            for (Punktekonto konto : top10) {
                ausgabe = ausgabe + "\n" + konto.getName() + ": " + konto.getPunkte();
            }
            return ausgabe;
        } else {
            List<Punktekonto> top10 = list.subList(1, 10);
            for (Punktekonto konto : top10) {
                ausgabe = ausgabe + "\n" + konto.getName() + ": " + konto.getPunkte();
            }
            return ausgabe;
        }
    }

    /**
     * Anpassen der Bestenliste nach Änderung der Punkte eines Spielers
     *
     * @param spieler Liste der Spieler deren Platzierung geändert werden soll.
     * @throws Exception
     */
    @Override
    public void bestenliste_anpassen(ArrayList<Spieler> spieler) throws Exception {
        for (Spieler sp : spieler) {
            Integer punkte = sp.getPunkte();
            for (Punktekonto konto : this.bestenliste) {
                if (konto.getName().equals(sp.getName())) {
                    konto.setPunkte(konto.getPunkte() + punkte);
                    konto.setGespielte_spiele(konto.getGespielte_spiele() + 1);
                    if (punkte >= 100) {
                        konto.setGewonnene_spiele(konto.getGewonnene_spiele() + 1);
                    }
                }
            }
        }
    }

    /**
     * Gibt zurück, ob ein Spieler der Host eines bestimmten Spielraums ist.
     *
     * @param name Name des Spielers der abgefragt wird.
     * @param spielraum Name des Spielraums der abgefragt wird.
     * @return True oder False
     */
    @Override
    public Boolean isHost(String name, String spielraum) {
        for (Spielraum raum : this.spielraeume) {
            if (raum.getName().equals(spielraum)) {
                return raum.getHost().equals(name);
            }
        }
        return false;
    }

    /**
     * Hier wird das Ingame Spiel gestartet
     *
     * @param spielraumname Name des Spielraums in dem ein Spiel gestartet werden soll.
     * @param spieler Liste der Spieler im Spielraum.
     * @throws Exception "Spiel existiert schon"
     * @throws Exception
     */
    @Override
    public void spielStarten(String spielraumname, ArrayList<String> spieler) throws Exception {
        if (spieler.size() < 2) {
            throw new Exception("Zu wenige Spieler");
        }
        for (Spiel spiel: this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                if (spiel.getSpielVorbei()) {
                    spielLoeschen(spielraumname);
                    break;
                }
                else {
                    throw new Exception("Spiel existiert schon");
                }
            }
        }
        ArrayList<String> spielkarten = new ArrayList<String>(Arrays.asList("HerzAss", "Herz2", "Herz3", "Herz4", "Herz5", "Herz6", "Herz7", "Herz8", "Herz9", "Herz10", "HerzBube", "HerzDame", "HerzKoenig",
                "PikAss", "Pik2", "Pik3", "Pik4", "Pik5", "Pik6", "Pik7", "Pik8", "Pik9", "Pik10", "PikBube", "PikDame", "PikKoenig",
                "KaroAss", "Karo2", "Karo3", "Karo4", "Karo5", "Karo6", "Karo7", "Karo8", "Karo9", "Karo10", "KaroBube", "KaroDame", "KaroKoenig",
                "KreuzAss", "Kreuz2", "Kreuz3", "Kreuz4", "Kreuz5", "Kreuz6", "Kreuz7", "Kreuz8", "Kreuz9", "Kreuz10", "KreuzBube", "KreuzDame", "KreuzKoenig"));
        Collections.shuffle(spielkarten);

        Integer chips = 80 / spieler.size();
        ArrayList<Spieler> neue_spieler = new ArrayList<Spieler>();
        for (String name : spieler) {
            ArrayList<String> handkarten = new ArrayList<String>(spielkarten.subList(0, 7));
            spielkarten.removeAll(spielkarten.subList(0, 7));
            Spieler neuer_spieler = new Spieler(name, handkarten, chips - 1);
            neue_spieler.add(neuer_spieler);
        }

        Spiel neues_spiel = new Spiel(spielraumname, neue_spieler, spielkarten);
        neues_spiel.setChipsImTopf(spieler.size());
        this.spiele.add(neues_spiel);
    }

    /**
     * Der Spieler kann eine Karte vom Ziehstabel ziehen und auf seine Hand legen, solange er noch keine 7 Karten auf der Hand hat.
     * Falls der Spieler während er die Funktion einen König auf der Hand hat, muss er 3 Coins Strafe zahlen und der König wird automatisch in eine freie Ecke gelegt
     * Ist die Karte ein König, wird der König direkt in eine freie Ecke gelegt.
     *
     * @param spielraumname Name des Spielraums
     * @param name          Name des Spielers
     * @throws Exception
     */
    @Override
    public void karteZiehen(String spielraumname, String name) throws Exception {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                ArrayList<Spieler> spielerList = new ArrayList<>(spiel.getSpieler());
                for (Spieler spieler : spielerList) {
                    if (spieler.getName().equals(name)) {
                        while (koenigAufHand(spielraumname, name)) {
                            if (zahleChip(spielraumname, name, 3)) {
                                return;
                            }
                            ArrayList<String> karten = new ArrayList<>(spieler.getHandkarten());
                            for (String karte : karten) {
                                if (karte.contains("Koenig")) {
                                    if (spiel.getStapelObenLinks().size() == 0) {
                                        ArrayList<String> stapel = new ArrayList<>(spiel.getStapelObenLinks());
                                        stapel.add(karte);
                                        stapel.add(karte);
                                        spiel.setStapelObenLinks(stapel);
                                        spieler.removeHandkarte(karte);
                                    } else if (spiel.getStapelObenRechts().size() == 0) {
                                        ArrayList<String> stapel = new ArrayList<>(spiel.getStapelObenRechts());
                                        stapel.add(karte);
                                        stapel.add(karte);
                                        spiel.setStapelObenRechts(stapel);
                                        spieler.removeHandkarte(karte);
                                    } else if (spiel.getStapelUntenLinks().size() == 0) {
                                        ArrayList<String> stapel = new ArrayList<>(spiel.getStapelUntenLinks());
                                        stapel.add(karte);
                                        stapel.add(karte);
                                        spiel.setStapelUntenLinks(stapel);
                                        spieler.removeHandkarte(karte);
                                    } else {
                                        ArrayList<String> stapel = new ArrayList<>(spiel.getStapelUntenRechts());
                                        stapel.add(karte);
                                        stapel.add(karte);
                                        spiel.setStapelUntenRechts(stapel);
                                        spieler.removeHandkarte(karte);
                                    }
                                }
                            }
                        }
                        if (spiel.getNachziehstapel().size() != 0) {
                            if (spieler.getHandkarten().size() < 19) {
                                ArrayList<String> nachziehstapel = new ArrayList<>(spiel.getNachziehstapel());
                                String neue_karte = nachziehstapel.get(0);
                                nachziehstapel.remove(0);
                                spiel.setNachziehstapel(nachziehstapel);
                                if (neue_karte.contains("Koenig")) {
                                    if (spiel.getStapelObenLinks().size() == 0) {
                                        ArrayList<String> stapel = new ArrayList<>(spiel.getStapelObenLinks());
                                        stapel.add(neue_karte);
                                        stapel.add(neue_karte);
                                        spiel.setStapelObenLinks(stapel);
                                    } else if (spiel.getStapelObenRechts().size() == 0) {
                                        ArrayList<String> stapel = new ArrayList<>(spiel.getStapelObenRechts());
                                        stapel.add(neue_karte);
                                        stapel.add(neue_karte);
                                        spiel.setStapelObenRechts(stapel);
                                    } else if (spiel.getStapelUntenLinks().size() == 0) {
                                        ArrayList<String> stapel = new ArrayList<>(spiel.getStapelUntenLinks());
                                        stapel.add(neue_karte);
                                        stapel.add(neue_karte);
                                        spiel.setStapelUntenLinks(stapel);
                                    } else {
                                        ArrayList<String> stapel = new ArrayList<>(spiel.getStapelUntenRechts());
                                        stapel.add(neue_karte);
                                        stapel.add(neue_karte);
                                        spiel.setStapelUntenRechts(stapel);
                                    }
                                } else {
                                    spieler.addHandkarte(neue_karte);
                                }
                            } else {
                                if (spieler.getChips() > 1) {
                                    spieler.setChips(spieler.getChips() - 1);
                                    spiel.setChipsImTopf(spiel.getChipsImTopf() + 1);
                                }
                                else {
                                    spieler.setAmLeben(false);
                                    ArrayList<String> nachziehstapel = spiel.getNachziehstapel();
                                    nachziehstapel.addAll(spieler.getHandkarten());
                                    Collections.shuffle(nachziehstapel);
                                    spiel.setNachziehstapel(nachziehstapel);
                                }
                            }
                        }
                    }
                }
                while(true) {
                    spielerList.add(spielerList.get(0));
                    spielerList.remove(0); //so dirty
                    spiel.setSpieler(spielerList);
                    Spieler sp = spielerList.get(0);
                    if (sp.getAmLeben()) {
                        break;
                    }
                }
            }
        }
    }

    public Boolean koenigAufHand(String raumname, String name) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(raumname)) {
                for (Spieler spieler : spiel.getSpieler()) {
                    if (spieler.getName().equals(name)) {
                        for (String karte : spieler.getHandkarten()) {
                            if (karte.contains("Koenig")) {
                                return true;
                            }
                        }
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gibt true zurück, wenn der Spieler an der Reihe ist
     *
     * @param spielraumname Name des Spielraums
     * @param name          Name des Spielers
     * @return true, wenn der Spieler an der Reihe ist
     */
    @Override
    public boolean amZug(String spielraumname, String name) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                if (spiel.getSpieler().get(0).getName().equals(name)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Die ausgewählte Karte des Spielers wird auf den ausgewählten Stapel abgelegt.
     *
     * @param spielraumname Name des Spielraums
     * @param name          Name des Spielers
     * @param karte         Karte, die abgelegt werden möchte
     * @param stapel        Stapel, wo die ausgewählte Karte hin abgelegt wird
     * @throws Exception
     */
    @Override
    public void karteSpielen(String spielraumname, String name, String karte, String stapel) throws Exception {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                ArrayList<Spieler> spieler = spiel.getSpieler();
                for (Spieler sp : spieler) {
                    if (sp.getName().equals(name)) {
                        if (stapel.equals("obenLinks")) {
                            ArrayList<String> neuer_stapel = spiel.getStapelObenLinks();
                            if (neuer_stapel.size() == 0) {
                                if (karte.contains("Koenig")) {
                                    neuer_stapel.add(karte);
                                    neuer_stapel.add(karte);
                                    sp.removeHandkarte(karte);
                                    spiel.setStapelObenLinks(neuer_stapel);
                                    neueRunde(spielraumname, name, sp.getHandkarten().size());
                                }
                                return;
                            }
                            if (karteSpielenMoeglich(karte, neuer_stapel.get(0))) {
                                neuer_stapel.set(0, karte);
                                sp.removeHandkarte(karte);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                            }
                            spiel.setStapelObenLinks(neuer_stapel);
                        } else if (stapel.equals("oben")) {
                            ArrayList<String> neuer_stapel = spiel.getStapelOben();
                            if (neuer_stapel.size() == 0) {
                                neuer_stapel.add(karte);
                                neuer_stapel.add(karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelOben(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                                return;
                            }
                            if (karteSpielenMoeglich(karte, neuer_stapel.get(0))) {
                                neuer_stapel.set(0, karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelOben(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                            }
                        } else if (stapel.equals("obenRechts")) {
                            ArrayList<String> neuer_stapel = spiel.getStapelObenRechts();
                            if (neuer_stapel.size() == 0) {
                                if (karte.contains("Koenig")) {
                                    neuer_stapel.add(karte);
                                    neuer_stapel.add(karte);
                                    sp.removeHandkarte(karte);
                                    spiel.setStapelObenRechts(neuer_stapel);
                                    neueRunde(spielraumname, name, sp.getHandkarten().size());
                                }
                                return;
                            }
                            if (karteSpielenMoeglich(karte, neuer_stapel.get(0))) {
                                neuer_stapel.set(0, karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelObenRechts(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                            }
                        } else if (stapel.equals("links")) {
                            ArrayList<String> neuer_stapel = spiel.getStapelMitteLinks();
                            if (neuer_stapel.size() == 0) {
                                neuer_stapel.add(karte);
                                neuer_stapel.add(karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelMitteLinks(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                                return;
                            }
                            if (karteSpielenMoeglich(karte, neuer_stapel.get(0))) {
                                neuer_stapel.set(0, karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelMitteLinks(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                            }
                        } else if (stapel.equals("rechts")) {
                            ArrayList<String> neuer_stapel = spiel.getStapelMitteRechts();
                            if (neuer_stapel.size() == 0) {
                                neuer_stapel.add(karte);
                                neuer_stapel.add(karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelMitteRechts(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                                return;
                            }
                            if (karteSpielenMoeglich(karte, neuer_stapel.get(0))) {
                                neuer_stapel.set(0, karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelMitteRechts(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                            }
                        } else if (stapel.equals("untenLinks")) {
                            ArrayList<String> neuer_stapel = spiel.getStapelUntenLinks();
                            if (neuer_stapel.size() == 0) {
                                if (karte.contains("Koenig")) {
                                    neuer_stapel.add(karte);
                                    neuer_stapel.add(karte);
                                    sp.removeHandkarte(karte);
                                    spiel.setStapelUntenLinks(neuer_stapel);
                                    neueRunde(spielraumname, name, sp.getHandkarten().size());
                                }
                                return;
                            }
                            if (karteSpielenMoeglich(karte, neuer_stapel.get(0))) {
                                neuer_stapel.set(0, karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelUntenLinks(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                            }
                        } else if (stapel.equals("unten")) {
                            ArrayList<String> neuer_stapel = spiel.getStapelUnten();
                            if (neuer_stapel.size() == 0) {
                                neuer_stapel.add(karte);
                                neuer_stapel.add(karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelUnten(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                                return;
                            }
                            if (karteSpielenMoeglich(karte, neuer_stapel.get(0))) {
                                neuer_stapel.set(0, karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelUnten(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                            }
                        } else if (stapel.equals("untenRechts")) {
                            ArrayList<String> neuer_stapel = spiel.getStapelUntenRechts();
                            if (neuer_stapel.size() == 0) {
                                if (karte.contains("Koenig")) {
                                    neuer_stapel.add(karte);
                                    neuer_stapel.add(karte);
                                    sp.removeHandkarte(karte);
                                    spiel.setStapelUntenRechts(neuer_stapel);
                                    neueRunde(spielraumname, name, sp.getHandkarten().size());
                                }
                                return;
                            }
                            if (karteSpielenMoeglich(karte, neuer_stapel.get(0))) {
                                neuer_stapel.set(0, karte);
                                sp.removeHandkarte(karte);
                                spiel.setStapelUntenRechts(neuer_stapel);
                                neueRunde(spielraumname, name, sp.getHandkarten().size());
                            }
                        }
                    }
                    spiel.setSpieler(spieler);
                    break;
                }
            }
        }
    }

    /**
     * Funktion prüft, ob es nach den Spielregeln erlaubt ist die Karte auf den Stapel zu legen.
     *
     * @param karte   Karte, die auf den Stapel gelegt werden soll.
     * @param obKarte oberste Karte des Stapels, auf den die Karte verschoben werden soll.
     * @return true wenn das ablegen auf den Stapel möglich ist
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
     * Checked, ob eine Spielrunde zu Ende ist. Passt gegebenenfalls das Spielfeld an oder beendet das Spiel.
     *
     * @param spielraumname Name des Spielraums
     * @param name          Name des Spielers
     * @param kartenzahl Anzahl der Handkarten
     */
    void neueRunde(String spielraumname, String name, Integer kartenzahl) {
        if (kartenzahl == 0) {

            for (Spiel spiel : this.spiele) {
                if (spiel.getSpielraumname().equals(spielraumname)) {
                    ArrayList<Spieler> spielerList = spiel.getSpieler();
                    Integer Punkte = spiel.getChipsImTopf();
                    for (Spieler sp : spielerList) {
                        if (!sp.getName().equals(name)) {
                            if (sp.getHandkarten().size() <= sp.getChips()) {
                                Punkte += sp.getHandkarten().size();
                            } else {
                                Punkte += sp.getChips();
                            }
                        }
                    }
                    for (Spieler sp : spielerList) {
                        if (sp.getName().equals(name)) {
                            Integer neue_punkte = sp.getPunkte() + Punkte;
                            if (neue_punkte >= 100) {
                                sp.setPunkte(neue_punkte);
                                spiel.setSpieler(spielerList);
                                spiel.setSpielVorbei(true);
                                return;
                            } else {
                                sp.setPunkte(neue_punkte);
                            }
                        }
                    }

                    ArrayList<String> spielkarten = new ArrayList<String>(Arrays.asList("HerzAss", "Herz2", "Herz3", "Herz4", "Herz5", "Herz6", "Herz7", "Herz8", "Herz9", "Herz10", "HerzBube", "HerzDame", "HerzKoenig",
                            "PikAss", "Pik2", "Pik3", "Pik4", "Pik5", "Pik6", "Pik7", "Pik8", "Pik9", "Pik10", "PikBube", "PikDame", "PikKoenig",
                            "KaroAss", "Karo2", "Karo3", "Karo4", "Karo5", "Karo6", "Karo7", "Karo8", "Karo9", "Karo10", "KaroBube", "KaroDame", "KaroKoenig",
                            "KreuzAss", "Kreuz2", "Kreuz3", "Kreuz4", "Kreuz5", "Kreuz6", "Kreuz7", "Kreuz8", "Kreuz9", "Kreuz10", "KreuzBube", "KreuzDame", "KreuzKoenig"));
                    Collections.shuffle(spielkarten);
                    Integer chips = 80 / spielerList.size();
                    for (Spieler spieler : spielerList) {
                        spieler.setHandkarten(new ArrayList<>(spielkarten.subList(0, 7)));
                        spielkarten.removeAll(spielkarten.subList(0, 7));
                        spieler.setChips(chips - 1);
                        spieler.setAmLeben(true);
                    }
                    spiel.stapelNeu(spielkarten);
                    spiel.setSpieler(spielerList);
                    spiel.setChipsImTopf(spielerList.size());
                    spielerList.add(spielerList.get(0));
                    spielerList.remove(0); //so dirty
                    spiel.setSpieler(spielerList);
                    return;
                }
            }
        }
    }

    Boolean einzigerUeberlebender (String spielraumname) {
        Integer ueberlebende = 0;
        String ueberlebender = "";
        for (Spiel spiel: this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                for (Spieler spieler: spiel.getSpieler()) {
                    if (spieler.getAmLeben()) {
                        ueberlebender = spieler.getName();
                        ueberlebende += 1;
                    }
                }
                if (ueberlebende == 1) {
                    neueRunde(spielraumname, ueberlebender, 0);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Der Spieler kann Karten von einem Stapel zu einem anderen Stapel verschieben, sofern dies möglich ist.
     *
     * @param spielraumname  Name des Spielraums
     * @param ausgangsstapel Stapel von dem Karten verschoben werden
     * @param zielstapel     Stapel, wohin die Karten verschoben werden
     * @throws Exception
     */
    @Override
    public void stapelVerschieben(String spielraumname, String ausgangsstapel, String zielstapel) throws Exception {
        String stapel = zielstapel;
        String karte = "";
        String setKarte = "";
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                if (ausgangsstapel.equals("obenLinks")) {
                    karte = spiel.getStapelObenLinks().get(0);
                    setKarte = spiel.getStapelObenLinks().get(1);
                } else if (ausgangsstapel.equals("oben")) {
                    karte = spiel.getStapelOben().get(0);
                    setKarte = spiel.getStapelOben().get(1);
                } else if (ausgangsstapel.equals("obenRechts")) {
                    karte = spiel.getStapelObenRechts().get(0);
                    setKarte = spiel.getStapelObenRechts().get(1);
                } else if (ausgangsstapel.equals("links")) {
                    karte = spiel.getStapelMitteLinks().get(0);
                    setKarte = spiel.getStapelMitteLinks().get(1);
                } else if (ausgangsstapel.equals("rechts")) {
                    karte = spiel.getStapelMitteRechts().get(0);
                    setKarte = spiel.getStapelMitteRechts().get(1);
                } else if (ausgangsstapel.equals("untenLinks")) {
                    karte = spiel.getStapelUntenLinks().get(0);
                    setKarte = spiel.getStapelUntenLinks().get(1);
                } else if (ausgangsstapel.equals("unten")) {
                    karte = spiel.getStapelUnten().get(0);
                    setKarte = spiel.getStapelUnten().get(1);
                } else if (ausgangsstapel.equals("untenRechts")) {
                    karte = spiel.getStapelUntenRechts().get(0);
                    setKarte = spiel.getStapelUntenRechts().get(1);
                }

                if (stapel.equals("obenLinks")) {
                    ArrayList<String> neuer_stapel = spiel.getStapelObenLinks();
                    if (neuer_stapel.size() == 0) {
                        if (setKarte.contains("Koenig")) {
                            neuer_stapel.add(karte);
                            neuer_stapel.add(setKarte);
                            spiel.setStapelObenLinks(neuer_stapel);
                            clearStapel(spielraumname, ausgangsstapel);
                        }
                        return;
                    }
                    if (karteSpielenMoeglich(setKarte, neuer_stapel.get(0))) {
                        neuer_stapel.set(0, karte);
                        spiel.setStapelObenLinks(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                    }
                } else if (stapel.equals("oben")) {
                    ArrayList<String> neuer_stapel = spiel.getStapelOben();
                    if (neuer_stapel.size() == 0) {
                        neuer_stapel.add(karte);
                        neuer_stapel.add(setKarte);
                        spiel.setStapelOben(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                        return;
                    }
                    if (karteSpielenMoeglich(setKarte, neuer_stapel.get(0))) {
                        neuer_stapel.set(0, karte);
                        spiel.setStapelOben(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                    }
                } else if (stapel.equals("obenRechts")) {
                    ArrayList<String> neuer_stapel = spiel.getStapelObenRechts();
                    if (neuer_stapel.size() == 0) {
                        if (setKarte.contains("Koenig")) {
                            neuer_stapel.add(karte);
                            neuer_stapel.add(setKarte);
                            spiel.setStapelObenRechts(neuer_stapel);
                            clearStapel(spielraumname, ausgangsstapel);
                        }
                        return;
                    }
                    if (karteSpielenMoeglich(setKarte, neuer_stapel.get(0))) {
                        neuer_stapel.set(0, karte);
                        spiel.setStapelObenRechts(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                    }
                } else if (stapel.equals("links")) {
                    ArrayList<String> neuer_stapel = spiel.getStapelMitteLinks();
                    if (neuer_stapel.size() == 0) {
                        neuer_stapel.add(karte);
                        neuer_stapel.add(setKarte);
                        spiel.setStapelMitteLinks(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                        return;
                    }
                    if (karteSpielenMoeglich(setKarte, neuer_stapel.get(0))) {
                        neuer_stapel.set(0, karte);
                        spiel.setStapelMitteLinks(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                    }
                } else if (stapel.equals("rechts")) {
                    ArrayList<String> neuer_stapel = spiel.getStapelMitteRechts();
                    if (neuer_stapel.size() == 0) {
                        neuer_stapel.add(karte);
                        neuer_stapel.add(setKarte);
                        spiel.setStapelMitteRechts(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                        return;
                    }
                    if (karteSpielenMoeglich(setKarte, neuer_stapel.get(0))) {
                        neuer_stapel.set(0, karte);
                        spiel.setStapelMitteRechts(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                    }
                } else if (stapel.equals("untenLinks")) {
                    ArrayList<String> neuer_stapel = spiel.getStapelUntenLinks();
                    if (neuer_stapel.size() == 0) {
                        if (setKarte.contains("Koenig")) {
                            neuer_stapel.add(karte);
                            neuer_stapel.add(setKarte);
                            spiel.setStapelUntenLinks(neuer_stapel);
                            clearStapel(spielraumname, ausgangsstapel);
                        }
                        return;
                    }
                    if (karteSpielenMoeglich(setKarte, neuer_stapel.get(0))) {
                        neuer_stapel.set(0, karte);
                        spiel.setStapelUntenLinks(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                    }
                } else if (stapel.equals("unten")) {
                    ArrayList<String> neuer_stapel = spiel.getStapelUnten();
                    if (neuer_stapel.size() == 0) {
                        neuer_stapel.add(karte);
                        neuer_stapel.add(setKarte);
                        spiel.setStapelUnten(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                        return;
                    }
                    if (karteSpielenMoeglich(setKarte, neuer_stapel.get(0))) {
                        neuer_stapel.set(0, karte);
                        spiel.setStapelUnten(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                    }
                } else if (stapel.equals("untenRechts")) {
                    ArrayList<String> neuer_stapel = spiel.getStapelUntenRechts();
                    if (neuer_stapel.size() == 0) {
                        if (setKarte.contains("Koenig")) {
                            neuer_stapel.add(karte);
                            neuer_stapel.add(setKarte);
                            spiel.setStapelUntenRechts(neuer_stapel);
                            clearStapel(spielraumname, ausgangsstapel);
                        }
                        return;
                    }
                    if (karteSpielenMoeglich(setKarte, neuer_stapel.get(0))) {
                        neuer_stapel.set(0, karte);
                        spiel.setStapelUntenRechts(neuer_stapel);
                        clearStapel(spielraumname, ausgangsstapel);
                    }
                }
                break;
            }
        }
    }

    /**
     * Deie Funktion löscht den eingegeben Stapel.
     *
     * @param spielraumname Name des Spielraums
     * @param stapel        Stapel, der gelöscht wird
     */
    void clearStapel(String spielraumname, String stapel) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                if (stapel.equals("obenLinks")) {
                    spiel.setStapelObenLinks(new ArrayList<String>());
                } else if (stapel.equals("oben")) {
                    spiel.setStapelOben(new ArrayList<String>());
                } else if (stapel.equals("obenRechts")) {
                    spiel.setStapelObenRechts(new ArrayList<String>());
                } else if (stapel.equals("rechts")) {
                    spiel.setStapelMitteRechts(new ArrayList<String>());
                } else if (stapel.equals("links")) {
                    spiel.setStapelMitteLinks(new ArrayList<String>());
                } else if (stapel.equals("untenLinks")) {
                    spiel.setStapelUntenLinks(new ArrayList<String>());
                } else if (stapel.equals("unten")) {
                    spiel.setStapelUnten(new ArrayList<String>());
                } else if (stapel.equals("untenRechts")) {
                    spiel.setStapelUntenRechts(new ArrayList<String>());
                }
            }
        }
    }

    /**
     * Der Spieler bekommt so viele Chips abgezogen, wie er bezahlen muss.
     *
     * @param spielraumname Name des Spielraums
     * @param name          Name des Spielers, der Chips bezahlen muss.
     */
    @Override
    public Boolean zahleChip(String spielraumname, String name, Integer anzahlCoins) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                ArrayList<Spieler> spieler = spiel.getSpieler();
                for (Spieler sp : spieler) {
                    if (sp.getName().equals(name)) {
                        if (sp.getChips() - anzahlCoins >= 0) {
                            sp.setChips(sp.getChips() - anzahlCoins);
                            spiel.setChipsImTopf(spiel.getChipsImTopf() + anzahlCoins);
                        }
                        else {
                            sp.setAmLeben(false);
                            ArrayList<String> nachziehstapel = spiel.getNachziehstapel();
                            nachziehstapel.addAll(sp.getHandkarten());
                            Collections.shuffle(nachziehstapel);
                            spiel.setNachziehstapel(nachziehstapel);
                            if (einzigerUeberlebender(spielraumname)) {
                                return true;
                            }
                        }
                    }
                }
                spiel.setSpieler(spieler);
                return false;
            }
        }
        return false;
    }

    /**
     * Handkarten werden sortiert bei Eingabe von true aufsteigend und bei false farblich.
     *
     * @param name          Name des Spielers, der die Karten sortieren können
     * @param filter        wie soll sortiert werden true aufsteigend false farblich
     * @param spielraumname Name des Spielraums
     * @throws Exception
     */
    @Override
    public void handkartenSortieren(String spielraumname, String name, Boolean filter) throws Exception {
        ArrayList<String> spielkarten = new ArrayList<String>(Arrays.asList("HerzAss", "Herz2", "Herz3", "Herz4", "Herz5", "Herz6", "Herz7", "Herz8", "Herz9", "Herz10", "HerzBube", "HerzDame", "HerzKoenig",
                "PikAss", "Pik2", "Pik3", "Pik4", "Pik5", "Pik6", "Pik7", "Pik8", "Pik9", "Pik10", "PikBube", "PikDame", "PikKoenig",
                "KaroAss", "Karo2", "Karo3", "Karo4", "Karo5", "Karo6", "Karo7", "Karo8", "Karo9", "Karo10", "KaroBube", "KaroDame", "KaroKoenig",
                "KreuzAss", "Kreuz2", "Kreuz3", "Kreuz4", "Kreuz5", "Kreuz6", "Kreuz7", "Kreuz8", "Kreuz9", "Kreuz10", "KreuzBube", "KreuzDame", "KreuzKoenig"));

        ArrayList<String> spielkarten2 = new ArrayList<String>(Arrays.asList("HerzAss", "PikAss", "KaroAss", "KreuzAss", "Herz2", "Pik2", "Karo2", "Kreuz2",
                "Herz3", "Pik3", "Karo3", "Kreuz3", "Herz4", "Pik4", "Karo4", "Kreuz4", "Herz5", "Pik5", "Karo5", "Kreuz5", "Herz6", "Pik6", "Karo6", "Kreuz6",
                "Herz7", "Pik7", "Karo7", "Kreuz7", "Herz8", "Pik8", "Karo8", "Kreuz8", "Herz9", "Pik9", "Karo9", "Kreuz9", "Herz10", "Pik10", "Karo10", "Kreuz10",
                "HerzBube", "PikBube", "KaroBube", "KreuzBube", "HerzDame", "PikDame", "KaroDame", "KreuzDame", "HerzKoenig", "PikKoenig", "KaroKoenig", "KreuzKoenig"));

        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                ArrayList<Spieler> spieler = spiel.getSpieler();
                for (Spieler sp : spieler) {
                    if (sp.getName().equals(name)) {
                        ArrayList<String> handkarten = sp.getHandkarten();

                        if (filter) {
                            handkarten.sort(Comparator.comparingInt(spielkarten2::indexOf));
                        } else {
                            handkarten.sort(Comparator.comparingInt(spielkarten::indexOf));
                        }
                        sp.setHandkarten(handkarten);
                    }
                }
                spiel.setSpieler(spieler);
                return;
            }
        }
    }

    /**
     * @param nachricht
     * @param sender
     * @param empfaenger
     * @throws Exception
     */
    @Override
    public synchronized void neue_spiel_nachricht(String nachricht, String sender, String empfaenger) throws Exception {
        for (String badWord : this.checkwords) {
            if (nachricht.toLowerCase().contains(badWord)) {
                throw new Exception("Nachricht enthält unerlaubte Teilworte.");
            }
        }
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(empfaenger)) {
                String[] lines = spiel.getSpielChat().split("\r\n|\r|\n");
                if (lines[0].equals("")) {
                    spiel.setSpielChat(sender + ": " + nachricht);
                } else if (lines.length < 20) {
                    String chat = spiel.getSpielChat();
                    spiel.setSpielChat(chat + "\n" + sender + ": " + nachricht);
                } else {
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(lines));
                    list.add(sender + ": " + nachricht);
                    list.remove(0);
                    String str = list.get(0);
                    list.remove(0);
                    for (String s : list) {
                        str = str + "\n" + s;
                    }
                    spiel.setSpielChat(str);
                }
            }
        }
        notifyAll();
    }

    /**
     * @param empfaenger Name des Empfängers der Nachricht
     * @return
     * @throws Exception
     */
    @Override
    public String getSpielChatverlauf(String empfaenger) throws Exception {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(empfaenger)) {
                return spiel.getSpielChat();
            }
        }
        return "";
    }

    /**
     * Der Spieler verlässt das Spiel
     *
     * @param name          Name des Spielers
     * @param spielraumname Name des Spielraums
     */
    @Override
    public void spielVerlassen(String spielraumname, String name) {
    }

    /**
     * Gibt alle Handkarten des momentanen Spielers zurück:
     *
     * @param name          Name des Spielers
     * @param spielraumname Name des Spielraums
     * @return ArrayList<Handkarten>
     */
    @Override
    public ArrayList<String> getHandkarten(String spielraumname, String name) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                for (Spieler spieler : spiel.getSpieler()) {
                    if (spieler.getName().equals(name)) {
                        return spieler.getHandkarten();
                    }
                }
            }
        }
        return new ArrayList<String>();
    }

    /**
     * Gibt alle Stapel des momentanen Spielraums zurück
     *
     * @param spielraumname Name des Spielraums
     * @return ArrayList<Stapel>
     */
    @Override
    public ArrayList<ArrayList<String>> getStapel(String spielraumname) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                ArrayList<ArrayList<String>> Stapel = new ArrayList<ArrayList<String>>();
                Stapel.add(spiel.getStapelObenLinks());
                Stapel.add(spiel.getStapelOben());
                Stapel.add(spiel.getStapelObenRechts());
                Stapel.add(spiel.getStapelMitteLinks());
                Stapel.add(spiel.getStapelMitteRechts());
                Stapel.add(spiel.getStapelUntenLinks());
                Stapel.add(spiel.getStapelUnten());
                Stapel.add(spiel.getStapelUntenRechts());
                return Stapel;
            }
        }
        return new ArrayList<ArrayList<String>>();
    }

    /**
     * ibt die Anzahl an Chips des Spielers aus oder 0, wenn der Spieler nicht existiert.
     *
     * @param spielraumname Name des Spielraums
     * @param name          Name des Spielers
     * @return Anzahl Chips des Spielers oder 0, wenn der Spieler nicht existiert.
     */
    @Override
    public Integer getChips(String spielraumname, String name) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                for (Spieler spieler : spiel.getSpieler()) {
                    if (spieler.getName().equals(name)) {
                        return spieler.getChips();
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Gibt an ob der Nachziehstapel leer ist.
     *
     * @param spielraumname Name des Spielraums
     * @return True oder False.
     */
    @Override
    public Boolean nachziehstapelLeer(String spielraumname) {
        for (Spiel spiel: this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                return (spiel.getNachziehstapel().size() == 0);
            }
        }
        return false;
    }

    /**
     * Anzahl der Coins im Topf oder 0 wenn der Spielraum nicht gefunden wurde.
     *
     * @param spielraumname Name des Spielraums
     * @return Anzahl der Coins im Topf oder 0 wenn der Spielraum nicht gefunden wurde.
     */
    @Override
    public Integer getChipsImTopf(String spielraumname) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                return spiel.getChipsImTopf();
            }
        }
        return 0;
    }

    /**
     * Gibt die Anzahl an Punkte des Spielers aus oder 0, wenn der Spieler nicht existiert.
     *
     * @param spielraumname Name des Spielraums
     * @param name          Name des Spielers
     * @return Anzahl Punkte des Spielers oder 0, wenn der Spieler nicht existiert.
     */
    @Override
    public Integer getPunkte(String spielraumname, String name) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                for (Spieler spieler : spiel.getSpieler()) {
                    if (spieler.getName().equals(name)) {
                        return spieler.getPunkte();
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Gibt einen String aus, der die Namen der Spieler mit ihren jeweiligen Punkten enthält.
     *
     * @param spielraumname Name des Spielraums
     * @return String mit den Namen der Spieler und ihren Punkten.
     */
    @Override
    public String getPunkteAlle(String spielraumname) {
        for (Spiel spiel: this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                ArrayList<Spieler> spieler = new ArrayList<>(spiel.getSpieler());
                Collections.sort(spieler, new Comparator<Spieler>() {
                    @Override
                    public int compare(Spieler s1, Spieler s2) {
                        return s2.getPunkte() - s1.getPunkte();
                    }
                });
                String ausgabe = "";
                for (Spieler sp: spieler) {
                    ausgabe += sp.getName() + ": " + sp.getPunkte() +"\n";
                }
                return ausgabe;
            }
        }
        return "";
    }

    /**
     * gibt zurück, ob das Spiel gestartet wurde
     *
     * @param spielraumname name des Spielraums
     * @return ob Spiel gestartet wurde
     */
    @Override
    public Boolean spielGestartet(String spielraumname) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gibt true zurück, wenn das Spiel vorbei ist. False sonst.
     *
     * @param spielraumname Name des Spielraums
     * @return true, wenn Spiel vorbei, false sonst.
     */
    @Override
    public Boolean spielVorbei(String spielraumname) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                return spiel.getSpielVorbei();
            }
        }
        return false;
    }

    /**
     * Gibt die Namen aller Spieler im momentanen Spiel aus.
     *
     * @param spielraumname Name des Spielraums
     * @return ArrayList<> Name der Spieler im Spiel
     */
    @Override
    public ArrayList<Spieler> getSpielSpieler(String spielraumname) {
        for (Spiel spiel : this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                return spiel.getSpieler();
            }
        }
        return new ArrayList<>();
    }

    /**
     * löscht das aktuelle Spiel
     *
     * @param spielraumname Name des Spiels
     */
    @Override
    public void spielLoeschen(String spielraumname) {
        ArrayList<Spiel> spiele = new ArrayList<>(this.spiele);
        for (Spiel spiel: spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                this.spiele.remove(spiel);
            }
        }
    }

    /**
     * Setzt den Status eines Spiels auf abgeschlossen.
     *
     * @param spielraumname Name des Spielraums
     */
    @Override
    public void setSpielVorbei(String spielraumname) {
        for (Spiel spiel: this.spiele) {
            if (spiel.getSpielraumname().equals(spielraumname)) {
                spiel.setSpielVorbei(true);
            }
        }
    }

    @Override
    public Boolean raumVoll(String spielraumname) {
        for (Spielraum raum: this.spielraeume) {
            if (raum.getName().equals(spielraumname)) {
                if (raum.getSpieler().size() < 4) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }


    //Server Main
    //

    public static void main(String[] args) throws Exception {

        try {
            Server obj1 = new Server();
            Zugriffsverwaltung verwaltung = (Zugriffsverwaltung) UnicastRemoteObject.exportObject(obj1, 0);
            System.out.println("Server - main: Zugriffsverwaltung exportiert");

            Server obj2 = new Server();
            Spielraumverwaltung srverwaltung = (Spielraumverwaltung) UnicastRemoteObject.exportObject(obj2, 0);
            System.out.println("Server - main: Spielraumverwaltung exportiert");

            Server obj3 = new Server();
            Chatverwaltung chverwaltung = (Chatverwaltung) UnicastRemoteObject.exportObject(obj3, 0);
            System.out.println("Server - main: Chatverwaltung exportiert");

            Server obj4 = new Server();
            Leaderboard lboard = (Leaderboard) UnicastRemoteObject.exportObject(obj4, 0);
            System.out.println("Server - main: Chatverwaltung exportiert");

            Server obj5 = new Server();
            Spielverwaltung sverwaltung = (Spielverwaltung) UnicastRemoteObject.exportObject(obj5, 0);
            System.out.println("Server - main: Spielverwaltung exportiert");

            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("Server - main: Registry kreiert");

            registry.bind("Zugriffsverwaltung", verwaltung);
            System.out.println("Server - main: Zugriffsverwaltung registriert");

            registry.bind("Spielraumverwaltung", srverwaltung);
            System.out.println("Server - main: Spielraumverwaltung registriert");

            registry.bind("Chatverwaltung", chverwaltung);
            System.out.println("Server - main: Chatverwaltung registriert");

            registry.bind("Leaderboard", lboard);
            System.out.println("Server - main: Bestenlistenverwaltung registriert");

            registry.bind("Spielverwaltung", sverwaltung);
            System.out.println("Server - main: Spielverwaltung registriert");
        } catch (Exception e) {
            System.err.println("Server exception:");
            e.printStackTrace();
        }

        System.out.println("RMIServer - main: Terminierung");
    }

}
