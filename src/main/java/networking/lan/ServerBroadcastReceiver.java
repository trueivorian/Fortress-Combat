package networking.lan;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

/**
 * The UDP packet receiver for servers - receives the broadcasted packet asking for server details
 */
public class ServerBroadcastReceiver extends Thread {
    private static final int broadcastPort = 3980;
    private static final int responsePort = 3981;
    private boolean running = true;
    private DatagramSocket socket;
    private String gameName = "Sample game name";
    private Boolean isPassworded = false;
    private Integer playerCount = 0;
    private byte[] buff = new byte[256];

    public ServerBroadcastReceiver() throws SocketException {
        socket = new DatagramSocket(broadcastPort);
    }

    @Override
    public void run() {
        System.out.println("Hello, world");
        Gson gson = new Gson();

        // Constantly receive packets and respond to them if they're valid
        while (running) {
            try {
                buff = new byte[256];
                DatagramPacket packet = new DatagramPacket(buff, buff.length);
                socket.receive(packet);
                System.out.println("received pkt");

                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                // Check it's the valid broadcast port first
                if (port == responsePort) {
                    JsonReader jsonReader = new JsonReader(new StringReader(new String(packet.getData())));
                    jsonReader.setLenient(true);
                    HashMap sData = gson.fromJson(jsonReader, HashMap.class);

                    // Check we're running the same protocol as them before replying
                    if (sData.containsKey("COMMAND") && sData.containsKey("GAME")) {
                        if (sData.get("COMMAND").equals("FIND_SERVER") && sData.get("GAME").equals("LastOfUs")) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("COMMAND", "FIND_SERVER_RESP");
                            data.put("GAME_NAME", gameName);
                            data.put("IS_PASSWORDED", isPassworded.toString());
                            data.put("PLAYER_COUNT", playerCount.toString());

                            buff = gson.toJson(data).getBytes();

                            // Send the response
                            packet = new DatagramPacket(buff, buff.length, address, responsePort);
                            Thread.sleep(200);
                            socket.send(packet);
                            System.out.println("Server queries, sent response to " + address.toString());
                        }
                    }
                } else {
                    System.out.println("port: " + port);
                }
            } catch (IOException | InterruptedException e ){
                // Nothing to do here!
            }
        }

        socket.close();
    }

    /**
     * Stops the server receiver
     */
    public void disconnect() {
        running = false;
    }
}
