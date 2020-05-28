package networking;

import networking.client.GameClient;
import networking.exceptions.ServerNotFoundException;
import networking.lan.ClientBroadcastSender;
import networking.lan.ServerBroadcastReceiver;
import networking.server.GameServer;
import ui.Launcher;

import java.net.SocketException;

/**
 * This class is used to host games and join games
 */
public class MultiplayerGame {
    private ServerBroadcastReceiver serverBroadcastReceiver;

    /**
     * Used when hosting a game - this launches the server and the client and then joins the server
     * @param username The username to join the server with
     */
    public MultiplayerGame(String username) throws ServerNotFoundException {
        try {
            GameServer gameServer = new GameServer("Unnamed game", 2);
            gameServer.start();
            GameClient gameClient = new GameClient(username, "localhost");
        } catch (ServerNotFoundException e) {
            Launcher.showAlert("An error occurred whilst attempting to start the server.");
        }

        init();
    }

    /**
     * Used when joining a hosted game
     * @param username The username to join the server with
     * @param address The address to join
     * @throws ServerNotFoundException Thrown when a valid server is not running on the address provided.
     */
    public MultiplayerGame(String username, String address) throws ServerNotFoundException {
        GameClient gameClient;
        try {
            gameClient = new GameClient(username, address);
        } catch (ServerNotFoundException e) {
            Launcher.showAlert("An error occurred whilst attempting to join the game.");
        }
    }

    public void stop() {
        serverBroadcastReceiver.disconnect();
    }

    private void init() {
        try {
            serverBroadcastReceiver = new ServerBroadcastReceiver();

            serverBroadcastReceiver.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
