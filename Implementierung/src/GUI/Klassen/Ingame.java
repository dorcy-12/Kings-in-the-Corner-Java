package GUI.Klassen;

import GUI.Controller.IngameController;
import GUI.Controller.SpielraumController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;


public class Ingame extends Application  {

    private String name = "";
    private String raumname = "";


    /**
     * Starten des Ingame Fensters (GUI).
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVorlagen/Ingame.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        primaryStage.setTitle("Ingame");
        primaryStage.setScene(new Scene(root, 1200, 800));
        IngameController controller = fxmlLoader.<IngameController>getController();
        controller.setName(name);
        controller.setRaumname(raumname);
        primaryStage.show();
        controller.chatUpdate();
        controller.handkartenUpdate();
        controller.stapelUpdate();
        controller.amZugUpdate();
        controller.spielVorbeiUpdate();
        controller.chipsUpdate();
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
