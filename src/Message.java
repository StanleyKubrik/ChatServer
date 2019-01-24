public class Message {
    private String cmd;
    private String name;
    private String message;

    @Override
    public String toString() {
        return "Message(" +
            "name = " + name + "; " +
            "message = " + message + "; " +
            "cmd = " + cmd +
            ")";
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
