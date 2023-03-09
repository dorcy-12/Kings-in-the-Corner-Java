package Server;

import Server.Spiel.Punktekonto;
import Server.Spiel.Spiel;
import Server.Spiel.Spieler;
import Server.Zugriffsverwaltung.Account;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void spieler_registrieren() throws Exception {
        Server test = new Server();

        //Registrieren mit richtigen Parametern
        try{
            test.spieler_registrieren("a", "a", "a");
            assertTrue(true);
        }catch (Exception e){
            assertTrue(false);
        }

        //Registrieren mit verschiedenen Passwörtern
        try{
            test.spieler_registrieren("b", "b", "c");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Passwörter stimmen nicht überein.", e.getMessage());
        }

        //Registrieren aber Benutzername schon vergeben
        try{
            test.spieler_registrieren("a", "a", "a");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Benutzername schon vergeben.", e.getMessage());
        }

        //Registrieren Registrieren mit unerlaubten Teilwort im Passwort
        try{
            test.spieler_registrieren("a", "fuck", "fuck");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Passwort Unerlaubtes Teilwort.", e.getMessage());
        }

        //Registrieren mit unerlaubten Teilwort im Benutzernamen
        try{
            test.spieler_registrieren("fuck", "a", "a");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Benutzername Unerlaubtes Teilwort.", e.getMessage());
        }

    }


    @Test
    void spieler_anmelden() throws Exception {
        Server test = new Server();

        test.spieler_registrieren("a", "a", "a");
        test.spieler_registrieren("b", "b", "b");

        //Anmeldedaten richtig
        try{
            test.spieler_anmelden("a", "a");
            assertTrue(true);
        }catch (Exception e){
            assertTrue(false);
        }

        //Bereitsangemeldet
        try{
            test.spieler_anmelden("a", "a");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Benutzer bereits angemeldet.", e.getMessage());
        }

        //Passwort falsch
        try{
            test.spieler_anmelden("b", "a");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Passwort falsch.", e.getMessage());
        }

        //Benutzer existiert nicht
        try{
            test.spieler_anmelden("c", "c");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Benutzername nicht vergeben.", e.getMessage());
        }
    }


    @Test
    void spieler_bearbeiten() throws Exception {
        Server test = new Server();

        test.spieler_registrieren("a", "a", "a");
        test.spieler_registrieren("c", "c", "c");

        //Passt alles
        try{
            test.spieler_bearbeiten("a", "b", "a", "a");
            assertTrue(true);
        }catch (Exception e){
            assertTrue(false);
        }

        //Benutzername schon vergeben
        try{
            test.spieler_bearbeiten("a", "c", "a", "a");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Benutzername schon vergeben.", e.getMessage());
        }

        try{
            test.spieler_bearbeiten("b", "d", "a", "b");
            fail();
        }catch (Exception e){
            assertEquals("Passwörter stimmen nicht überein.", e.getMessage());
        }

        try{
            test.spieler_bearbeiten("b", "d", "fuck", "fuck");
            fail();
        }catch (Exception e){
            assertEquals("Passwort Unerlaubtes Teilwort.", e.getMessage());
        }

        //
        try{
            test.spieler_bearbeiten("b", "fuck", "a", "a");
            fail();
        }catch (Exception e){
            assertEquals("Benutzername Unerlaubtes Teilwort.", e.getMessage());
        }

    }

    @Test
    void spieler_loeschen() throws Exception {
        Server test = new Server();

        test.spieler_registrieren("a", "a", "a");

        //fasches passwort
        try{
            test.spieler_loeschen("a", "b");
            fail();
        }catch (Exception e){
            assertEquals("Passwort falsch.", e.getMessage());
        }

        //Passt alles
        try{
            test.spieler_loeschen("a", "a");
            assertTrue(true);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void spieler_abmelden() throws Exception {
        Server test = new Server();

        test.spieler_registrieren("a", "a", "a");

        // Spieler abmelden testen
        try{
            test.spieler_abmelden("a");
            assertTrue(true);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void lobby_hinzufuegen() throws Exception {
        Server test = new Server();

        test.lobby_hinzufuegen("a");

        //lobby hinzufügen testen
        try{
            assertTrue(test.is_lobby("a"));
        }catch (Exception e){fail();}
    }

    @Test
    void lobby_entfernen() throws Exception {
        Server test = new Server();

        test.spieler_registrieren("a", "a", "a");
        test.lobby_hinzufuegen("a");

        //Spieler nicht in Lobby test
        try{
            test.lobby_entfernen("b");
        }catch(Exception e){
            assertEquals("Spieler ist nicht in Lobby", e.getMessage());
        }

        //Spieler Erfolgreich aus dem Lobby entfernt test.
        try{
            test.lobby_entfernen("a");
            assertTrue(true);
        }catch(Exception e){
            assertTrue(false);
        }

    }

    @Test
    void is_lobby() throws Exception {
        Server test = new Server();

        test.spieler_registrieren("a", "a", "a");
        test.lobby_hinzufuegen("a");

        //Ob spieler im Lobby testen
        assertTrue(test.is_lobby("a"));
        assertFalse(test.is_lobby("b"));
    }

    @Test
    void spielraum_erstellen() throws Exception {
        Server test = new Server();

        test.spieler_registrieren("a", "a", "a");
        test.spieler_registrieren("b", "b", "b");

        //Registrieren mit richtigen Parametern
        try{
            test.spielraum_erstellen("a", "a", "a", "a");
            assertTrue(true);
        }catch (Exception e){
            assertTrue(false);
        }

        //Unerlaubtes Teilwort Testen (Benutzername)
        try{
            test.spielraum_erstellen("b", "fuck", "a", "a");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Spielraumname Unerlaubtes Teilwort.", e.getMessage());
        }

        //Unerlaubtes Teilwort Testen (Passwort)
        try{
            test.spielraum_erstellen("b", "a", "fuck", "fuck");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Passwort Unerlaubtes Teilwort.", e.getMessage());
        }

        //Spielraumname ist schon vergeben test
        try{
            test.spielraum_erstellen("b", "a", "a", "a");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Spielraumname vergeben.", e.getMessage());
        }

        //Passwörter stimmen nicht überein
        try{
            test.spielraum_erstellen("b", "b", "b", "a");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Passwörter stimmen nicht überein.", e.getMessage());
        }


    }

    @Test
    void spieler_entfernen() throws Exception {
        Server test = new Server();

        Account a = new Account("a", "a");
        Account b = new Account("b", "b");

        test.lobby_hinzufuegen(a.getBenutzername());
        test.lobby_hinzufuegen(b.getBenutzername());

        test.spielraum_erstellen(a.getBenutzername(), "a", "a", "a");

        test.spielraum_beitreten("a", true,"a","a");
        test.spielraum_beitreten("b", true,"a","a");

        //Spieler entfernen funktioniert richtig
        try{
            test.spieler_entfernen(b.getBenutzername(), "a");
            assertTrue(true);
        }catch (Exception e){
            fail();
        }

        // Spieler nicht in diesem Spielraum Exception testen
        try{
            test.spieler_entfernen(b.getBenutzername(), "a");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Spieler nicht in diesem Spielraum.", e.getMessage());
        }

        //Spielraum existiert nicht Exception test
        try{
            test.spieler_entfernen(b.getBenutzername(), "b");
            assertTrue(false);
        }catch (Exception e){
            assertEquals("Spielraum existiert nicht.", e.getMessage());
        }



    }

    @Test
    public void spielraum_beitreten() throws Exception {
        Server test = new Server();

        test.lobby_hinzufuegen("a");
        test.lobby_hinzufuegen("b");
        test.lobby_hinzufuegen("c");
        test.lobby_hinzufuegen("d");

        test.spielraum_erstellen("e","f","t","t");

        //Erfolgreich Beitreten
        try {
            test.spielraum_beitreten("a",true,"f","t");
            test.spielraum_beitreten("b",true,"f","t");
            assertTrue(test.getRaumSpieler("f").contains("a"));
            assertTrue(test.getRaumSpieler("f").contains("b"));
        }catch(Exception e){
            fail();
        }

        //Passwort falsch
        try {
            test.spielraum_beitreten("c",true,"f","z");
            fail();
        }catch(Exception e){
            assertEquals("Passwort falsch.", e.getMessage());
        }

        //Spieler existiert nicht oder nicht in lobby.
        try {
            test.spielraum_beitreten("z",false,"f","t");
            fail();
        }catch(Exception e){
            assertEquals("Spieler ist nicht in Lobby oder existiert nicht.", e.getMessage());
        }

        //Spielraum existiert nicht
        try {
            test.spielraum_beitreten("d",true,"z","t");
            fail();
        }catch(Exception e){
            assertEquals("Spielraum existiert nicht.", e.getMessage());
        }

        test.spielraum_beitreten("c",true,"f","t");

        //Spielraum ist voll
        try{
            test.spielraum_beitreten("d",true,"f","t");
            fail();
        }catch (Exception e){
            assertEquals("Spielraum voll.", e.getMessage());
        }

    }

    @Test
    void bot_hinzufuegen() throws Exception {
        Server test = new Server();

        test.lobby_hinzufuegen("a");

        test.spielraum_erstellen("a","f","t","t");

        test.bot_hinzufuegen("b","f");

        assertTrue(test.getSpielraeume().get(0).getSpieler().get(1).equals("b"));

    }

    @Test
    void spielraum_verlassen() throws Exception {
        Server test = new Server();

        test.lobby_hinzufuegen("b");

        test.spielraum_erstellen("a","f","t","t");
        test.spielraum_beitreten("b",true,"f","t");



        test.spielraum_verlassen("a","f");
        assertFalse(test.getRaumSpieler("f").contains("a"));


    }

    @Test
    void getRaumSpieler() throws Exception {
        Server test = new Server();

        test.lobby_hinzufuegen("a");
        test.lobby_hinzufuegen("b");
        test.lobby_hinzufuegen("c");

        test.spielraum_erstellen("a","f","t","t");

        test.spielraum_beitreten("b",true,"f","t");
        test.spielraum_beitreten("c",true,"f","t");

        // Anzahl von Spieler im Spielraum Test
        try{
            assertEquals(3,test.getRaumSpieler("f").size());
        }catch (Exception e ){fail();}

    }

    @Test
    void spiel_starten() throws Exception {
        Server test = new Server();

        Account a = new Account("a","aa");
        Account b = new Account("b","bb");
        Account c = new Account("c","cc");

        test.lobby_hinzufuegen("a");
        test.lobby_hinzufuegen("b");
        test.lobby_hinzufuegen("c");

        test.spielraum_erstellen("a","f","t","t");

        //nicht ausreichende Spieler Test
        try {
            test.spiel_starten("f");
            fail();
        }catch (Exception e){
            assertEquals("Nicht genug Spieler in Spielraum.", e.getMessage());
        }

        test.spielraum_beitreten("b", true,"f","t");
        test.spielraum_beitreten("c", true,"f","t");

        //Spielraum existiert nicht
        try {
            test.spiel_starten("z");
            fail();
        }catch (Exception e){
            assertEquals("Spielraum existiert nicht.", e.getMessage());
        }

        // Erfolgreich Spiel Starten
        try {
            test.spiel_starten("f");
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }

    }
    @Test
    void getSpielraumChatverlauf() throws Exception {
        Server test = new Server();

        test.spielraum_erstellen("a","f","t","t");
        test.spielraum_erstellen("b","g","x","x");

        //Nachricht in SpielraumChatverlauf
        try{
            test.neue_spielraum_nachricht("Gut gespielt","a","f");
            assertTrue(test.getSpielraumChatverlauf("f").contains("a: Gut gespielt"));
        }catch (Exception e){fail();}

        //Jede Spielraum besitzt eine eigen Spielraumchatverlauf
        test.neue_spielraum_nachricht("GG","b","g");
        assertFalse(test.getSpielraumChatverlauf("f").contains("a: GG"));

    }
    @Test
    void rename_punktekonto() throws Exception{
        Server test = new Server();

        //testen richtig funktioniert

        ArrayList<Punktekonto> punkte = new ArrayList<>();
        punkte.add(new Punktekonto("a",0,0,0));
        punkte.add(new Punktekonto("b",0,0,0));
        punkte.add(new Punktekonto("c",0,0,0));

        test.setBestenliste(punkte);

        test.rename_punktekonto("a","l");

        assertEquals("l", test.getBestenliste().get(0).getName());

    }

    @Test
    void neue_spielraum_nachricht() throws Exception{
        Server test = new Server();

        test.spielraum_erstellen("a","f","t","t");

        //Nachricht enthält unerlaubte Teilworte
        try{
            test.neue_spielraum_nachricht("Fuck","a","f");
        }catch (Exception e){
            assertEquals("Nachricht enthält unerlaubte Teilworte.",e.getMessage());
        }

        //Nachricht erhalten
        try{
            test.neue_spielraum_nachricht("Gut gespielt","a","f");
            assertTrue(test.getSpielraumChatverlauf("f").contains("a: Gut gespielt"));
        }catch (Exception e){fail();}



    }

    @Test
    void neue_nachricht() throws Exception {
        Server test = new Server();

        //Nachricht enthält unerlaubte Teilworte
        try {
            test.neue_nachricht("scheisse","a");
            fail();
        }catch (Exception e){assertEquals("Unerlaubtes Teilwort.", e.getMessage());}

        //Nachricht erhalten
        try {
            test.neue_nachricht("Kings in the Corner","a");
            assertTrue(test.getChatverlauf().contains("a: Kings in the Corner"));
        }catch(Exception e ){fail();}
    }



    @Test
    void top_10_ausgeben() throws Exception {
        Server test = new Server();

        ArrayList<Punktekonto> beste = new ArrayList<>();
        beste.add(new Punktekonto("a",0,0,10));
        beste.add(new Punktekonto("b",0,0,40));
        beste.add(new Punktekonto("c",0,0,30));

        test.setBestenliste(beste);

        //testen wenn liste < 10
        assertEquals("b: 40\nc: 30\na: 10", test.top_10_ausgeben());

        beste.add(new Punktekonto("d",0,0,60));
        beste.add(new Punktekonto("e",0,0,25));
        beste.add(new Punktekonto("f",0,0,1));
        beste.add(new Punktekonto("g",0,0,90));
        beste.add(new Punktekonto("h",0,0,33));
        beste.add(new Punktekonto("i",0,0,44));
        beste.add(new Punktekonto("j",0,0,66));
        beste.add(new Punktekonto("k",0,0,77));

        //testen wenn liste > 10
        assertEquals("g: 90\nk: 77\nj: 66\nd: 60\ni: 44\nb: 40\nh: 33\nc: 30\ne: 25\na: 10",
                test.top_10_ausgeben());
    }


    @Test
    void neues_punktekonto() throws Exception {
        Server test = new Server();

        try{
            test.neues_punktekonto("a");
            assertTrue(test.get_punktekonto("a").stream().allMatch(x -> x == 0));
        }catch (Exception e){fail();}
    }

    @Test
    void punktekonto_loeschen() throws Exception {
        Server test = new Server();

        test.neues_punktekonto("a");

        test.punktekonto_loeschen("a");

        assertNull(test.get_punktekonto("a"));

    }

    @Test
    void get_punktekonto() throws Exception {
        Server test = new Server();

        //Keine Konto ist vorhanden Test
        try {
            test.get_punktekonto("f");
        }catch (Exception e){assertNull(e);}

        // Erfolgreich getter test
        test.neues_punktekonto("a");
        test.neues_punktekonto("b");
        try{
            assertTrue(test.get_punktekonto("a").stream().allMatch(x -> x == 0));
            assertTrue(test.get_punktekonto("b").stream().allMatch(x -> x == 0));
        }catch (Exception e){fail();}
    }

    @Test
    void bestenliste_anpassen() throws Exception {
        Server test = new Server();

        ArrayList<Punktekonto> beste = new ArrayList<>();
        beste.add(new Punktekonto("a",0,0,10));
        beste.add(new Punktekonto("b",0,0,40));
        beste.add(new Punktekonto("c",0,0,30));

        ArrayList<Spieler> spieler = new ArrayList<>();
        spieler.add(new Spieler("a",new ArrayList<String>(),0));
        spieler.add(new Spieler("c",new ArrayList<String>(),0));

        spieler.get(0).setPunkte(5);
        spieler.get(1).setPunkte(100);


        test.setBestenliste(beste);

        test.bestenliste_anpassen(spieler);

        //testen ob bestenliste angepasst wird.
        assertEquals(15,test.getBestenliste().get(0).getPunkte());
        assertEquals(1,test.getBestenliste().get(0).getGespielte_spiele());
        assertEquals(0,test.getBestenliste().get(0).getGewonnene_spiele());

        //testen gewonnene Spiele
        assertEquals(130,test.getBestenliste().get(2).getPunkte());
        assertEquals(1,test.getBestenliste().get(2).getGespielte_spiele());
        assertEquals(1,test.getBestenliste().get(2).getGewonnene_spiele());
    }
    @Test
    void isHost() throws Exception{
        Server test = new Server();

        test.spielraum_erstellen("a","f","t","t");

        //testen isHost mit einem falsche Name
        assertFalse(test.isHost("b","f"));

        //testen isHost mit richtigen Name
        assertTrue(test.isHost("a","f"));

        //testen isHost wenn spielraum nicht existiert
        assertFalse(test.isHost("a","d"));

    }

    //ich muss get handkarte und chips hier testen
    @Test
    void spielStarten() throws Exception {
        Server test = new Server();

        test.spielraum_erstellen("a","f","t","t");

        ArrayList<String> spieler = new ArrayList<String>(Collections.singletonList("a"));

        //testen spiel starten mit wenig spieler
        try{
            test.spielStarten("f",spieler);
            fail();
        }
        catch(Exception e){
            assertEquals("Zu wenige Spieler", e.getMessage());
        }

        spieler.add("b");
        spieler.add("c");
        test.spielStarten("f",spieler);

        //testen ob anzahl von chips richtig sind
        for (String sp: spieler){
            assertEquals(25, test.getChips("f",sp));
        }

        //testen ob anzahl von Handkarten richtig sind
        for (String sp: spieler){
            assertEquals(7, test.getHandkarten("f",sp).size());
        }

        //testen ob die 4 Stapel eine Karte Haben
        assertEquals(2, test.getStapel("f").get(1).size()); //obigen Stapel
        assertEquals(2, test.getStapel("f").get(3).size());//linke Stapel
        assertEquals(2, test.getStapel("f").get(4).size());//rechte Stapel
        assertEquals(2, test.getStapel("f").get(6).size());//unteren Stapel
    }


    @Test
    void karteZiehen() throws Exception{
        Server test = new Server();

        test.spielraum_erstellen("a","f","t","t");
        test.spielraum_beitreten("b",true,"f","t");
        test.spielraum_beitreten("c",true,"f","t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a","b","c"));

        test.spielStarten("f",spielers);
        Integer chipsimTopf = test.getChipsImTopf("f");

        ArrayList<String> karten = new ArrayList<>(Arrays.asList("HerzDame","Pik4","Kreuz5","PikKoenig"));

        ArrayList<String> nachzieh = new ArrayList<>(Arrays.asList("HerzDame","PikKoenig","Kreuz5"));

        //testen karteziehen mit einem Koenig auf der Hand
        test.getSpiele().get(0).getSpieler().get(0).setHandkarten(karten);
        test.getSpiele().get(0).setNachziehstapel(nachzieh);

        test.karteZiehen("f","a");

        assertEquals(chipsimTopf + 3, test.getChipsImTopf("f"));

        //testen kartziehen mit einem Koenig als die gezogene Karte
        test.getSpiele().get(0).getSpieler().get(0).setHandkarten(karten);

        test.karteZiehen("f","b");

        assertTrue(test.getStapel("f").get(0).contains("PikKoenig"));
    }

    @Test
    void koenigAufHand() throws Exception{
        Server test = new Server();

        test.spielraum_erstellen("a","f","t","t");
        test.spielraum_beitreten("b",true,"f","t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a","b"));

        test.spielStarten("f",spielers);

        //testen mit einem Koenig auf Hand
        for(String sp: spielers){
            boolean hasKing = false;
            for(String karte: test.getHandkarten("f",sp)){
                if (karte.contains("Koenig")) {
                    hasKing = true;
                    break;
                }
            }
            assertEquals(hasKing, test.koenigAufHand("f",sp));
        }


    }

    //karte spielen muss oben sein
    @Test
    void amZug() throws Exception {
        Server test = new Server();

        test.spielraum_erstellen("a","f","t","t");
        test.spielraum_beitreten("b",true,"f","t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a","b"));

        test.spielStarten("f",spielers);

        //testen ob am Zug richtig funktioniert
        test.karteZiehen("f","a");
        assertFalse(test.amZug("f","a"));

    }

    @Test
    void karteSpielen() throws Exception {
        Server test = new Server();

        test.spielraum_erstellen("a","f","t","t");
        test.spielraum_beitreten("b",true,"f","t");
        test.spielraum_beitreten("c",true,"f","t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a","b","c"));

        test.spielStarten("f",spielers);

        test.getSpiele().get(0).getSpieler().get(1).setHandkarten(new ArrayList<>(Arrays.asList("Kreuz2","Karo4","Pik5","Karo9","Pik8","Karo8")));
        test.getSpiele().get(0).setStapelOben(new ArrayList<String>(Arrays.asList("PikAss","PikAss")));


        //testen kartespielen mit gleiche farbe
        test.karteSpielen("f","b","Kreuz2","oben");
        assertFalse(test.getStapel("f").get(1).contains("Kreuz2"));

        //Kartenspiele mit falscher Wertereihenfolge testen

        test.karteSpielen("f","b","Karo4","oben");
        assertFalse(test.getStapel("f").get(1).contains("Karo4"));


        //testen Karte auf dem falschen Stapel
        test.karteSpielen("f","b","Pik5","obenLinks");
        assertFalse(test.getStapel("f").get(0).contains("Pik5"));

    }


    @Test
    void getHandKarten() throws Exception{
        Server test = new Server();

        test.spielraum_erstellen("a", "f", "t", "t");
        test.spielraum_beitreten("b", true, "f", "t");
        test.spielraum_beitreten("c", true, "f", "t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a", "b", "c"));

        test.spielStarten("f", spielers);

        //testen wenn Spieler nicht existiert
        assertEquals(0, test.getHandkarten("f","z").size());

        //testen wenn Spielraum nicht existiert
        assertEquals(0, test.getHandkarten("d","a").size());

        //testen alles funktioniert
        assertEquals(7, test.getHandkarten("f","a").size());
        assertEquals(7, test.getHandkarten("f","b").size());
        assertEquals(7, test.getHandkarten("f","c").size());

    }

    @Test
    void KartenSpielenMoeglich() throws Exception{
        Server test = new Server();

        //testen wert reihenfolge
        assertFalse (test.karteSpielenMoeglich("karo4","Pik3"));
        assertFalse (test.karteSpielenMoeglich("Kreuz10","Karo6"));
        assertFalse (test.karteSpielenMoeglich("KreuzKoenig","HerzBube"));
        assertFalse (test.karteSpielenMoeglich("karo4","HerzAss"));

        //testen farbe reihenfolge
        assertFalse (test.karteSpielenMoeglich("karo3","Herz4"));
        assertFalse (test.karteSpielenMoeglich("KreuzBube","PikKoenig"));
        assertFalse (test.karteSpielenMoeglich("KreuzAss","Pik2"));
        assertFalse (test.karteSpielenMoeglich("Pik6","Pik7"));
        assertFalse (test.karteSpielenMoeglich("Pik6","PikBube"));
        assertFalse (test.karteSpielenMoeglich("PikBube","KaroBube"));

        //testen richtige reihenFolge
        assertTrue (test.karteSpielenMoeglich("Karo4","Pik5"));
        assertTrue (test.karteSpielenMoeglich("Pik9","Herz10"));
        assertTrue (test.karteSpielenMoeglich("Kreuz8","Karo9"));
        assertTrue (test.karteSpielenMoeglich("HerzDame","KreuzKoenig"));
        assertTrue (test.karteSpielenMoeglich("HerzBube","KreuzDame"));
        assertTrue (test.karteSpielenMoeglich("Herz10","KreuzBube"));
        assertTrue (test.karteSpielenMoeglich("Herz9","Kreuz10"));
        assertTrue (test.karteSpielenMoeglich("Herz8","Kreuz9"));
        assertTrue (test.karteSpielenMoeglich("Herz7","Kreuz8"));
        assertTrue (test.karteSpielenMoeglich("Herz6","Kreuz7"));
        assertTrue (test.karteSpielenMoeglich("Herz5","Kreuz6"));
        assertTrue (test.karteSpielenMoeglich("Herz4","Kreuz5"));
        assertTrue (test.karteSpielenMoeglich("Herz3","Kreuz4"));
        assertTrue (test.karteSpielenMoeglich("Herz2","Kreuz3"));
        assertTrue (test.karteSpielenMoeglich("HerzAss","Kreuz2"));
        assertTrue (test.karteSpielenMoeglich("Karo7","Kreuz8"));
        assertTrue (test.karteSpielenMoeglich("Karo6","Kreuz7"));
        assertTrue (test.karteSpielenMoeglich("Karo5","Kreuz6"));
        assertTrue (test.karteSpielenMoeglich("Karo4","Kreuz5"));
        assertTrue (test.karteSpielenMoeglich("Karo3","Kreuz4"));
        assertTrue (test.karteSpielenMoeglich("Karo2","Kreuz3"));
        assertTrue (test.karteSpielenMoeglich("KaroAss","Kreuz2"));
        assertTrue (test.karteSpielenMoeglich("HerzDame","PikKoenig"));
        assertTrue (test.karteSpielenMoeglich("HerzBube","PikDame"));
        assertTrue (test.karteSpielenMoeglich("Herz10","PikBube"));
        assertTrue (test.karteSpielenMoeglich("Herz9","Pik10"));
        assertTrue (test.karteSpielenMoeglich("Herz8","Pik9"));
        assertTrue (test.karteSpielenMoeglich("Herz7","Pik8"));
        assertTrue (test.karteSpielenMoeglich("Herz6","Pik7"));
        assertTrue (test.karteSpielenMoeglich("Herz5","Pik6"));
        assertTrue (test.karteSpielenMoeglich("Herz4","Pik5"));
        assertTrue (test.karteSpielenMoeglich("Herz3","Pik4"));
        assertTrue (test.karteSpielenMoeglich("Herz2","Pik3"));
        assertTrue (test.karteSpielenMoeglich("HerzAss","Pik2"));
        assertTrue (test.karteSpielenMoeglich("Karo7","Pik8"));
        assertTrue (test.karteSpielenMoeglich("Karo6","Pik7"));
        assertTrue (test.karteSpielenMoeglich("Karo5","Pik6"));
        assertTrue (test.karteSpielenMoeglich("Karo4","Pik5"));
        assertTrue (test.karteSpielenMoeglich("Karo3","Pik4"));
        assertTrue (test.karteSpielenMoeglich("Karo2","Pik3"));
        assertTrue (test.karteSpielenMoeglich("KaroAss","Pik2"));


    }

    @Test
    void stapelVerschieben() throws Exception {
            Server test = new Server();

            test.spielraum_erstellen("a", "f", "t", "t");
            test.spielraum_beitreten("b", true, "f", "t");
            test.spielraum_beitreten("c", true, "f", "t");

            ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a", "b", "c"));

            test.spielStarten("f", spielers);

            ArrayList<String> stapelOben = new ArrayList<String>();
            stapelOben.add("Pik4");
            stapelOben.add("Pik4");
            test.getSpiele().get(0).setStapelOben(stapelOben);

            ArrayList<String> ObenRechts = new ArrayList<String>();
            ObenRechts.add("Karo5");
            ObenRechts.add("Karo5");
            test.getSpiele().get(0).setStapelObenRechts(ObenRechts);

            ArrayList<String> Rechts = new ArrayList<String>();
            Rechts.add("Herz6");
            Rechts.add("Herz6");
            test.getSpiele().get(0).setStapelMitteRechts(Rechts);

            //testen ob es richtig funktioniert
             test.stapelVerschieben("f", "oben", "obenRechts");
             assertEquals(0,test.getSpiele().get(0).getStapelOben().size());
             assertEquals("Pik4",test.getSpiele().get(0).getStapelObenRechts().get(0));


            //testen StapelVerschieben mit zwei Stapel die nicht zueinander passen.
            test.stapelVerschieben("f", "Rechts", "ObenRechts");
            assertEquals(2,test.getSpiele().get(0).getStapelMitteRechts().size());
            assertEquals("Pik4",test.getSpiele().get(0).getStapelObenRechts().get(0));
    }

    @Test
    void clearStapel() throws Exception {
        Server test = new Server();

        test.spielraum_erstellen("a", "f", "t", "t");
        test.spielraum_beitreten("b", true, "f", "t");
        test.spielraum_beitreten("c", true, "f", "t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a", "b", "c"));

        test.spielStarten("f", spielers);

        //testen ob richtig funktioniert
        test.clearStapel("f","oben");
        assertEquals(0, test.getStapel("f").get(1).size());

        test.clearStapel("f","unten");
        assertEquals(0, test.getStapel("f").get(6).size());

        test.clearStapel("f","rechts");
        assertEquals(0, test.getStapel("f").get(4).size());

        test.clearStapel("f","links");
        assertEquals(0, test.getStapel("f").get(3).size());

    }

    @Test
    void zahleChip() throws Exception {

        Server test = new Server();

        test.spielraum_erstellen("a", "f", "t", "t");
        test.spielraum_beitreten("b", true, "f", "t");
        test.spielraum_beitreten("c", true, "f", "t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a", "b", "c"));

        test.spielStarten("f", spielers);

        //testen ob die Chips im Topf sich erhöht
        test.zahleChip("f","a",4);
        assertEquals(21, test.getChips("f","a"));

        test.zahleChip("f","b",20);
        assertEquals(5, test.getChips("f","b"));

    }

    @Test
    void handkartenSortieren() throws Exception {
        Server test = new Server();

        ArrayList<String> spielkarten = new ArrayList<String>(Arrays.asList("HerzAss", "Herz2", "Herz3", "Herz4", "Herz5", "Herz6", "Herz7", "Herz8", "Herz9", "Herz10", "HerzBube", "HerzDame", "HerzKoenig",
                "PikAss", "Pik2", "Pik3", "Pik4", "Pik5", "Pik6", "Pik7", "Pik8", "Pik9", "Pik10", "PikBube", "PikDame", "PikKoenig",
                "KaroAss", "Karo2", "Karo3", "Karo4", "Karo5", "Karo6", "Karo7", "Karo8", "Karo9", "Karo10", "KaroBube", "KaroDame", "KaroKoenig",
                "KreuzAss", "Kreuz2", "Kreuz3", "Kreuz4", "Kreuz5", "Kreuz6", "Kreuz7", "Kreuz8", "Kreuz9", "Kreuz10", "KreuzBube", "KreuzDame", "KreuzKoenig"));

        ArrayList<String> spielkarten2 = new ArrayList<String>(Arrays.asList("HerzAss", "PikAss", "KaroAss", "KreuzAss", "Herz2", "Pik2", "Karo2", "Kreuz2",
                "Herz3", "Pik3", "Karo3", "Kreuz3", "Herz4", "Pik4", "Karo4", "Kreuz4", "Herz5", "Pik5", "Karo5", "Kreuz5", "Herz6", "Pik6", "Karo6", "Kreuz6",
                "Herz7", "Pik7", "Karo7", "Kreuz7", "Herz8", "Pik8", "Karo8", "Kreuz8", "Herz9", "Pik9", "Karo9", "Kreuz9", "Herz10", "Pik10", "Karo10", "Kreuz10",
                "HerzBube", "PikBube", "KaroBube", "KreuzBube", "HerzDame", "PikDame", "KaroDame", "KreuzDame", "HerzKoenig", "PikKoenig", "KaroKoenig", "KreuzKoenig"));

        test.spielraum_erstellen("a", "f", "t", "t");
        test.spielraum_beitreten("b", true, "f", "t");
        test.spielraum_beitreten("c", true, "f", "t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a", "b", "c"));

        test.spielStarten("f", spielers);

        ArrayList<String> wert = test.getHandkarten("f","a");

        //testen farblich sortiert
        wert.sort(Comparator.comparingInt(spielkarten::indexOf));
        test.handkartenSortieren("f","a",false);
        assertEquals(wert,test.getHandkarten("f","a"));

        //testen aufsteigend sortiert
        wert.sort(Comparator.comparingInt(spielkarten2::indexOf));
        test.handkartenSortieren("f","a",true);
        assertEquals(wert,test.getHandkarten("f","a"));

    }

    @Test
    void neue_spiel_nachricht() throws Exception {
        Server test = new Server();

        test.spielraum_erstellen("a", "f", "t", "t");
        test.spielraum_beitreten("b", true, "f", "t");
        test.spielraum_beitreten("c", true, "f", "t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a", "b", "c"));

        test.spielStarten("f", spielers);

        //Nachricht enthält unerlaubte Teilworte
        try {
            test.neue_spiel_nachricht("scheisse","a","f");
            fail();
        }catch (Exception e){assertEquals("Nachricht enthält unerlaubte Teilworte.", e.getMessage());}

        //Nachricht erhalten
        try {
            test.neue_spiel_nachricht("Kings in the Corner","a","f");
            assertTrue(test.getSpielChatverlauf("f").contains("a: Kings in the Corner"));
        }catch(Exception e ){fail();}
    }

    @Test
    void spielVerlassen() throws Exception{
        Server test = new Server();

        test.spielraum_erstellen("a", "f", "t", "t");
        test.spielraum_beitreten("b", true, "f", "t");
        test.spielraum_beitreten("c", true, "f", "t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a", "b", "c"));

        test.spielStarten("f", spielers);

        ArrayList<String> akarten = test.getHandkarten("f","a");

        ArrayList<String> bkarten = test.getHandkarten("f","b");

        ArrayList<String> ckarten = test.getHandkarten("f","c");

        //testen ob es richtig funktioniert

        test.spielVerlassen("f","a");
        assertFalse(test.getSpielSpieler("f").contains(new Spieler("a",akarten,25)));

        test.spielVerlassen("f","b");
        assertFalse(test.getSpielSpieler("f").contains(new Spieler("b",bkarten,25)));


    }


    @Test
    void nachziehstapelLeer() throws Exception {
        Server test = new Server();

        test.spielraum_erstellen("a", "p", "t", "t");
        test.spielraum_beitreten("b", true, "p", "t");
        test.spielraum_beitreten("c", true, "p", "t");

        ArrayList<String> spieler = new ArrayList<String>(Arrays.asList("a", "b", "c"));
        ArrayList<String> nkarten = new ArrayList<>();

        test.spielStarten("p", spieler);

        for(int i =0; i <12;i++){
            test.karteZiehen("p","c");
        }

        for(int i =0; i <12;i++){
            test.karteZiehen("p","a");
        }

        for(int i =0; i <3;i++){
            test.karteZiehen("p","b");
        }

        //spiel.getNachziehstapel();


        //testen ob richtig funktioniert
        assertFalse(test.nachziehstapelLeer("f"));

        assertTrue(test.nachziehstapelLeer("p"));

    }

    @Test
    void spielGestartet() throws Exception {
        Server test = new Server();

        test.spielraum_erstellen("a", "f", "t", "t");
        test.spielraum_beitreten("b", true, "f", "t");
        test.spielraum_beitreten("d", true, "f", "t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a","b","c"));

        test.spielraum_erstellen("d", "p", "t", "t");
        test.spielraum_beitreten("e", true, "p", "t");

        ArrayList<String> spielers2 = new ArrayList<String>(Arrays.asList("d","e"));

        test.spielStarten("f",spielers);

        //Testen ob die Methode funktioniert mit richtiger Ausgabe
        assertTrue(test.spielGestartet("f"));

        assertFalse(test.spielGestartet("p"));

    }

    @Test
    void spielVorbei() throws Exception {
        Server test = new Server();
        ArrayList<String> hkarten = new ArrayList<String>(Arrays.asList(""));

        test.spielraum_erstellen("c", "f", "t", "t");
        test.spielraum_beitreten("e", true, "f", "t");
        test.spielraum_beitreten("f", true, "f", "t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("c","e","f"));


        test.spielStarten("f",spielers);

        test.getSpiele().get(0).getSpieler().get(0).setPunkte(120);

        test.neueRunde("f","c",0);

        assertTrue(test.spielVorbei("f"));

        //testen Wenn alle Spieler weniger als100 punkte haben
    }

    @Test
    void spielLoeschen() throws Exception {

        Server test = new Server();
        ArrayList<String> hkarten = new ArrayList<String>(Arrays.asList("a","b","c"));
        test.spielraum_erstellen("a", "f", "t", "t");
        test.spielraum_beitreten("b", true, "f", "t");
        test.spielraum_beitreten("d", true, "f", "t");

        test.spielraum_erstellen("a", "p", "t", "t");
        test.spielraum_beitreten("b", true, "p", "t");
        test.spielraum_beitreten("d", true, "p", "t");

        ArrayList<String> spielers = new ArrayList<String>(Arrays.asList("a","b","c"));


        test.spielStarten("f",spielers);
        test.spielStarten("p",spielers);

        test.spielLoeschen("p");

        assertEquals(0,test.getSpielSpieler("p").size());

    }
    @Test
    void getPunktAlle() throws Exception{
        Server test = new Server();

        test.spielraum_erstellen("a", "p", "t", "t");
        test.spielraum_beitreten("b", true, "p", "t");
        test.spielraum_beitreten("c", true, "p", "t");

        ArrayList<String> spieler = new ArrayList<String>(Arrays.asList("a", "b", "c"));


        test.spielStarten("p", spieler);

        test.getSpiele().get(0).getSpieler().get(0).setPunkte(10);
        test.getSpiele().get(0).getSpieler().get(1).setPunkte(30);
        test.getSpiele().get(0).getSpieler().get(2).setPunkte(20);

        assertEquals("b: 30\nc: 20\na: 10\n", test.getPunkteAlle("p"));
    }
}