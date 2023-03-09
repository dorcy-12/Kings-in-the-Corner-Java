package GUI.Controller;

import GUI.Klassen.Lobby;
import GUI.Klassen.Registrieren;
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

public class LoginController {
    public TextField benutzername;
    public PasswordField passwort;

    private Registry registry;
    private Zugriffsverwaltung zgverwaltung;

    @FXML
    private TextField errorFeld;

    @FXML
    private Button login;
    @FXML
    private Button anmelden;
    @FXML
    private Button registrieren;

    public LoginController() throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry();
        this.zgverwaltung = (Zugriffsverwaltung) registry.lookup("Zugriffsverwaltung");
    }

    /**
     * Wenn der Anmelden-Button betätigt wird, öffnet sich (mit richtigem Benutzernamen und Passwort) die Lobby
     * und der Spieler wird angemeldet.
     * @throws Exception
     */
    @FXML
    protected void anmeldenButtonPressed() throws Exception {

        String ben = benutzername.getText();
        String pas = passwort.getText();

        try {
            zgverwaltung.spieler_anmelden(ben, pas);
            System.out.println("Spieler angemeldet.");
            Stage stage = (Stage) registrieren.getScene().getWindow();
            stage.close();
            Lobby lob = new Lobby();
            lob.setName(ben);
            lob.start(new Stage());
        }
        catch (Exception e) {
            errorFeld.setVisible(true);
            errorFeld.setText(e.getMessage());
        }


    }

    @FXML
    void errorAnzeigen(ActionEvent event) {
        errorFeld.setVisible(true);
        errorFeld.setText("Error.");
    }

    /**
     * Wenn der Spielerregistrieren-Button gedrückt wird, öffnet sich das Fenster wo sich der
     * Spieler registrieren kann.
     * @throws Exception
     */
    @FXML
    protected void registrierenButtonPressed() throws Exception {
        Stage stage = (Stage) registrieren.getScene().getWindow();
        stage.close();
        new Registrieren().start(new Stage());
    }

}