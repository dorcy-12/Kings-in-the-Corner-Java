package GUI.Klassen;

import Client.Client;
import Server.Zugriffsverwaltung.Zugriffsverwaltung;
import GUI.Controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Login extends Application  {

    /**
     * Starten des Login Fensters(GUI).
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVorlagen/Login.fxml"));

        Parent root = (Parent)fxmlLoader.load();
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        LoginController controller = fxmlLoader.<LoginController>getController();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}