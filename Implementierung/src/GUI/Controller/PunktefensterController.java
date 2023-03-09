package GUI.Controller;

import Server.Spiel.Leaderboard;
import Server.Spiel.Spielraumverwaltung;
import Server.Spiel.Spielverwaltung;
import Server.Zugriffsverwaltung.Zugriffsverwaltung;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PunktefensterController {

    private Registry registry;

    private Spielverwaltung sverwaltung;

    private String raumname = "";

    @FXML
    private Label namePunkte;

    @FXML
    private TextArea punkteFeld;

    private Boolean stop = false;

    @FXML
    private AnchorPane punkteFenster;

    @FXML
    private Button schliessenButton;

    public PunktefensterController() throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry();
        this.sverwaltung = (Spielverwaltung) registry.lookup("Spielverwaltung");
    }

    @FXML
    void schliessenButtonPressed(ActionEvent event) {
        Stage stage = (Stage) schliessenButton.getScene().getWindow();
        stop = true;
        stage.close();
    }

    public void tabelleUpdate() throws Exception {

        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200),
                event -> {
                    try {
                        String punkteString = sverwaltung.getPunkteAlle(raumname);
                        punkteFeld.setText(punkteString);
                        if (stop) {
                            timeLine.stop();
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

    public void setRaumname(String name) {
        this.raumname = name;
    }

}
