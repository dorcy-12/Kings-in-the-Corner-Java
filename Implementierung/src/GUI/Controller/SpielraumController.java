package GUI.Controller;

import GUI.Klassen.Ingame;
import GUI.Klassen.Lobby;
import Server.Spiel.Bot;
import Server.Spiel.Leaderboard;
import Server.Spiel.Spielraumverwaltung;
import Server.Spiel.Spielverwaltung;
import Server.Zugriffsverwaltung.Zugriffsverwaltung;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SpielraumController {

    private Registry registry;
    private Spielraumverwaltung srverwaltung;

    private Zugriffsverwaltung zgverwaltung;

    private Spielverwaltung sverwaltung;

    private Leaderboard leaderboard;
    private String name = "";

    private String raumname = "";

    private Boolean stop = false;

    @FXML
    private TextArea chat;

    @FXML
    private TextField errorFeld;

    @FXML
    private Button botEntfernen;

    @FXML
    private Button botHinzufuegen;

    @FXML
    private TextField punkte;

    @FXML
    private TextField gespielteSpiele;

    @FXML
    private TextField gewonneneSpiele;

    @FXML
    private TextField neueNachricht;


    @FXML
    private Button raumVerlassen;

    @FXML
    private Button spielStarten;

    @FXML
    private TextArea gespielteSpieleTextfeld;


    @FXML
    private TextArea gewonneneSpieleTextfeld;


    @FXML
    private TextArea punkteTextfeld;

    @FXML
    private TextField spieler;

    @FXML
    private TextArea spielerTextfeld;

    public SpielraumController() throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry();
        this.srverwaltung = (Spielraumverwaltung) registry.lookup("Spielraumverwaltung");
        this.zgverwaltung = (Zugriffsverwaltung) registry.lookup("Zugriffsverwaltung");
        this.leaderboard = (Leaderboard) registry.lookup("Leaderboard");
        this.sverwaltung = (Spielverwaltung) registry.lookup("Spielverwaltung");
    }


    /**
     * Wenn Enter gedrückt wird und man im Textfeld etwas geschrieben hat, wird
     * das geschriebene in den chatverlauf mit aufgenommen.
     * @param event
     * @throws Exception
     */
    @FXML
    void neueNachrichtAbschicken(KeyEvent event) throws Exception {
        if (event.getCode().equals(KeyCode.ENTER)) {
            try {
                errorFeld.setVisible(false);
                String nachricht = neueNachricht.getText();
                srverwaltung.neue_spielraum_nachricht(nachricht, name, raumname);
                TimeUnit.MILLISECONDS.sleep(5);
                neueNachricht.clear();
            } catch (Exception e) {
                errorFeld.setVisible(true);
                errorFeld.setText(e.getMessage());
            }
        }

    }

    /**
     * Wenn der Raumverlassen-Button geklickt wird, schließt sich das Spielraum-Fenster und
     * das Lobby-Fenster öffnet sich.
     * @param event
     * @throws Exception
     */
    @FXML
    void raumVerlassenButtonPressed(ActionEvent event) throws Exception {
        try {
            srverwaltung.spielraum_verlassen(name, raumname);
            zgverwaltung.lobby_hinzufuegen(name);
            Stage stage = (Stage) raumVerlassen.getScene().getWindow();
            stage.close();
            Lobby lob = new Lobby();
            lob.setName(name);
            lob.start(new Stage());
            stop = true;
        } catch (Exception e) {
            errorFeld.setVisible(true);
            errorFeld.setText(e.getMessage());
            errorFeld.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        }
    }

    /**
     * Wenn der Spielstarten-Button betätigt wird startet das Spiel.
     * Das Spiel-Fenster öffnet sich und das Spielraum-Fenster schließt.
     * @param event
     * @throws Exception
     */
    @FXML
    void spielStartenButtonPressed(ActionEvent event) throws Exception {
        if (srverwaltung.isHost(name, raumname)) {
            try {
                if (!sverwaltung.spielGestartet(raumname)) {
                    sverwaltung.spielStarten(raumname, srverwaltung.getRaumSpieler(raumname));
                    Stage stage = (Stage) spielStarten.getScene().getWindow();
                    stage.close();
                    Ingame ing = new Ingame();
                    ing.setName(name);
                    ing.setRaumname(raumname);
                    ing.start(new Stage());
                    stop = true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                errorFeld.setVisible(true);
                errorFeld.setText(e.getMessage());
                errorFeld.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            }
        } else {
            try {
                if (sverwaltung.spielGestartet(raumname)) {
                    Stage stage = (Stage) spielStarten.getScene().getWindow();
                    stage.close();
                    Ingame ing = new Ingame();
                    ing.setName(name);
                    ing.setRaumname(raumname);
                    ing.start(new Stage());
                    stop = true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                errorFeld.setVisible(true);
                errorFeld.setText(e.getMessage());
                errorFeld.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            }
        }
    }

    @FXML
    void errorAnzeigen(ActionEvent event) {
        errorFeld.setVisible(true);
        errorFeld.setText("Error.");
    }


    /**
     * Der Chatverlauf updatet sich wiederholt umso den aktualisierten Chatverlauf
     * in der Textarea chatverlauf im Fenster der Lobby anzeigen lassen zu können.
     * @throws Exception
     */
    public void chatUpdate() throws Exception {

        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200),
                event -> {
                    try {
                        chat.setText(srverwaltung.getSpielraumChatverlauf(raumname));
                        if (stop) {
                            timeLine.stop();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        timeLine.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(200)));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Die Bestenliste updatet sich immer umso die besten Spieler
     * in der Textarea leaderboard anzeigen lassen zu können.
     * @throws Exception
     */
    public void bestenlisteUpdate() throws Exception {

        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200),
                event -> {
                    try {
                        if (!stop) {
                            String spielerTextfeldInhalt = "";
                            String gespielteSpieleInhalt = "";
                            String gewonneneSpieleInhalt = "";
                            String punkteInhalt = "";
                            ArrayList<String> raumspieler = srverwaltung.getRaumSpieler(raumname);
                            for (String sp : raumspieler) {
                                ArrayList<Integer> punkte = leaderboard.get_punktekonto(sp);
                                spielerTextfeldInhalt += sp + "\n";
                                gespielteSpieleInhalt += String.valueOf(punkte.get(0)) + "\n";
                                gewonneneSpieleInhalt += String.valueOf(punkte.get(1)) + "\n";
                                punkteInhalt += String.valueOf(punkte.get(2)) + "\n";
                                spielerTextfeld.setText(spielerTextfeldInhalt);
                                gespielteSpieleTextfeld.setText(gespielteSpieleInhalt);
                                gewonneneSpieleTextfeld.setText(gewonneneSpieleInhalt);
                                punkteTextfeld.setText(punkteInhalt);

                            }
                        }
                        else {
                            timeLine.stop();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        timeLine.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(200)));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    public void gestartetUpdate() throws Exception {

        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200),
                event -> {
                    try {
                        if (sverwaltung.spielGestartet(raumname)) {
                            if (!sverwaltung.spielVorbei(raumname)) {
                                spielStarten.fire();
                                timeLine.stop();
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    });
        timeLine.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(200)));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    @FXML
    void botEntfernenButtonPressed(ActionEvent event) throws Exception {
        srverwaltung.bot_entfernen(raumname);
    }

    @FXML
    void botHinzufuegenButtonPressed(ActionEvent event) throws Exception {
        if (!srverwaltung.raumVoll(raumname)) {
            Random rand = new Random();
            String botname = "Bot" + rand.nextInt(100000);
            Bot bot = new Bot(botname, raumname, null, 0);
            srverwaltung.bot_hinzufuegen(botname, raumname);
            leaderboard.neues_punktekonto(botname);
            bot.start();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRaumname(String name) {
        this.raumname = name;
    }

}
