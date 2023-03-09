package GUI.Klassen;

import GUI.Controller.ProfilBearbeitenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class ProfilBearbeiten extends Application  {

    private String name = "";

    /**
     * Starten des Profil Bearbeiten Fensters(GUI).
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVorlagen/ProfilBearbeiten.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        primaryStage.setTitle("Profil Bearbeiten");
        primaryStage.setScene(new Scene(root, 600, 400));
        ProfilBearbeitenController controller = fxmlLoader.<ProfilBearbeitenController>getController();
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
