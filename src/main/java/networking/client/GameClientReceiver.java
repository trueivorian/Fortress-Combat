package networking.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import networking.CommandType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Sam Gunner on 25/01/2019.
 */
public class GameClientReceiver extends Thread {
    private BufferedReader bufferedReader;
    private Gson gson;
    private BlockingQueue<String> messages;
    private ArrayList<User> joinedUsers; // This will contain a list of all currently authenticated users
    private ClientHandler clientHandler;
    private boolean runReceiver = true;

    /**
     * Creates a new instance of GameClientReceiver
     * @param bufferedReader The reader from the server
     * @param messageQueue The message queue to be sent to the server
     */
    public GameClientReceiver(BufferedReader bufferedReader, BlockingQueue<String> messageQueue, String username) {
        this.bufferedReader = bufferedReader;
        gson = new Gson();
        this.messages = messageQueue;
        joinedUsers = new ArrayList<>();
        clientHandler = new ClientHandler(username);
    }

    @Override
    public void run() {
        String dataReceived;
        HashMap structuredData;

        while (runReceiver) {
            try {
                dataReceived = bufferedReader.readLine();
                structuredData = gson.fromJson(dataReceived, HashMap.class);

                if (structuredData.containsKey("COMMAND")) {
                    String cmds = (String) structuredData.get("COMMAND");
                    CommandType cmd = CommandType.valueOf(cmds);
                    System.out.println("CLIENT - RECEIVING: " + dataReceived);

                    switch(cmd) {
                        default:
                            clientHandler.handle(cmd, structuredData);
                            break;
                    }
                }
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                break;
            } catch (JsonSyntaxException e) {
                //
            }
            catch (NoSuchMethodException e){

            }
        }
    }

    /**
     * Forcefully stops the thread
     */
    public void disconnect() {
        runReceiver = false;
    }
}
