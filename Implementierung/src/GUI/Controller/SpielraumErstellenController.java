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

public class SpielraumErstellenController {
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
    private PasswordField passwortWiederholen;

    @FXML
    private Button spielraumErstellenButton;

    public SpielraumErstellenController() throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry();
        this.srverwaltung = (Spielraumverwaltung) registry.lookup("Spielraumverwaltung");
        this.zgverwaltung = (Zugriffsverwaltung) registry.lookup("Zugriffsverwaltung");
    }

    /**
     * Wenn der Abbrechen-Button betätigt wird, schießt sich der SpielraumErstellem-Screen und der Lobby-Screen öffnet sich
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
     * Wenn der SpielraumBetreten-Button betätigt wird, schließt sich das SpielraumErstellen-Fenster
     * und ein Spielraumfenster öffnet sich.
     * Desweiteren wird ein Spielraum erstellt.
     * @param event
     * @throws Exception
     */
    @FXML
    void spielraumBeitretenButtonPushed(ActionEvent event) throws Exception {
        String raumname = nameDesSpielraums.getText();
        String pas = passwort.getText();
        String pasw = passwortWiederholen.getText();

        try {
            if (!raumname.equals("")) {
                srverwaltung.spielraum_erstellen(name, raumname, pas, pasw);
                zgverwaltung.lobby_entfernen(name);
                Stage stage = (Stage) spielraumErstellenButton.getScene().getWindow();
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
