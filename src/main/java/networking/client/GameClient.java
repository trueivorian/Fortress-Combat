package networking.client;

import com.google.gson.Gson;
import gamelogic.Card;
import networking.CommandType;
import networking.exceptions.ServerNotFoundException;
import ui.PlayMenuController;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Sam Gunner on 25/01/2019.
 */
public class GameClient {
    private String username;
    private ArrayList<String> otherUsers;
    private String hostAddress;
    private int hostPort = 3979;
    private GameClientReceiver receiver;
    private static GameClientSender sender;
    private static Gson gson;
    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<String> deck = new ArrayList<>();
    private ArrayList<String> board = new ArrayList<>();

    /**
     * Instantiates the class - with no password
     * @param username The username to connect with
     * @param ip The address for the host to connect to
     * @throws ServerNotFoundException Thrown when the server cannot be connected to.
     */
    public GameClient(String username, String ip) throws ServerNotFoundException {
        this(username, ip, ""); // Connect to the server with no password
    }

    /**
     * Instantiates the class - with a password
     * @param username The username to connect with
     * @param ip The address for the host to connect to
     * @param password The password to attempt to join the game with
     * @throws ServerNotFoundException Thrown when the server cannot be connected to.
     */
    public GameClient(String username, String ip, String password) throws ServerNotFoundException {
        this.username = username;
        this.hostAddress = ip;
        gson = new Gson();
        System.out.println("Client started");

        try {
            Socket server = new Socket(ip, hostPort); // Attempt to form a connection
            PrintStream toServer = new PrintStream(server.getOutputStream());
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

            BlockingQueue<String> q = new LinkedBlockingQueue<>();

            // Attempt to form the threads and run them
            receiver = new GameClientReceiver(fromServer, q, username);
            sender = new GameClientSender(q, toServer);

            receiver.start();
            sender.start();

            // A test deck
            ArrayList<Integer> acceptableIDs = new ArrayList<>();
            acceptableIDs.add(16);
            acceptableIDs.add(10);
            acceptableIDs.add(18);
            acceptableIDs.add(28);
            acceptableIDs.add(36);
            acceptableIDs.add(42);

//            ArrayList<>

            // Form command and convert to json, send.
            Map<Object, Object> data = new HashMap<>();
            data.put("COMMAND", CommandType.JOIN_GAME);
            data.put("USERNAME", username);
            ArrayList<Integer> deckData = getDeckFromFile();
            data.put("DECK_DATA", deckData);
            data.put("PASSWORD", password);
            sender.getMessages().add(gson.toJson(data));

        } catch (IOException e) {
            forceDisconnect();

            throw new ServerNotFoundException(ip, hostPort);
        }
    }

    /**
     * Sends a message to the server from this user - can only be used once a game has been joined!
     * @param message The message to be sent
     */
    public static void sendMessage(HashMap<Object, Object> message) {
        String json = gson.toJson(message);
        sender.getMessages().add(json);
    }
    private ArrayList<Integer> getDeckFromFile(){

        String fileName = PlayMenuController.getDeck();

        ArrayList<Integer> deck = new ArrayList<>();
        String line = null;
        try {

            FileReader fileReader =
                    new FileReader(fileName);

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                deck.add(Integer.parseInt(line));
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
    }
        return deck;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public ArrayList<String> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<String> deck) {
        this.deck = deck;
    }

    public ArrayList<String> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<String> board) {
        this.board = board;
    }

    /**
     * Forces a disconnection from the server
     */
    public void forceDisconnect() {
        if (sender != null) {
            sender.disconnect();
        }
        if (receiver != null) {
            receiver.disconnect();
        }
    }
}