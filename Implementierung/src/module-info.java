module GUI {
      requires javafx.controls;
      requires javafx.fxml;
      requires javafx.base;
      requires java.rmi;


    opens GUI.Klassen to javafx.fxml;
    opens GUI.Controller to javafx.fxml;
    exports GUI.Klassen;
    exports Server.Zugriffsverwaltung;
    exports Server.Spiel;
    exports Server.Chat;
}
