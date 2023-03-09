package Server.Chat;

/**
 * Die Klasse ChatNachricht enthält als Attribute
 */
public class Chatnachricht {
    private String sender;
    private String nachricht;
    private String empfaenger;

    public Chatnachricht (String sender, String nachricht, String empfaenger) {
        this.sender = sender;
        this.nachricht = nachricht;
        this.empfaenger = empfaenger;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return this.sender;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public String getnachricht() {
        return this.nachricht;
    }

    public void setEmpfaenger(String empfaenger) {
        this.empfaenger = empfaenger;
    }

    public String getEmpfaenger() {
        return this.empfaenger;
    }
}
