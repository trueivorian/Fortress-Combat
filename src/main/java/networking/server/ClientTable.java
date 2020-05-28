package networking.server;

import com.google.gson.Gson;
import networking.CommandType;
import networking.exceptions.UserIDNotFoundException;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Stores currently connected users, as well as some other information about the game
 */
public class ClientTable extends Thread {
    private static String gameName = "Unnamed game";
    private static boolean isPassworded = false;
    private static String gamePassword;
    private static ConcurrentHashMap<Integer, User> users;
    private static ConcurrentHashMap<Integer, Boolean> confirmedUsers;
    private static int hostID;
    private static int lastid = 0;
    private static int currentPlayerTurn = -1;
    private static Gson gson = new Gson();
    private static int numPlayers = 2;


    public static void init(ConcurrentHashMap<Integer, User> users1, ConcurrentHashMap<Integer, Boolean> users2) {
        users = users1;
        confirmedUsers = users2;
    }

    /**
     * Adds another user to the client table
     * @param isHost Whether or not the client is playing on the host machine
     */
    public static int addUser(Socket socket, boolean isHost) {
        User user = new User("unconfirmed", new LinkedBlockingQueue<>(), socket, isHost);
        user.start(); // start the user sender thread
        user.setUserId(lastid);
        if (isHost)
            hostID = lastid;
        confirmedUsers.put(lastid, false); // user is not confirmed yet
        int userid = lastid;
        lastid++;
        users.put(userid, user);
        return userid;
    }

