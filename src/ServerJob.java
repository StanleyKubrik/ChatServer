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
            .flatMap(v -> Flowable.just(serverSocket.accept()))
            .flatMap(cl -> {
                Client client = new Client(cl);
                Message message = new Message();
                String inMessage = client.getIn().readUTF();
                message.setCmd(parseMessage(inMessage, Constant.PATTERN_CMD));
                message.setName(parseMessage(inMessage, Constant.PATTERN_MESSAGE));
                if (message.getCmd().equals(Constant.TAG_LOGIN)){
                    client.setName(message.getName());
                    pull.add(0, client);
                }
                sendForAll(client.getName(), Constant.TAG_CONNECT_CLIENT);
                return Flowable.just(message);
            })
            .filter(Objects::nonNull)
            .subscribe(System.out::println, Throwable::printStackTrace);

        in = Flowable.interval(50, TimeUnit.MILLISECONDS, Schedulers.io())
                .flatMap(v -> {
                    Message message = new Message();
                    pull.forEach(client -> {
                        try {
                            if (client.getIn().available() > 0) {
                                message.setName(client.getName());
                                String inMessage = client.getIn().readUTF();
                                switch (parseMessage(inMessage, Constant.PATTERN_CMD)) {
                                    case Constant.TAG_MESSAGE:
                                        try {
                                            sendForAll(parseMessage(inMessage, Constant.PATTERN_MESSAGE));
                                        } catch (Exception e) {
                                            client.getCs().close();
                                        }
                                        break;
                                    case Constant.TAG_EXIT:
                                        sendForAll(message.getName(), Constant.TAG_DISCONNECT_CLIENT);
                                        break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    return Flowable.just(message);
                });
        listener();
    }

//    private Message checkClient(List<Client> pull){
//        Message message = new Message();
//        pull.forEach(client -> {
//            try {
//                if (client.getIn().available() > 0) {
//                    message.setName(client.getName());
//                    String inMessage = client.getIn().readUTF();
//                    switch (parseMessage(inMessage, Constant.PATTERN_CMD)) {
//                        case Constant.TAG_MESSAGE:
//                            try {
//                                sendForAll(parseMessage(inMessage, Constant.TAG_MESSAGE));
//                            } catch (Exception e) {
//                                client.getCs().close();
//                                // pull.remove(client);
//                            }
//                            break;
//                        case Constant.TAG_EXIT:
//                            sendForAll(message.getName(), Constant.TAG_DISCONNECT_CLIENT);
//                            // pull.remove(client);
//                            break;
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        return message;
//    }

    private void listener(){
        in.filter(message -> message.getCmd() != null && message.getMessage() != null)
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    private String parseMessage(String message, String cmdPattern) {
        Pattern pattern = Pattern.compile(cmdPattern);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()){
            return matcher.group().replaceAll(":", " ").trim();
        } else {
            return "";
        }
    }

    // TODO sendForAll (Message message)
    private void sendForAll(String message, String tag){
        pull.forEach(client -> {
            try {
                client.getOut().writeUTF(message.concat(tag));
                client.getOut().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendForAll(String message){
        pull.forEach(client -> {
            try {
                client.getOut().writeUTF(client.getName().concat(": ").concat(message));
                client.getOut().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}