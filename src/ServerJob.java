import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerJob {
    private ServerSocket serverSocket = new ServerSocket(Constant.SOCKET_PORT);
    private List<Client> pull = new ArrayList<>();
    private Flowable<Message> in;
    private SimpleDateFormat sdf;

    public ServerJob() throws IOException {
        connectClient();
    }

    private void connectClient() {
        Flowable.interval(50, TimeUnit.MILLISECONDS, Schedulers.io())
            .flatMap(v -> Flowable.just(serverSocket.accept())).flatMap(cl -> {
                Client client = new Client(cl);
                Message message = new Message();
                String inMessage = client.getIn().readUTF();
                message.setCmd(parseMessage(inMessage, Constant.PATTERN_CMD));
                message.setName(parseMessage(inMessage, Constant.PATTERN_LOGIN));
                if (message.getCmd().equals(Constant.TAG_LOGIN)){
                    client.setName(message.getName());
                    pull.add(0, client);
                }
                return Flowable.just(message);
            })
            .filter(Objects::nonNull)
            .subscribe(System.out::println, Throwable::printStackTrace);
    }

    private String parseMessage(String message, String cmdPattern) {
        Pattern pattern = Pattern.compile(cmdPattern);
        Matcher matcher = pattern.matcher(message);
        return "";
    }
}

//    String lon = login.replaceAll(":", " ");
//    System.out.println(lon);

