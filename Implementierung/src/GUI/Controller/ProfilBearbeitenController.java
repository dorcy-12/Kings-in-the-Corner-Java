package GUI.Controller;

import GUI.Klassen.Lobby;
import GUI.Klassen.Login;
import Server.Spiel.Leaderboard;
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

public class ProfilBearbeitenController {

    private Registry registry;
    private Zugriffsverwaltung zgverwaltung;

    private Leaderboard leaderboard;

    private String name = "";

    @FXML
    private Button abbrechen;

    @FXML
    private TextField benutzername;

    @FXML
    private TextField errorFeld;

    @FXML
    private PasswordField passwort;

    @FXML
    private PasswordField passwortWiederholen;

    @FXML
    private Button profilLoeschen;

    @FXML
    private Button uebernehmen;

    public ProfilBearbeitenController() throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry();
        this.zgverwaltung = (Zugriffsverwaltung) registry.lookup("Zugriffsverwaltung");
        this.leaderboard = (Leaderboard) registry.lookup("Leaderboard");
    }

    @FXML
    void BenutzernameEingabe(ActionEvent event) {

    }

    @FXML
    void errorANzeigen(ActionEvent event) {

    }


    /**
     * wenn der Abbrechen-Button betätigt wird, kommt der Spieler zurück zum Lobby-Fenster.
     * @param event
     * @throws Exception
     */
    @FXML
    void abbrechenButtonPressed(ActionEvent event) throws Exception {
        Stage stage = (Stage) abbrechen.getScene().getWindow();
        stage.close();
        Lobby lob = new Lobby();
        lob.setName(name);
        lob.start(new Stage());
    }

    @FXML
    void passwordWiederholenEingabe(ActionEvent event) {

    }

    @FXML
    void passwortEingabe(ActionEvent event) {

    }

    /**
     * Wenn der Profillöschen-Button betätigt wird, öffnet sich wieder der Login-Screen und
     * dei Benutzerdaten des Spielers werden gelöscht.
     * @param event
     * @throws Exception
     */
    @FXML
    void profilLoeschenButtonPressed(ActionEvent event) throws Exception {
        String npas = passwort.getText();
        String npasw = passwortWiederholen.getText();

        try {
            if (npas.equals(npasw)) {
                zgverwaltung.spieler_loeschen(name, npas);
                leaderboard.punktekonto_loeschen(name);
                Stage stage = (Stage) profilLoeschen.getScene().getWindow();
                stage.close();
                new Login().start(new Stage());
            }
            else {
                errorFeld.setVisible(true);
                errorFeld.setText("Passwörter stimmen nicht überein.");
            }
        } catch (Exception e) {
            errorFeld.setVisible(true);
            errorFeld.setText(e.getMessage());
        }
    }

    /**
     * Wenn der Übernehmen-Button betätigt wird, werden die Änderungen der Benutzerdaten übernommen
     * und das Lobby-Fenster öffnet sich.
     */
    @FXML
    void uebernehmenButtonPressed(ActionEvent event) throws Exception {
        String nben = benutzername.getText();
        String npas = passwort.getText();
        String npasw = passwortWiederholen.getText();

        try {
            zgverwaltung.spieler_bearbeiten(name, nben, npas, npasw);
            leaderboard.rename_punktekonto(name, nben);
            Stage stage = (Stage) uebernehmen.getScene().getWindow();
            stage.close();
            Lobby lob = new Lobby();
            lob.setName(nben);
            lob.start(new Stage());
        } catch (Exception e) {
            errorFeld.setVisible(true);
            errorFeld.setText(e.getMessage());
        }
    }


    public void setName(String name) {
        this.name = name;
    }

}
