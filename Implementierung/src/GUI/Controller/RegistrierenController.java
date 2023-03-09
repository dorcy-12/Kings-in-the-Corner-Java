package GUI.Controller;

import GUI.Klassen.AGBs;
import GUI.Klassen.Lobby;
import GUI.Klassen.Login;
import Server.Spiel.Leaderboard;
import Server.Zugriffsverwaltung.Zugriffsverwaltung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegistrierenController {

    private Registry registry;
    private Zugriffsverwaltung zgverwaltung;

    private Leaderboard leaderboard;

    @FXML
    private Button abbrechen;

    @FXML
    private Button agbsButton;

    @FXML
    private TextField errorFeld;

    @FXML
    private CheckBox agbsCheckBox;

    @FXML
    private TextField benutzername;

    @FXML
    private PasswordField passwort;

    @FXML
    private PasswordField passwortWiederholen;

    @FXML
    private Button registrieren;

    public RegistrierenController() throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry();
        this.zgverwaltung = (Zugriffsverwaltung) registry.lookup("Zugriffsverwaltung");
        this.leaderboard = (Leaderboard) registry.lookup("Leaderboard");
    }

    @FXML
    void BenutzernameEingabe(ActionEvent event) {

    }

    /**
     * Bei Betätigung des abbrechen Buttons öffnet sich das Login-Fenster.
     * Das Registrieren-Fenster wird geschlossen.
     * @param event
     * @throws Exception
     */
    @FXML
    void abbrechenButtonPressed(ActionEvent event) throws Exception {
        Stage stage = (Stage) abbrechen.getScene().getWindow();
        stage.close();
        new Login().start(new Stage());

    }

    @FXML
    void errorAnzeigen(ActionEvent event) {
        errorFeld.setVisible(true);
        errorFeld.setText("Error.");
    }

    /**
     * Bei Betätigung des AGB-Buttons öffnet sich das Fenster mit den AGB's
     * Das Registrieren-Fenster wird geschlossen.
     * @param event
     * @throws Exception
     */
    @FXML
    void agbsButtonPressed(ActionEvent event) throws Exception {
        Stage stage = (Stage) agbsButton.getScene().getWindow();
        stage.close();
        new AGBs().start(new Stage());
    }


    @FXML
    void agbsCheckBoxPressed(ActionEvent event) {

    }

    @FXML
    void passwordWiederholenEingabe(ActionEvent event) {

    }

    @FXML
    void passwortEingabe(ActionEvent event) {

    }

    /**
     * Wenn der Registrieren-Button getätigt wird ein Punktekonto angelegt und ein Account und
     * es öffnet sich das Lobby-Fenster.
     * Das Registrieren Fenster wird geschlossen.
     * Es wird geprüft, ob die Checkbox für die AGB's angeklickt wurde
     * und ob alle Felder ausgefüllt wurden.
     * Wurde eines dieser Sachen vom Nutzer nicht gemacht werden Exceptions geworfen.
     * @throws "AGBs nicht  akzeptiert."
     * @throws "Nicht alle Felder ausgefüllt."
     */
    @FXML
    void registrierenButtonPressed(ActionEvent event) throws Exception {

        String ben = benutzername.getText();
        String pas = passwort.getText();
        String pasw = passwortWiederholen.getText();

        try {
            if (!ben.equals("") && !pas.equals("") && !pasw.equals("")) {
                if (agbsCheckBox.isSelected()) {
                    zgverwaltung.spieler_registrieren(ben, pas, pasw);
                    leaderboard.neues_punktekonto(ben);
                    zgverwaltung.spieler_anmelden(ben, pas);
                    System.out.println("Spieler angemeldet.");
                    Stage stage = (Stage) registrieren.getScene().getWindow();
                    stage.close();
                    Lobby lob = new Lobby();
                    lob.setName(ben);
                    lob.start(new Stage());
                }
                else {
                    errorFeld.setVisible(true);
                    errorFeld.setText("AGBs nicht  akzeptiert.");
                }
            }
            else {
                errorFeld.setVisible(true);
                errorFeld.setText("Nicht alle Felder ausgefüllt.");
            }
        }
        catch (Exception e) {
            errorFeld.setVisible(true);
            errorFeld.setText(e.getMessage());
        }

    }

}


