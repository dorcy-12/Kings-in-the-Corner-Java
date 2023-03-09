package GUI.Controller;

import GUI.Klassen.Lobby;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class SpielergebnisController {

    @FXML
    private TableView<?> tabelle;

    @FXML
    private Label werhatGewonnen;

    @FXML
    private Button zurLobbyButton;

    /**
     * Wenn der zur Lobby-Button betätigt wird, öffnet sich das Lobby-Fenster.
     * Und das Spielergebnis-Fenster schließt.
     * @param event
     * @throws Exception
     */
    @FXML
    void zurLobbyButtonPressed(ActionEvent event) throws Exception {
        Stage stage = (Stage) zurLobbyButton.getScene().getWindow();
        stage.close();
        new Lobby().start(new Stage());
    }

}
