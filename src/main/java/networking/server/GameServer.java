package networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class GameServer extends Thread {
    private static final int HOST_PORT = 3979;
    private int numPlayers;
    private ServerLogic serverLogic;

    /**
     * Instantiates the class
     * @param numPlayers The number of players to be playing
     */
    public GameServer(String gameName, int numPlayers) {
        ClientTable.setGameName(gameName);
        this.numPlayers = numPlayers;
    }

    /**
     * Sets a password for the game. This can only be used once GameServer has been instantiated and cannot be used once the game has started.
     * @param password The password for the game
     */
    public void setGamePassword(String password) {
        ClientTable.setIsPassworded(true);
        ClientTable.setGamePassword(password);
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(HOST_PORT);
            serverLogic = new ServerLogic(new LinkedBlockingQueue<>());
            serverLogic.start();
            ClientTable.init(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());

            // Loop until we've got all the clients connected
            while (ClientTable.getNumberPlayersConnected() < numPlayers) {
                Socket ss = socket.accept();
                int userid = ClientTable.addUser(ss, false);
                GameServerReceiver gsr = new GameServerReceiver(new BufferedReader(new InputStreamReader(ss.getInputStream())), userid); // Create receiver and start thread
                gsr.start();
            }

            System.out.println("Server has received all players - terminating bootstrapping thread");

            // Now, we can terminate this thread.
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("all clients connected. main thread terminating.");
    }
}
