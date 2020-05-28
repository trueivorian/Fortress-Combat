package networking.client;

import com.google.gson.Gson;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Sam Gunner on 25/01/2019.
 */
public class GameClientSender extends Thread {
    private BlockingQueue<String> messages;
    private PrintStream printStream;
    private Gson gson;
    private boolean runSender = true;

    public GameClientSender(BlockingQueue<String> messages, PrintStream printStream) {
        this.messages = messages;
        this.printStream = printStream;
        gson = new Gson();
    }

    public BlockingQueue<String> getMessages() {
        return messages;
    }

    @Override
    public void run() {
        while (runSender) {
            try {
                String message = messages.take();
                printStream.println(message);
            } catch (InterruptedException e) {
                HashMap<String, String> toSend = new HashMap<>();
                toSend.put("mode", "CLIENT_DISCONNECT");
                String serialised = gson.toJson(toSend);

                printStream.print(serialised);
                printStream.close();
                break;
            }
        }
    }

    /**
     * Forcefully stops the thread.
     */
    public void disconnect() {
        runSender = false;
    }
}
