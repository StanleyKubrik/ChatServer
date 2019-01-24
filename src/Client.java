import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private DataOutputStream out;
    private DataInputStream in;
    private Socket cs;
    private String name;

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
            "name = " + name + '\'' +
            ")";
    }

    public DataOutputStream getOut() {
        return out;
    }

    public DataInputStream getIn() {
        return in;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getCs() {
        return cs;
    }
}
