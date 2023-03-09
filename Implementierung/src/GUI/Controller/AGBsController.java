package GUI.Controller;

import GUI.Klassen.Registrieren;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AGBsController {

    @FXML
    private TextArea textFeld;

    @FXML
    private TextArea agbFeld;

    @FXML
    private Button zurueck;

    /**
     * Wenn der zurück-Button im Fenster der AGB's gedrückt wird, öffnet sich wieder das Registrieren-Fenster.
     * @param event
     * @throws Exception
     */
    @FXML
    void zurueckButtonPressed(ActionEvent event) throws Exception {
        Stage stage = (Stage) zurueck.getScene().getWindow();
        stage.close();
        new Registrieren().start(new Stage());
    }

}
