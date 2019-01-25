import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Server {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            new ServerJob();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}