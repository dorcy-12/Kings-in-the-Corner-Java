package GUI.Controller;

import GUI.Klassen.Lobby;
import GUI.Klassen.Spielraum;
import Server.Spiel.Spielraumverwaltung;
import Server.Zugriffsverwaltung.Account;
import Server.Zugriffsverwaltung.Zugriffsverwaltung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SpielraumBeitretenController {

    private Registry registry;
    private Spielraumverwaltung srverwaltung;

    private Zugriffsverwaltung zgverwaltung;

    private String name = "";
    @FXML
    private Button abbrechenButton;

    @FXML
    private TextField errorFeld;

    @FXML
    private TextField nameDesSpielraums;

    @FXML
    private PasswordField passwort;

    @FXML
    private Button spielraumBeitretenButton;

    public SpielraumBeitretenController() throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry();
        this.srverwaltung = (Spielraumverwaltung) registry.lookup("Spielraumverwaltung");
        this.zgverwaltung = (Zugriffsverwaltung) registry.lookup("Zugriffsverwaltung");
    }

    /**
     * Wenn der abbrechen Button geklickt wird, öffnet sich das Lobbyfenster.
     * Und das SpielraumBeitreten Fenster schließt sich.
     * @param event
     * @throws Exception
     */
    @FXML
    void abbrechenButtonPushed(ActionEvent event) throws Exception {
        Stage stage = (Stage) abbrechenButton.getScene().getWindow();
        stage.close();
        Lobby lob = new Lobby();
        lob.setName(name);
        lob.start(new Stage());

    }

    /**
     * Wenn der Spielraum-Beitreten-Button geklickt wird, öffnet sich das Spielraumfenster und
     * das Spielraum-Fenster schließt sich
     * @param event
     * @throws Exception
     */
    @FXML
    void spielraumBeitretenButtonPushed(ActionEvent event) throws Exception {
        String raumname = nameDesSpielraums.getText();
        String pas = passwort.getText();

        try {
            if (!raumname.equals("")) {
                srverwaltung.spielraum_beitreten(name ,zgverwaltung.is_lobby(name), raumname, pas);
                zgverwaltung.lobby_entfernen(name);
                Stage stage = (Stage) spielraumBeitretenButton.getScene().getWindow();
                stage.close();
                Spielraum spr = new Spielraum();
                spr.setName(name);
                spr.setRaumname(raumname);
                spr.start(new Stage());
            }
            else {
                errorFeld.setVisible(true);
                errorFeld.setText("Feld Name nicht ausgefüllt.");
            }
        } catch (Exception e) {
            errorFeld.setVisible(true);
            errorFeld.setText(e.getMessage());
        }

    }

    @FXML
    void errorAnzeigen(ActionEvent event) {
        errorFeld.setVisible(true);
        errorFeld.setText("Error.");
    }

    public void setName(String name) {
        this.name = name;
    }
}
