package GUI.Klassen;

import GUI.Controller.SpielraumController;
import GUI.Controller.SpielraumErstellenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Spielraum extends Application  {

    private String name = "";
    private String raumname = "";

    /**
     * Öffnet das Spielraumfenster(GUI) mit dem Chat und der Bestenliste.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVorlagen/Spielraum.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        primaryStage.setTitle("Spielraum");
        primaryStage.setScene(new Scene(root, 600, 400));
        SpielraumController controller = fxmlLoader.<SpielraumController>getController();
        controller.setName(name);
        controller.setRaumname(raumname);
        primaryStage.show();
        controller.chatUpdate();
        controller.bestenlisteUpdate();
        controller.gestartetUpdate();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRaumname(String name) {
        this.raumname = name;
    }

}
