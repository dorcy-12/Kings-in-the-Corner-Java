package Server.Zugriffsverwaltung;

/**
 * Die Klasse Account hat einen Bentuzernamen und ein Passowrt eines Nutzers als Attribut
 * und die entsprechenden Getter, Setter und Konstruktor Methoden
 */
public class Account {
    private String benutzername;
    private String passwort;

    public Account(String benutzername, String passwort) {
        this.benutzername = benutzername;
        this.passwort = passwort;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getBenutzername() {
        return this.benutzername;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getPasswort() {
        return this.passwort;
    }
    
}
