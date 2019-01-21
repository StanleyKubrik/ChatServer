import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private DataOutputStream out;
    private DataInputStream in;
    private Socket cs;
    private String login;

    public Client(Socket cs) throws IOException {
        this.cs = cs;
        out = new DataOutputStream(cs.getOutputStream());
        in = new DataInputStream(cs.getInputStream());
    }

    @Override
    public String toString() {
        return "Client(" +
            "out = " + out +
            "in = " + in +
            "login = " + login + '\'' +
            ")";
    }

    public DataOutputStream getOut() {
        return out;
    }

    public DataInputStream getIn() {
        return in;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
