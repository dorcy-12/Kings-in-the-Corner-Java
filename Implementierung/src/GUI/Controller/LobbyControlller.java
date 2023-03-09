package GUI.Controller;

import GUI.Klassen.*;
import Server.Chat.Chatverwaltung;
import Server.Spiel.Leaderboard;
import Server.Zugriffsverwaltung.Zugriffsverwaltung;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LobbyControlller {
    private Registry registry;
    private Chatverwaltung chverwaltung;

    private Zugriffsverwaltung zgverwaltung;

    private Leaderboard leaderboard;

    private String name = "";

    private Boolean stop = false;

    @FXML
    private TextField errorFeld;

    @FXML
    private Button abmelden;

    @FXML
    private TextArea bestenliste;

    @FXML
    private TextArea chatverlauf;

    @FXML
    private TextField neueChatnachricht;

    @FXML
    private Button profilBearbeiten;

    @FXML
    private Button spielraumBeitreten;

    @FXML
    private Button spielraumErstellen;

    public LobbyControlller() throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry();
        this.chverwaltung = (Chatverwaltung) registry.lookup("Chatverwaltung");
        this.zgverwaltung = (Zugriffsverwaltung) registry.lookup("Zugriffsverwaltung");
        this.leaderboard = (Leaderboard) registry.lookup("Leaderboard");
    }

    /**
     * Spieler kann abmelden-Button drücken und gelangt zum Login-Fenster.
     * @param event
     * @throws Exception
     */
    @FXML
    void abmeldenButtonPressed(ActionEvent event) throws Exception {
        zgverwaltung.spieler_abmelden(name);
        Stage stage = (Stage) abmelden.getScene().getWindow();
        stage.close();
        new Login().start(new Stage());
        stop = true;
    }

    /**
     * Wenn Enter gedrückt wird und man im Textfeld etwas geschrieben hat und die Entertaste betätigt wird.
     * das geschriebene in den chatverlauf mit aufgenommen.
     * @param event
     * @throws Exception
     */
    @FXML
    void neueChatnachrichtAbgesendet(KeyEvent event) throws Exception {
        if (event.getCode().equals(KeyCode.ENTER)) {
            try {
                errorFeld.setVisible(false);
                String nachricht = neueChatnachricht.getText();
                chverwaltung.neue_nachricht(nachricht, name);
                TimeUnit.MILLISECONDS.sleep(5);
                neueChatnachricht.clear();
            } catch (Exception e) {
                errorFeld.setVisible(true);
                errorFeld.setText(e.getMessage());
            }
        }
    }

    /**
     * Wenn der Profilberarbeiten-Button betätigt wird, öffnet das Profilbearbeiten-Fenster.
     * @param event
     * @throws Exception
     */
    @FXML
    void profilBearbeitenButtonPressed(ActionEvent event) throws Exception {
        Stage stage = (Stage) profilBearbeiten.getScene().getWindow();
        stage.close();
        ProfilBearbeiten profb = new ProfilBearbeiten();
        profb.setName(name);
        profb.start(new Stage());
        stop = true;
    }

    /**
     * Wenn der Spielraumbeitreten-Button betätigt wird, öffnet sich das Spielraumbeitreten-Fenster.
     * @param event
     * @throws Exception
     */
    @FXML
    void spielraumBeitretenButtonPressed(ActionEvent event) throws Exception {
        Stage stage = (Stage) spielraumBeitreten.getScene().getWindow();
        stage.close();
        SpielraumBeitreten srb = new SpielraumBeitreten();
        srb.setName(name);
        srb.start(new Stage());
        stop = true;
    }

    /**
     * Wenn man den Spielraumerstellen-Button betätigt öffnet sich ein Spielraum-Fenster.
     * @param event
     * @throws Exception
     */
    @FXML
    void spielraumErstellenButtonPressed(ActionEvent event) throws Exception {
        Stage stage = (Stage) spielraumErstellen.getScene().getWindow();
        stage.close();
        SpielraumErstellen sre = new SpielraumErstellen();
        sre.setName(name);
        sre.start(new Stage());
        stop = true;
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
                       chatverlauf.setText(chverwaltung.getChatverlauf());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        timeLine.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(200)));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Die Bestenliste updatet sich immer umso die aktualisierten top 10 Spieler
     * in der Textarea bestenliste anzeigen lassen zu können.
     * @throws Exception
     */
    public void bestenlisteUpdate() throws Exception {

        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200),
                event -> {
                    try {
                        bestenliste.setText(leaderboard.top_10_ausgeben());
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

    public void setName(String name) {
        this.name = name;
    }

    @FXML
    void errorAnzeigen(ActionEvent event) {
        errorFeld.setVisible(true);
        errorFeld.setText("Error.");
    }
}
