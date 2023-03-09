package GUI.Controller;

import GUI.Klassen.Ingame;
import GUI.Klassen.Punktefenster;
import GUI.Klassen.Spielraum;
import GUI.Klassen.Spielregeln;
import Server.Spiel.Leaderboard;
import Server.Spiel.Spieler;
import Server.Spiel.Spielraumverwaltung;
import Server.Spiel.Spielverwaltung;
import Server.Zugriffsverwaltung.Zugriffsverwaltung;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class IngameController<eigeneKarte3, k> {

    private Registry registry;
    private Spielraumverwaltung srverwaltung;

    private Zugriffsverwaltung zgverwaltung;

    private Leaderboard leaderboard;

    private Spielverwaltung sverwaltung;

    private Integer ausgKarte = 0;

    private String ausgStapel = "";

    private String name = "";

    private String raumname = "";

    private Boolean amZug = false;

    private Boolean handkartenSet = false;
    private Integer handkartenAnzahl = 0;

    private Boolean stop = false;

    @FXML
    private TextField punkteFeld;

    @FXML
    private Button zugBeenden;

    @FXML
    private Label chippotLabel;
    @FXML
    private TextField amZugFeld;

    @FXML
    private ImageView KarteMitteRechts;

    @FXML
    private TextField anzahlChips;

    @FXML
    private Button aufsteigenSortierend;

    @FXML
    private TextArea chatNachrichtenFeld;

    @FXML
    private ImageView eigeneKarte1;

    @FXML
    private ImageView eigeneKarte2;

    @FXML
    private ImageView eigeneKarte3;

    @FXML
    private ImageView eigeneKarte4;

    @FXML
    private ImageView eigeneKarte5;

    @FXML
    private ImageView eigeneKarte6;

    @FXML
    private ImageView eigeneKarte7;

    @FXML
    private ImageView eigeneKarte8;

    @FXML
    private ImageView eigeneKarte9;

    @FXML
    private ImageView eigeneKarte10;

    @FXML
    private ImageView eigeneKarte11;

    @FXML
    private ImageView eigeneKarte12;

    @FXML
    private ImageView eigeneKarte13;

    @FXML
    private ImageView eigeneKarte14;

    @FXML
    private ImageView eigeneKarte15;

    @FXML
    private ImageView eigeneKarte16;

    @FXML
    private ImageView eigeneKarte17;

    @FXML
    private ImageView eigeneKarte18;

    @FXML
    private ImageView eigeneKarte19;

    @FXML
    private ImageView ziehstabel;

    @FXML
    private Button farblichSortieren;

    @FXML
    private TextField neueChatnachricht;

    @FXML
    private Button rangtabelle;

    @FXML
    private Button spielVerlassen;

    @FXML
    private AnchorPane spielfeld;

    @FXML
    private Button spielregeln;

    @FXML
    private ImageView karteLinksOben;

    @FXML
    private ImageView karteLinksUnten;

    @FXML
    private ImageView karteObenLinksOben;

    @FXML
    private ImageView karteObenLinksUnten;

    @FXML
    private ImageView karteObenOben;

    @FXML
    private ImageView karteObenRechtsOben;

    @FXML
    private ImageView karteObenRechtsUnten;

    @FXML
    private TextField errorFeld;

    @FXML
    private ImageView karteObenUnten;

    @FXML
    private ImageView karteRechtsOben;

    @FXML
    private ImageView karteRechtsUnten;

    @FXML
    private ImageView karteUntenLinksOben;

    @FXML
    private ImageView karteUntenLinksUnten;

    @FXML
    private ImageView karteUntenOben;

    @FXML
    private ImageView karteUntenRechtsOben;

    @FXML
    private ImageView karteUntenRechtsUnten;

    @FXML
    private ImageView karteUntenUnten;

    public IngameController() throws RemoteException, NotBoundException {
        this.registry = LocateRegistry.getRegistry();
        this.srverwaltung = (Spielraumverwaltung) registry.lookup("Spielraumverwaltung");
        this.zgverwaltung = (Zugriffsverwaltung) registry.lookup("Zugriffsverwaltung");
        this.leaderboard = (Leaderboard) registry.lookup("Leaderboard");
        this.sverwaltung = (Spielverwaltung) registry.lookup("Spielverwaltung");
    }


    /**
     * Wenn der aufsteigenSortierend-Button geklickt wird, werden die Karten aufsteigend sortiert.
     *
     * @param event
     */
    @FXML
    void aufsteigenSortierendButtonPressed(ActionEvent event) {
        try {
            sverwaltung.handkartenSortieren(raumname, name, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wenn der farblichSortieren-Button gedrückt wird, werden die Karten farblich sortiert.
     *
     * @param event
     */
    @FXML
    void farblichSortierenButtonPressed(ActionEvent event) {
        try {
            sverwaltung.handkartenSortieren(raumname, name, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wenn Enter gedrückt wird, wird die Nachricht gesendet.
     *
     * @param event Enter gedrückt
     */
    @FXML
    void neueChatnachrichtAbgeschickt(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) {
            try {
                errorFeld.setVisible(false);
                String nachricht = neueChatnachricht.getText();
                sverwaltung.neue_spiel_nachricht(nachricht, name, raumname);
                TimeUnit.MILLISECONDS.sleep(5);
                neueChatnachricht.clear();
            } catch (Exception e) {
                errorFeld.setVisible(true);
                errorFeld.setText(e.getMessage());
            }
        }
    }


    @FXML
    void rangtabelleButtonPressed(ActionEvent event) throws Exception {
        Punktefenster pf = new Punktefenster();
        pf.setRaumname(raumname);
        pf.start(new Stage());
    }

    /**
     * Wenn der Spielverlassen-Button Ingame betätigt wird, kommt man wieder zum Spielraum-Fenster.
     *
     * @param event
     * @throws Exception
     */
    @FXML
    void spielVerlassenButtonPressed(ActionEvent event) throws Exception {
        if (!srverwaltung.isHost(name, raumname)) {
            return;
        }
        sverwaltung.setSpielVorbei(raumname);
        //Stage stage = (Stage) spielVerlassen.getScene().getWindow();
        //stage.close();
        //Spielraum spr = new Spielraum();
        //spr.setName(name);
        //spr.setRaumname(raumname);
        //spr.start(new Stage());
        //stop = true;
    }

    /**
     * Wenn der Spielregeln-Button Ingame betätigt wird, kommt man zum Spielregeln-Fenster.
     *
     * @param event
     * @throws Exception
     */
    @FXML
    void spielregelnButtonPressed(ActionEvent event) throws Exception {
        Stage stage = (Stage) spielregeln.getScene().getWindow();
        new Spielregeln().start(new Stage());
    }

    ImageView karte = null;
    ImageView stapelKarte = null;

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte1ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte1.setOpacity(0.5);
            karte = eigeneKarte1;
        } else {
            if (karte != eigeneKarte1) {
                karte.setOpacity(1.0);
                eigeneKarte1.setOpacity(0.5);
                karte = eigeneKarte1;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 0;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte2ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte2.setOpacity(0.5);
            karte = eigeneKarte2;
        } else {
            if (karte != eigeneKarte2) {
                karte.setOpacity(1.0);
                eigeneKarte2.setOpacity(0.5);
                karte = eigeneKarte2;
                ausgKarte = 1;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 1;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte3ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte3.setOpacity(0.5);
            karte = eigeneKarte3;
        } else {
            if (karte != eigeneKarte3) {
                karte.setOpacity(1.0);
                eigeneKarte3.setOpacity(0.5);
                karte = eigeneKarte3;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 2;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte4ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte4.setOpacity(0.5);
            karte = eigeneKarte4;

        } else {
            if (karte != eigeneKarte4) {
                karte.setOpacity(1.0);
                eigeneKarte4.setOpacity(0.5);
                karte = eigeneKarte4;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 3;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte5ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte5.setOpacity(0.5);
            karte = eigeneKarte5;

        } else {
            if (karte != eigeneKarte5) {
                karte.setOpacity(1.0);
                eigeneKarte5.setOpacity(0.5);
                karte = eigeneKarte5;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 4;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte6ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte6.setOpacity(0.5);
            karte = eigeneKarte6;

        } else {
            if (karte != eigeneKarte6) {
                karte.setOpacity(1.0);
                eigeneKarte6.setOpacity(0.5);
                karte = eigeneKarte6;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 5;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte7ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte7.setOpacity(0.5);
            karte = eigeneKarte7;

        } else {
            if (karte != eigeneKarte7) {
                karte.setOpacity(1.0);
                eigeneKarte7.setOpacity(0.5);
                karte = eigeneKarte7;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 6;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte8ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte8.setOpacity(0.5);
            karte = eigeneKarte8;

        } else {
            if (karte != eigeneKarte8) {
                karte.setOpacity(1.0);
                eigeneKarte8.setOpacity(0.5);
                karte = eigeneKarte8;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 7;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte9ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte9.setOpacity(0.5);
            karte = eigeneKarte9;

        } else {
            if (karte != eigeneKarte9) {
                karte.setOpacity(1.0);
                eigeneKarte9.setOpacity(0.5);
                karte = eigeneKarte9;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 8;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte10ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte10.setOpacity(0.5);
            karte = eigeneKarte10;

        } else {
            if (karte != eigeneKarte10) {
                karte.setOpacity(1.0);
                eigeneKarte10.setOpacity(0.5);
                karte = eigeneKarte10;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 9;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte11ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte11.setOpacity(0.5);
            karte = eigeneKarte11;

        } else {
            if (karte != eigeneKarte11) {
                karte.setOpacity(1.0);
                eigeneKarte11.setOpacity(0.5);
                karte = eigeneKarte11;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 10;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte12ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte12.setOpacity(0.5);
            karte = eigeneKarte12;

        } else {
            if (karte != eigeneKarte12) {
                karte.setOpacity(1.0);
                eigeneKarte12.setOpacity(0.5);
                karte = eigeneKarte12;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 11;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte13ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte13.setOpacity(0.5);
            karte = eigeneKarte13;

        } else {
            if (karte != eigeneKarte13) {
                karte.setOpacity(1.0);
                eigeneKarte13.setOpacity(0.5);
                karte = eigeneKarte13;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 12;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte14ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte14.setOpacity(0.5);
            karte = eigeneKarte14;

        } else {
            if (karte != eigeneKarte14) {
                karte.setOpacity(1.0);
                eigeneKarte14.setOpacity(0.5);
                karte = eigeneKarte14;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 13;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte15ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte15.setOpacity(0.5);
            karte = eigeneKarte15;

        } else {
            if (karte != eigeneKarte15) {
                karte.setOpacity(1.0);
                eigeneKarte15.setOpacity(0.5);
                karte = eigeneKarte15;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 14;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte16ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte16.setOpacity(0.5);
            karte = eigeneKarte16;

        } else {
            if (karte != eigeneKarte16) {
                karte.setOpacity(1.0);
                eigeneKarte16.setOpacity(0.5);
                karte = eigeneKarte16;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 15;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte17ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte17.setOpacity(0.5);
            karte = eigeneKarte17;

        } else {
            if (karte != eigeneKarte17) {
                karte.setOpacity(1.0);
                eigeneKarte17.setOpacity(0.5);
                karte = eigeneKarte17;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 16;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte18ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte18.setOpacity(0.5);
            karte = eigeneKarte18;

        } else {
            if (karte != eigeneKarte18) {
                karte.setOpacity(1.0);
                eigeneKarte18.setOpacity(0.5);
                karte = eigeneKarte18;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 17;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void eigeneKarte19ausgewaehlt(MouseEvent event) {
        if (stapelKarte != null) {
            stapelKarte.setOpacity(1.0);
            stapelKarte = null;
        }
        if (karte == null) {
            eigeneKarte19.setOpacity(0.5);
            karte = eigeneKarte19;

        } else {
            if (karte != eigeneKarte19) {
                karte.setOpacity(1.0);
                eigeneKarte19.setOpacity(0.5);
                karte = eigeneKarte19;
            } else {
                karte.setOpacity(1.0);
                karte = null;
            }
        }
        ausgKarte = 18;
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void ziehstabelausgewaehlt(MouseEvent event) {
        if (!amZug) {
            return;
        }
        try {
            sverwaltung.karteZiehen(raumname, name);
            if (sverwaltung.getHandkarten(raumname, name).size() == handkartenAnzahl + 1) {
                sverwaltung.zahleChip(raumname, name, 1);
            }
            handkartenSet = false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    void karteLinksObenAusgewaehlt(MouseEvent event) {
        if (!amZug) {
            return;
        }
        if (karte != null) {
            try {
                String spielKarte = sverwaltung.getHandkarten(raumname, name).get(ausgKarte);
                sverwaltung.karteSpielen(raumname, name, spielKarte, "links");
                karte.setOpacity(1.0);
                karte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else if (stapelKarte != null) {
            try {
                sverwaltung.stapelVerschieben(raumname, ausgStapel, "links");
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void karteLinksUntenAusgewaehlt(MouseEvent event) {
        if (karte != null) {
            karte.setOpacity(1.0);
            karte = null;
        }
        if (stapelKarte == null) {
            karteLinksUnten.setOpacity(0.5);
            stapelKarte = karteLinksUnten;

        } else {
            if (stapelKarte != karteLinksUnten) {
                stapelKarte.setOpacity(1.0);
                karteLinksUnten.setOpacity(0.5);
                stapelKarte = karteLinksUnten;
            } else {
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            }
        }
        ausgStapel = "links";
    }

    @FXML
    void karteObenLinksAusgewaehlt(MouseEvent event) {
        if (karte != null) {
            karte.setOpacity(1.0);
            karte = null;
        }
        if (stapelKarte == null) {
            karteObenLinksUnten.setOpacity(0.5);
            stapelKarte = karteObenLinksUnten;

        } else {
            if (stapelKarte != karteObenLinksUnten) {
                stapelKarte.setOpacity(1.0);
                karteObenLinksUnten.setOpacity(0.5);
                stapelKarte = karteObenLinksUnten;
            } else {
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            }
        }
        ausgStapel = "obenLinks";
    }

    @FXML
    void karteObenLinksObenAusgewaehlt(MouseEvent event) {
        if (!amZug) {
            return;
        }
        if (karte != null) {
            try {
                String spielKarte = sverwaltung.getHandkarten(raumname, name).get(ausgKarte);
                sverwaltung.karteSpielen(raumname, name, spielKarte, "obenLinks");
                karte.setOpacity(1.0);
                karte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else if (stapelKarte != null) {
            try {
                sverwaltung.stapelVerschieben(raumname, ausgStapel, "obenLinks");
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void karteObenObenAusgewaehlt(MouseEvent event) {
        if (!amZug) {
            return;
        }
        if (karte != null) {
            try {
                String spielKarte = sverwaltung.getHandkarten(raumname, name).get(ausgKarte);
                sverwaltung.karteSpielen(raumname, name, spielKarte, "oben");
                karte.setOpacity(1.0);
                karte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else if (stapelKarte != null) {
            try {
                sverwaltung.stapelVerschieben(raumname, ausgStapel, "oben");
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void karteObenRechtsObenAusgewaehlt(MouseEvent event) {
        if (!amZug) {
            return;
        }
        if (karte != null) {
            try {
                String spielKarte = sverwaltung.getHandkarten(raumname, name).get(ausgKarte);
                sverwaltung.karteSpielen(raumname, name, spielKarte, "obenRechts");
                karte.setOpacity(1.0);
                karte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else if (stapelKarte != null) {
            try {
                sverwaltung.stapelVerschieben(raumname, ausgStapel, "obenRechts");
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void karteObenRechtsUntenAusgewaehlt(MouseEvent event) {
        if (karte != null) {
            karte.setOpacity(1.0);
            karte = null;
        }
        if (stapelKarte == null) {
            karteObenRechtsUnten.setOpacity(0.5);
            stapelKarte = karteObenRechtsUnten;

        } else {
            if (stapelKarte != karteObenRechtsUnten) {
                stapelKarte.setOpacity(1.0);
                karteObenRechtsUnten.setOpacity(0.5);
                stapelKarte = karteObenRechtsUnten;
            } else {
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            }
        }
        ausgStapel = "obenRechts";
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void karteObenUntenAusgewaehlt(MouseEvent event) {
        if (karte != null) {
            karte.setOpacity(1.0);
            karte = null;
        }
        if (stapelKarte == null) {
            karteObenUnten.setOpacity(0.5);
            stapelKarte = karteObenUnten;

        } else {
            if (stapelKarte != karteObenUnten) {
                stapelKarte.setOpacity(1.0);
                karteObenUnten.setOpacity(0.5);
                stapelKarte = karteObenUnten;
            } else {
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            }
        }
        ausgStapel = "oben";
    }

    @FXML
    void karteRechtsObenAusgewaehlt(MouseEvent event) {
        if (!amZug) {
            return;
        }
        if (karte != null) {
            try {
                String spielKarte = sverwaltung.getHandkarten(raumname, name).get(ausgKarte);
                sverwaltung.karteSpielen(raumname, name, spielKarte, "rechts");
                karte.setOpacity(1.0);
                karte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else if (stapelKarte != null) {
            try {
                sverwaltung.stapelVerschieben(raumname, ausgStapel, "rechts");
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void karteRechtsUntenAusgewaehlt(MouseEvent event) {
        if (karte != null) {
            karte.setOpacity(1.0);
            karte = null;
        }
        if (stapelKarte == null) {
            karteRechtsUnten.setOpacity(0.5);
            stapelKarte = karteRechtsUnten;

        } else {
            if (stapelKarte != karteRechtsUnten) {
                stapelKarte.setOpacity(1.0);
                karteRechtsUnten.setOpacity(0.5);
                stapelKarte = karteRechtsUnten;
            } else {
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            }
        }
        ausgStapel = "rechts";
    }

    @FXML
    void karteUntenLinksObenAusgewaehlt(MouseEvent event) {
        if (!amZug) {
            return;
        }
        if (karte != null) {
            try {
                String spielKarte = sverwaltung.getHandkarten(raumname, name).get(ausgKarte);
                sverwaltung.karteSpielen(raumname, name, spielKarte, "untenLinks");
                karte.setOpacity(1.0);
                karte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else if (stapelKarte != null) {
            try {
                sverwaltung.stapelVerschieben(raumname, ausgStapel, "untenLinks");
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void karteUntenLinksUntenAusgewaehlt(MouseEvent event) {
        if (karte != null) {
            karte.setOpacity(1.0);
            karte = null;
        }
        if (stapelKarte == null) {
            karteUntenLinksUnten.setOpacity(0.5);
            stapelKarte = karteUntenLinksUnten;

        } else {
            if (stapelKarte != karteUntenLinksUnten) {
                stapelKarte.setOpacity(1.0);
                karteUntenLinksUnten.setOpacity(0.5);
                stapelKarte = karteUntenLinksUnten;
            } else {
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            }
        }
        ausgStapel = "untenLinks";
    }

    @FXML
    void karteUntenObenAusgewaehlt(MouseEvent event) {
        if (!amZug) {
            return;
        }
        if (karte != null) {
            try {
                String spielKarte = sverwaltung.getHandkarten(raumname, name).get(ausgKarte);
                sverwaltung.karteSpielen(raumname, name, spielKarte, "unten");
                karte.setOpacity(1.0);
                karte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else if (stapelKarte != null) {
            try {
                sverwaltung.stapelVerschieben(raumname, ausgStapel, "unten");
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void karteUntenRechtsObenAusgewaehlt(MouseEvent event) {
        if (!amZug) {
            return;
        }
        if (karte != null) {
            try {
                String spielKarte = sverwaltung.getHandkarten(raumname, name).get(ausgKarte);
                sverwaltung.karteSpielen(raumname, name, spielKarte, "untenRechts");
                karte.setOpacity(1.0);
                karte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else if (stapelKarte != null) {
            try {
                sverwaltung.stapelVerschieben(raumname, ausgStapel, "untenRechts");
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void karteUntenRechtsUntenAusgewaehlt(MouseEvent event) {
        if (karte != null) {
            karte.setOpacity(1.0);
            karte = null;
        }
        if (stapelKarte == null) {
            karteUntenRechtsUnten.setOpacity(0.5);
            stapelKarte = karteUntenRechtsUnten;

        } else {
            if (stapelKarte != karteUntenRechtsUnten) {
                stapelKarte.setOpacity(1.0);
                karteUntenRechtsUnten.setOpacity(0.5);
                stapelKarte = karteUntenRechtsUnten;
            } else {
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            }
        }
        ausgStapel = "untenRechts";
    }

    /**
     * Wenn die Maus gedrückt ist, wird bei der ausgewählten Karte die Sichtbarkeit reduziert.
     *
     * @param event Maus gedrückt
     */
    @FXML
    void karteUntenUntenAusgewaehlt(MouseEvent event) {
        if (karte != null) {
            karte.setOpacity(1.0);
            karte = null;
        }
        if (stapelKarte == null) {
            karteUntenUnten.setOpacity(0.5);
            stapelKarte = karteUntenUnten;

        } else {
            if (stapelKarte != karteUntenUnten) {
                stapelKarte.setOpacity(1.0);
                karteUntenUnten.setOpacity(0.5);
                stapelKarte = karteUntenUnten;
            } else {
                stapelKarte.setOpacity(1.0);
                stapelKarte = null;
            }
        }
        ausgStapel = "unten";
    }

    /**
     * Der Chatverlauf updatet sich wiederholt umso den aktualisierten Chatverlauf
     * in der Textarea chatverlauf im Fenster der Lobby anzeigen lassen zu können.
     *
     * @throws Exception
     */
    public void chatUpdate() throws Exception {

        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200),
                event -> {
                    try {
                        chatNachrichtenFeld.setText(sverwaltung.getSpielChatverlauf(raumname));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (stop) {
                        timeLine.stop();
                    }
                });
        timeLine.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(200)));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Updatet in regelmäßigen Abständen die Karten die der Server hat mit den Handkarten des Spielers in Ingame
     *
     * @throws Exception
     */
    public void handkartenUpdate() throws Exception {

        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(20),
                event -> {
                    try {
                        ArrayList<String> handkarten = sverwaltung.getHandkarten(raumname, name);
                        double opacity1 = eigeneKarte1.getOpacity();
                        if (handkarten.size() > 0) {
                            eigeneKarte1.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(0) + ".png")));
                        } else {
                            eigeneKarte1.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte1.setOpacity(opacity1);
                        double opacity2 = eigeneKarte2.getOpacity();
                        if (handkarten.size() > 1) {
                            eigeneKarte2.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(1) + ".png")));
                        } else {
                            eigeneKarte2.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte2.setOpacity(opacity2);
                        double opacity3 = eigeneKarte3.getOpacity();
                        if (handkarten.size() > 2) {
                            eigeneKarte3.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(2) + ".png")));
                        } else {
                            eigeneKarte3.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte3.setOpacity(opacity3);
                        double opacity4 = eigeneKarte4.getOpacity();
                        if (handkarten.size() > 3) {
                            eigeneKarte4.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(3) + ".png")));
                        } else {
                            eigeneKarte4.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte4.setOpacity(opacity4);
                        double opacity5 = eigeneKarte5.getOpacity();
                        if (handkarten.size() > 4) {
                            eigeneKarte5.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(4) + ".png")));
                        } else {
                            eigeneKarte5.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte5.setOpacity(opacity5);
                        double opacity6 = eigeneKarte6.getOpacity();
                        if (handkarten.size() > 5) {
                            eigeneKarte6.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(5) + ".png")));
                        } else {
                            eigeneKarte6.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte6.setOpacity(opacity6);
                        double opacity7 = eigeneKarte7.getOpacity();
                        if (handkarten.size() > 6) {
                            eigeneKarte7.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(6) + ".png")));
                        } else {
                            eigeneKarte7.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte7.setOpacity(opacity7);
                        double opacity8 = eigeneKarte8.getOpacity();
                        if (handkarten.size() > 7) {
                            eigeneKarte8.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(7) + ".png")));
                        } else {
                            eigeneKarte8.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte8.setOpacity(opacity8);
                        double opacity9 = eigeneKarte9.getOpacity();
                        if (handkarten.size() > 8) {
                            eigeneKarte9.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(8) + ".png")));
                        } else {
                            eigeneKarte9.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte9.setOpacity(opacity9);
                        double opacity10 = eigeneKarte10.getOpacity();
                        if (handkarten.size() > 9) {
                            eigeneKarte10.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(9) + ".png")));
                        } else {
                            eigeneKarte10.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte10.setOpacity(opacity10);
                        double opacity11 = eigeneKarte11.getOpacity();
                        if (handkarten.size() > 10) {
                            eigeneKarte11.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(10) + ".png")));
                        } else {
                            eigeneKarte11.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte11.setOpacity(opacity11);
                        double opacity12 = eigeneKarte12.getOpacity();
                        if (handkarten.size() > 11) {
                            eigeneKarte12.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(11) + ".png")));
                        } else {
                            eigeneKarte12.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte12.setOpacity(opacity12);
                        double opacity13 = eigeneKarte13.getOpacity();
                        if (handkarten.size() > 12) {
                            eigeneKarte13.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(12) + ".png")));
                        } else {
                            eigeneKarte13.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte13.setOpacity(opacity13);
                        double opacity14 = eigeneKarte14.getOpacity();
                        if (handkarten.size() > 13) {
                            eigeneKarte14.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(13) + ".png")));
                        } else {
                            eigeneKarte14.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte14.setOpacity(opacity14);
                        double opacity15 = eigeneKarte15.getOpacity();
                        if (handkarten.size() > 14) {
                            eigeneKarte15.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(14) + ".png")));
                        } else {
                            eigeneKarte15.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte15.setOpacity(opacity15);
                        double opacity16 = eigeneKarte16.getOpacity();
                        if (handkarten.size() > 15) {
                            eigeneKarte16.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(15) + ".png")));
                        } else {
                            eigeneKarte16.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte16.setOpacity(opacity16);
                        double opacity17 = eigeneKarte17.getOpacity();
                        if (handkarten.size() > 16) {
                            eigeneKarte17.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(16) + ".png")));
                        } else {
                            eigeneKarte17.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte17.setOpacity(opacity17);
                        double opacity18 = eigeneKarte18.getOpacity();
                        if (handkarten.size() > 17) {
                            eigeneKarte18.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(17) + ".png")));
                        } else {
                            eigeneKarte18.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte18.setOpacity(opacity18);
                        double opacity19 = eigeneKarte19.getOpacity();
                        if (handkarten.size() > 18) {
                            eigeneKarte19.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + handkarten.get(18) + ".png")));
                        } else {
                            eigeneKarte19.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        eigeneKarte19.setOpacity(opacity19);
                        if (stop) {
                            timeLine.stop();
                        }
                    } catch (Exception e) {
                        System.out.println("ERROR WIRD GEWORFEN");
                        System.out.println(e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
        timeLine.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(200)));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Updatet in regelmäßigen Abständen die Stapel, die der Server hat mit den Stapeln des Spielers, die ihm in Ingame angezeigt werden.
     *
     * @throws Exception
     */
    public void stapelUpdate() throws Exception {
        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200),
                event -> {
                    try {
                        ArrayList<ArrayList<String>> stapel = sverwaltung.getStapel(raumname);
                        //Karte Oben Links
                        double opacity1 = karteObenLinksOben.getOpacity();
                        double opacity2 = karteObenLinksUnten.getOpacity();
                        if (stapel.get(0).size() == 2) {
                            if (stapel.get(0).get(0).equals(stapel.get(0).get(1))) {
                                karteObenLinksOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(0).get(0) + ".png")));
                                karteObenLinksUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/RueckseiteKarte.png")));
                            } else {
                                karteObenLinksOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(0).get(0) + ".png")));
                                karteObenLinksUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(0).get(1) + ".png")));
                            }
                        } else {
                            karteObenLinksOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                            karteObenLinksUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        karteObenLinksOben.setOpacity(opacity1);
                        karteObenLinksUnten.setOpacity(opacity2);
                        //Karte Oben
                        double opacity3 = karteObenOben.getOpacity();
                        double opacity4 = karteObenUnten.getOpacity();
                        if (stapel.get(1).size() == 2) {
                            if (stapel.get(1).get(0).equals(stapel.get(1).get(1))) {
                                karteObenOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(1).get(0) + ".png")));
                                karteObenUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/RueckseiteKarte.png")));
                            } else {
                                karteObenOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(1).get(0) + ".png")));
                                karteObenUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(1).get(1) + ".png")));
                            }
                        } else {
                            karteObenOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                            karteObenUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        karteObenOben.setOpacity(opacity3);
                        karteObenUnten.setOpacity(opacity4);
                        //Karte Oben Rechts
                        double opacity5 = karteObenRechtsOben.getOpacity();
                        double opacity6 = karteObenRechtsUnten.getOpacity();
                        if (stapel.get(2).size() == 2) {
                            if (stapel.get(2).get(0).equals(stapel.get(2).get(1))) {
                                karteObenRechtsOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(2).get(0) + ".png")));
                                karteObenRechtsUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/RueckseiteKarte.png")));
                            } else {
                                karteObenRechtsOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(2).get(0) + ".png")));
                                karteObenRechtsUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(2).get(1) + ".png")));
                            }
                        } else {
                            karteObenRechtsOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                            karteObenRechtsUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        karteObenRechtsOben.setOpacity(opacity5);
                        karteObenRechtsUnten.setOpacity(opacity6);
                        //Karte Links
                        double opacity7 = karteLinksOben.getOpacity();
                        double opacity8 = karteLinksUnten.getOpacity();
                        if (stapel.get(3).size() == 2) {
                            if (stapel.get(3).get(0).equals(stapel.get(3).get(1))) {
                                karteLinksOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(3).get(0) + ".png")));
                                karteLinksUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/RueckseiteKarte.png")));
                            } else {
                                karteLinksOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(3).get(0) + ".png")));
                                karteLinksUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(3).get(1) + ".png")));
                            }
                        } else {
                            karteLinksOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                            karteLinksUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        karteLinksOben.setOpacity(opacity7);
                        karteLinksUnten.setOpacity(opacity8);
                        //Karte Rechts
                        double opacity9 = karteRechtsOben.getOpacity();
                        double opacity10 = karteRechtsUnten.getOpacity();
                        if (stapel.get(4).size() == 2) {
                            if (stapel.get(4).get(0).equals(stapel.get(4).get(1))) {
                                karteRechtsOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(4).get(0) + ".png")));
                                karteRechtsUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/RueckseiteKarte.png")));
                            } else {
                                karteRechtsOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(4).get(0) + ".png")));
                                karteRechtsUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(4).get(1) + ".png")));
                            }
                        } else {
                            karteRechtsOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                            karteRechtsUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        karteRechtsOben.setOpacity(opacity9);
                        karteRechtsUnten.setOpacity(opacity10);
                        //Karte Unten Links
                        double opacity11 = karteUntenLinksOben.getOpacity();
                        double opacity12 = karteUntenLinksUnten.getOpacity();
                        if (stapel.get(5).size() == 2) {
                            if (stapel.get(5).get(0).equals(stapel.get(5).get(1))) {
                                karteUntenLinksOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(5).get(0) + ".png")));
                                karteUntenLinksUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/RueckseiteKarte.png")));
                            } else {
                                karteUntenLinksOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(5).get(0) + ".png")));
                                karteUntenLinksUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(5).get(1) + ".png")));
                            }
                        } else {
                            karteUntenLinksOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                            karteUntenLinksUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        karteUntenLinksOben.setOpacity(opacity11);
                        karteUntenLinksUnten.setOpacity(opacity12);
                        //Karte Unten
                        double opacity13 = karteUntenOben.getOpacity();
                        double opacity14 = karteUntenUnten.getOpacity();
                        if (stapel.get(6).size() == 2) {
                            if (stapel.get(6).get(0).equals(stapel.get(6).get(1))) {
                                karteUntenOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(6).get(0) + ".png")));
                                karteUntenUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/RueckseiteKarte.png")));
                            } else {
                                karteUntenOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(6).get(0) + ".png")));
                                karteUntenUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(6).get(1) + ".png")));
                            }
                        } else {
                            karteUntenOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                            karteUntenUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        karteUntenOben.setOpacity(opacity13);
                        karteUntenUnten.setOpacity(opacity14);
                        //Karte Unten Rechts
                        double opacity15 = karteUntenRechtsOben.getOpacity();
                        double opacity16 = karteUntenRechtsUnten.getOpacity();
                        if (stapel.get(7).size() == 2) {
                            if (stapel.get(7).get(0).equals(stapel.get(7).get(1))) {
                                karteUntenRechtsOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(7).get(0) + ".png")));
                                karteUntenRechtsUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/RueckseiteKarte.png")));
                            } else {
                                karteUntenRechtsOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(7).get(0) + ".png")));
                                karteUntenRechtsUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/" + stapel.get(7).get(1) + ".png")));
                            }
                        } else {
                            karteUntenRechtsOben.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                            karteUntenRechtsUnten.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        }
                        karteUntenRechtsOben.setOpacity(opacity15);
                        karteUntenRechtsUnten.setOpacity(opacity16);
                        if (sverwaltung.nachziehstapelLeer(raumname)) {
                            ziehstabel.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/leereKarte.png")));
                        } else {
                            ziehstabel.setImage(new Image(getClass().getResourceAsStream("/GUI/Klassen/FXMLVorlagen/Images/RueckseiteKarte.png")));
                        }
                        if (stop) {
                            timeLine.stop();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
        timeLine.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(200)));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Fragt den Server in regelmäßigen Abständen, ob man am Zug ist
     *
     * @throws Exception
     */
    public void amZugUpdate() throws Exception {

        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200),
                event -> {
                    try {
                        amZug = sverwaltung.amZug(raumname, name);
                        if (amZug) {
                            if (!amZugFeld.isVisible()) {
                                amZugFeld.setVisible(true);
                            }
                            if (!handkartenSet) {
                                handkartenAnzahl = sverwaltung.getHandkarten(raumname, name).size();
                                handkartenSet = true;
                            }
                            if (stop) {
                                timeLine.stop();
                            }
                        } else {
                            if (amZugFeld.isVisible()) {
                                amZugFeld.setVisible(false);
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        timeLine.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(200)));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Fragt in regelmäßigen Abständen den Server, ob das Spiel vorbei ist.
     *
     * @throws Exception
     */
    public void spielVorbeiUpdate() throws Exception {

        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200),
                event -> {
                    try {
                        if (sverwaltung.spielVorbei(raumname)) {
                            if (srverwaltung.isHost(name, raumname)) {
                                ArrayList<Spieler> spieler = sverwaltung.getSpielSpieler(raumname);
                                leaderboard.bestenliste_anpassen(spieler);
                            }
                            Stage stage = (Stage) spielVerlassen.getScene().getWindow();
                            stage.close();
                            Spielraum spr = new Spielraum();
                            spr.setName(name);
                            spr.setRaumname(raumname);
                            spr.start(new Stage());
                            stop = true;
                            timeLine.stop();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
        timeLine.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(200)));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Fragt den Server in regelmäßigen Abständen nach seiner aktuellen Chipanzahl.
     *
     * @throws Exception
     */
    public void chipsUpdate() throws Exception {

        Timeline timeLine = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200),
                event -> {
                    try {
                        Integer chips = sverwaltung.getChips(raumname, name);
                        anzahlChips.setText("Anzahl Chips: " + chips.toString());
                        Integer topfChips = sverwaltung.getChipsImTopf(raumname);
                        chippotLabel.setText(topfChips.toString());
                        Integer punkte = sverwaltung.getPunkte(raumname, name);
                        punkteFeld.setText("Punkte: " + punkte.toString());
                        if (stop) {
                            timeLine.stop();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
        timeLine.getKeyFrames().addAll(kf, new KeyFrame(Duration.millis(200)));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRaumname(String name) {
        this.raumname = name;
    }

    public void errorAnzeigen(ActionEvent actionEvent) {
    }

}

