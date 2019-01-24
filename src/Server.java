import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

    public static void main(String[] args)  {
//        CountDownLatch latch = new CountDownLatch(1);
//        try {
//            new ServerJob();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        String message = "login:Nikita";
        //Pattern pattern = Pattern.compile(".+?:");
        Pattern pattern = Pattern.compile(":.+");
        Matcher matcher = pattern.matcher(message);
        String login;
        if( matcher.find() ) {
            login = matcher.group().replaceAll(":", "");
            System.out.println(login);
        }
    }
}