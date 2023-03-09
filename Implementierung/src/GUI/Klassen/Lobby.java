package GUI.Klassen;

import GUI.Controller.LobbyControlller;
import GUI.Controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Lobby extends Application  {
    private String name = "";

    /**
     * Starten des Lobby Fensters(GUI).
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVorlagen/Lobby.fxml"));

        Parent root = (Parent)fxmlLoader.load();
        primaryStage.setTitle("Lobby");
        primaryStage.setScene(new Scene(root, 600, 400));
        LobbyControlller controller = fxmlLoader.<LobbyControlller>getController();
        controller.setName(name);
        primaryStage.show();
        controller.chatUpdate();
        controller.bestenlisteUpdate();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setName(String name) {
        this.name = name;
    }
}
