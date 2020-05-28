package networking.server;

import com.google.gson.Gson;
import networking.CommandType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sam Gunner on 25/01/2019.
 */
public class GameServerReceiver extends Thread implements Runnable {
    private BufferedReader bufferedReader;
    private Gson gson;
    private int userid;

    public GameServerReceiver(BufferedReader bufferedReader, int userid) {
        this.bufferedReader = bufferedReader;
        this.gson = new Gson();
        this.userid = userid;
    }

    @Override
    public void run() {
        String dataReceived;

        while (true) {
            try {
                // Wait for a command to be sent
                dataReceived = bufferedReader.readLine();
                HashMap structuredData = gson.fromJson(dataReceived, HashMap.class);

                // Check that it's a valid format
                if (structuredData.containsKey("COMMAND")) {
                    System.out.println("SERVER | " + dataReceived);
                    CommandType command = CommandType.valueOf((String) structuredData.get("COMMAND"));
                    structuredData.put("USER_ID", userid);

                    // Decide what to do with the command
                    switch (command) {
                        case TERMINATE_CONN:
                            handleTerminateConn(structuredData);
                            return;
                        case JOIN_GAME:
                            handleJoinGame(structuredData);
                            break;
                        default: // Otherwise, we pass it through to ServerLogic
                            ServerLogic.handle(command, structuredData);
                            break;
                    }
                }
            } catch (IOException e) { // The client has closed the connection
                handleSuddenDisconnect();
                break;
            }
        }
    }

    /**
     * Handles the termination of the current connection.
     * @param data The data received from the user
     */
    private void handleTerminateConn(HashMap data) {
        try {
            ClientTable.disconnectUser(userid);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the JOIN_GAME command
     * @param structuredData The HashMap with the command data
     */
    private void handleJoinGame(HashMap structuredData) {
        // Check all required parameters have been specified.
        if (structuredData.containsKey("USERNAME") && structuredData.containsKey("DECK_DATA")) {
            // We also need to consider the fact that the game might be passworded
            if (ClientTable.isPassworded()) {
                if (structuredData.containsKey("PASSWORD")) {
                    String pwd = (String) structuredData.get("PASSWORD");
                    if (ClientTable.getGamePassword().equals(pwd)) { // Check the password is correct
                        ClientTable.confirmUser(userid, (String) structuredData.get("USERNAME"));
                    } else { // Password is incorrect
                        sendUnsuccessfulJoinMessage("Incorrect game password provided.");
                        ClientTable.disconnectUser(userid);
                        return;
                    }
                } else { // Password not specified - assume incorrect
                    sendUnsuccessfulJoinMessage("Incorrect game password provided.");
                    ClientTable.disconnectUser(userid);
                    return;
                }
            } else { // User successfully joined game
                ClientTable.confirmUser(userid, (String) structuredData.get("USERNAME"));
            }
        } else { // Not all parameters specified.
            sendUnsuccessfulJoinMessage("Received malformed JOIN_GAME command");
            ClientTable.disconnectUser(userid);
            return;
        }

        // Now, set deck data in client table
        if (structuredData.get("DECK_DATA") instanceof ArrayList) {
            ClientTable.setDeckData(userid, (ArrayList<Double>) structuredData.get("DECK_DATA"));
        } else {
            System.out.println("not there");
        }
    }

    /**
     * Called when the client unexpectedly closes the connection
     */
    private void handleSuddenDisconnect() {
        if (ServerLogic.gameHasStarted()) { // We need to inform the other player that they have won
            ArrayList<User> users = ClientTable.getConfirmedUsers();
            for (User user : users) {
                if (user.getUserId() != userid) {
                    try {
                        HashMap<Object, Object> data = new HashMap<>();
                        data.put("COMMAND", "GAME_END");
                        data.put("WINNER_ID", user.getUserId());
                        ClientTable.sendMessageToUser(user.getUserId(), data);
                    } catch (IOException e) { // If something goes wrong (which it shouldn't!)

                    }
                    break;
                }
            }
        }

        ClientTable.disconnectUser(userid);
    }

    // end handlers
    private void sendUnsuccessfulJoinMessage(String message) {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("COMMAND", CommandType.JOIN_GAME_RESP);
        data.put("SUCCESS", false);
        data.put("MESSAGE", message);
        try {
            ClientTable.sendMessageToUser(userid, data);
        } catch (IOException e) {
            // We're already trying to disconnect them, so we don't need to worry about doing anything to save this connection
        }
    }
}
