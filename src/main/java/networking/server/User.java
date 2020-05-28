package networking.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class User extends Thread {
    private BlockingQueue<String> messages; // Messages yet to be sent to the client
    private String name; // The username of the client
    private boolean isHost; // Whether or not the client is the host
    private int id;
    private Socket socket;
    private ArrayList<Integer> deck;
    private static Gson gson = new Gson();

    public User(String username, BlockingQueue<String> messages, Socket socket, boolean isHost) {
        this.name = username;
        this.messages = messages;
        this.isHost = isHost;
        this.socket = socket;
    }

    public User(String username, BlockingQueue<String> messages, Socket socket) {
        new User(username, messages, socket, false);
    }

    @Override
    public void run() {
        try {
            PrintStream printStream = new PrintStream(this.socket.getOutputStream());

            while (true) {
                String message = messages.take();
                System.out.println("SERVER - SENDING: " + message);
                printStream.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // thread ended
        }
    }

    public void sendMessage(String message) throws IOException {
        messages.add(message);
    }

    public void sendMessage(HashMap<Object, Object> data) throws IOException {
        String json = gson.toJson(data);
        sendMessage(json);
    }

    public boolean isHost() {
        return isHost;
    }

    /**
     * Forcefully closes the connection with the client
     */
    public void closeConnection() throws IOException {
        socket.close();
    }

    public BlockingQueue<String> getMessages() {
        return messages;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public void setUserId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return id;
    }

    public ArrayList<Integer> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Integer> deck) {
        this.deck = deck;
    }
}
