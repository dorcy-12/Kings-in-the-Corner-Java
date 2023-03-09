package GUI.Klassen;

import GUI.Controller.ProfilBearbeitenController;
import GUI.Controller.SpielraumErstellenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SpielraumErstellen extends Application  {
    private String name = "";

    /**
     * Öffnet das Spielraum Erstellen Fenster(GUI).
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVorlagen/SpielraumErstellen.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        primaryStage.setTitle("Spielraum Erstellen");
        primaryStage.setScene(new Scene(root, 600, 400));
        SpielraumErstellenController controller = fxmlLoader.<SpielraumErstellenController>getController();
        controller.setName(name);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setName(String name) {
        this.name = name;
    }

}