    /**
     * Adds a new message to the queue for each user
     * @param data The data to be sent to each user
     */
    public static void broadcastMessage(Map<Object, Object> data) throws IOException {
        String json = gson.toJson(data);

        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            entry.getValue().sendMessage(json);
        }
    }

    /**
     * Sends a private message to a connected client
     * @param id The id of the client to send the message to
     * @param data The data to send to the client
     */
    public static void sendMessageToUser(int id, HashMap<Object, Object> data) throws IOException {
        if (users.containsKey(id)) {
            Gson tmp = new Gson();
            String json = tmp.toJson(data);
            users.get(id).sendMessage(json);
        }
    }

    /**
     * Returns the number of players currently connected
     * @return int The number of players currently connected
     */
    public static int getNumberPlayersConnected() {
        int sofar = 0;

        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            if (confirmedUsers.get(entry.getKey()))
                sofar++;
        }

        return sofar;
    }

    /**
     * Returns a list of all currently connected users
     * @return All users
     */
    public static Map<Integer, User> getUsers() {
        return users;
    }

    /**
     * Returns a list of all currently connected & confirmed users
     * @return All confirmed users
     */
    public static ArrayList<User> getConfirmedUsers() {
        ArrayList<User> toReturn = new ArrayList<>();

        // Loop through users and only return confirmed ones.
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            Integer userid = entry.getKey();
            if (confirmedUsers.containsKey(userid)) {
                if (confirmedUsers.get(userid)) {
                    toReturn.add(entry.getValue());
                }
            }
        }

        return toReturn;
    }

    /**
     * Confirms a user
     * @param userid The id of the user to confirm
     * @param username The username of the user to confirm
     */
    public static void confirmUser(int userid, String username) {
        confirmedUsers.replace(userid, false, true);

        if (users.containsKey(userid)) {
            User user = users.get(userid);
            user.setUserName(username);
            users.remove(userid);
            users.put(userid, user);
        }

        HashMap<Integer, String> toSendConfirmed = new HashMap<>();

        HashMap<Object, Object> data = new HashMap<>();
        data.put("COMMAND", CommandType.PLAYER_JOINED);
        data.put("PLAYER_ID", userid);
        data.put("USERNAME", username);

        // Alert all other users that a user has joined.
        for (Map.Entry<Integer, User> user : users.entrySet()) {
            if (confirmedUsers.get(user.getValue().getUserId()) && (user.getValue().getUserId() != userid)) { // Make sure we only send it to confirmed users that aren't the user that's just joined.
                toSendConfirmed.put(user.getKey(), user.getValue().getUserName());

                try {
                    user.getValue().sendMessage(data);
                } catch (IOException e) {
                    // Couldn't send message to the user - ignore for now.
                }
            }
        }

        // Now, send a JOIN_GAME_RESP command to the user that just joined - needs to contain the list of users already connected
        data = new HashMap<>();
        data.put("COMMAND", CommandType.JOIN_GAME_RESP);
        data.put("SUCCESS", true);
        data.put("PLAYER_LIST", toSendConfirmed);
        data.put("PLAYER_ID", userid);
        try {
            users.get(userid).sendMessage(data);
        } catch (IOException e) { // this shouldn't happen todo: maybe consider it
            e.printStackTrace();
        }
    }

    /**
     * Changes the current player's turn
     * @param playerid The ID of the player
     */
    public static void setCurrentPlayerturn(int playerid) {
        currentPlayerTurn = playerid;
    }

    /**
     * Forcefully disconnects a user's connection to the server.
     * @param userid The id of the user to connect
     */
    public static void disconnectUser(int userid) {
        if (users.containsKey(userid)) {
            User user = users.get(userid);
            try {
                user.closeConnection();
            } catch (IOException e) {
                // We weren't able to close the connection, but don't worry about it
            } finally {
                users.remove(userid);
                confirmedUsers.remove(userid);
            }
        }

        System.out.println("disconnected user");
    }

    public static boolean isPassworded() {
        return isPassworded;
    }

    public static void setIsPassworded(boolean boolVal) {
        isPassworded = boolVal;
    }

    public static String getGamePassword() {
        return gamePassword;
    }

    public static void setGamePassword(String password) {
        gamePassword = password;
    }

    public static String getGameName() {
        return gameName;
    }

    public static void setGameName(String name) {
        gameName = name;
    }

    /**
     * Returns all decks
     * @return The decks for the clients
     */
    public static HashMap<Integer, ArrayList<Integer>> getDecks() {
        HashMap<Integer, ArrayList<Integer>> decks = new HashMap<>();
        for (Map.Entry<Integer, User> user : users.entrySet()) {
            decks.put(user.getKey(), user.getValue().getDeck());
        }

        return decks;
    }

    /**
     * Returns a specified user from the list of currently connected users
     * @param id The id of the player to fetch
     * @return User The user
     * @throws UserIDNotFoundException Thrown when the specified user doesn't exist
     */
    private static User getUser(int id) throws UserIDNotFoundException {
        if (users.containsKey(id))
            return users.get(id);
        else
            throw new UserIDNotFoundException(id);
    }

    /**
     * Sets the deck data for a specified user
     * @param userid The user id for which the data should be sey
     * @param deck_data The deck data to set for the user
     */
    public static void setDeckData(Integer userid, ArrayList<Double> deck_data) {
        ArrayList<Integer> newDeck = new ArrayList<>();

        for (int i = 0; i < deck_data.size(); i++) {
            newDeck.add(deck_data.get(i).intValue());
        }

        User user = users.get(userid);
        user.setDeck(newDeck);
        users.replace(userid, user);

        int count = 0;
        for (Map.Entry<Integer, Boolean> entry : confirmedUsers.entrySet()) {
            if (entry.getValue())
                count++;
        }

        if (count == numPlayers) {
            ServerLogic.gameStart();
        }
    }

    /**
     * Broadcasts a message exclusively (READ: to all but one user)
     * @param userid
     * @param data
     */
    public static void exclusiveBroadcast(int userid, HashMap<Object, Object> data) {
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            if (entry.getValue().getUserId() != userid) {
                try {
                    entry.getValue().sendMessage(data);
                } catch (IOException e) {
                    // todo: something?
                }
            }
        }
    }
}