package GUI.Klassen;

import GUI.Controller.SpielraumBeitretenController;
import GUI.Controller.SpielraumErstellenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class SpielraumBeitreten extends Application  {

    private String name = "";

    /**
     * Öffnet das SpielraumBeitreten Fenster(GUI).
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVorlagen/SpielraumBeitreten.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        primaryStage.setTitle("Spielraum Beitreten");
        primaryStage.setScene(new Scene(root, 600, 400));
        SpielraumBeitretenController controller = fxmlLoader.<SpielraumBeitretenController>getController();
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
