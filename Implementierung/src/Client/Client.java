package Client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Server.Zugriffsverwaltung.*;
import Server.Spiel.*;
import GUI.Klassen.*;

public class Client {
    
    public static void main(String[] args) throws Exception {

        Login login = new Login();
        login.main(args);

    }

}

