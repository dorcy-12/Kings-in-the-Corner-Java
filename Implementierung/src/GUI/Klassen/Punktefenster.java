package GUI.Klassen;

import GUI.Controller.IngameController;
import GUI.Controller.PunktefensterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Punktefenster extends Application  {

    private String raumname = "";

    /**
     * Starten des AGB Fensters (GUI).
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVorlagen/Punktefenster.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Punktefenster");
        primaryStage.setScene(new Scene(root, 300, 300));
        PunktefensterController controller = fxmlLoader.getController();
        controller.setRaumname(raumname);
        primaryStage.show();
        controller.tabelleUpdate();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setRaumname (String raumname) {
        this.raumname = raumname;
    }

}
