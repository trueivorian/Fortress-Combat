package networking.lan;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import ui.JoinGameController;
import ui.Launcher;

import java.io.IOException;
import java.io.StringReader;
import java.net.*;
import java.util.HashMap;

/**
 * UDP packet sender for clients - looks for servers and adds them to a list of found servers
 */
public class ClientBroadcastSender extends Thread {
    private static final int broadcastPort = 3980;
    private static final int responsePort = 3981;
    private DatagramSocket socket;
    private byte[] buffer;
    private Gson gson;
    private boolean running = true;

    /**
     * Test entry point for finding servers
     * @param args
     */
    public static void main(String[] args) {
        try {
            ServerBroadcastReceiver serverBroadcastReceiver = new ServerBroadcastReceiver();
            ClientBroadcastSender clientBroadcastSender = new ClientBroadcastSender();
            serverBroadcastReceiver.start();
            clientBroadcastSender.start();
            clientBroadcastSender.startSearching();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Instantiates the object and sets some necessary variables
     */
    public ClientBroadcastSender() {
        gson = new Gson();
        try {
            socket = new DatagramSocket(responsePort);
        } catch (IOException e) {
            Launcher.showAlert("An internal networking error has occurred: " + e.getMessage());
        }
    }

    /**
     * Starts searching for servers - call thread.start() first
     */
    public void startSearching() {
        try {
            socket.setBroadcast(true);

            HashMap<String, Object> toSend = new HashMap<>();
            toSend.put("COMMAND", "FIND_SERVER");
            toSend.put("GAME", "LastOfUs");
            String serialised = gson.toJson(toSend);
            buffer = serialised.getBytes();

            InetAddress address = InetAddress.getByName("255.255.255.255");
            DatagramPacket pkt = new DatagramPacket(buffer, buffer.length, address, broadcastPort);
            socket.send(pkt);
            System.out.println("sent pkt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancels the searching of servers
     */
    public void stopSearching() {
        running = false;
    }

    @Override
    public void run() {
        Gson gson = new Gson();

        while (running) {
            try {
                buffer = new byte[256];
                DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);
                socket.receive(pkt);

                InetAddress address = pkt.getAddress();
                System.out.println("Received server query response from " + address.getHostAddress());


                JsonReader jsonReader = new JsonReader(new StringReader(new String(pkt.getData())));
                jsonReader.setLenient(true);
                HashMap sData = gson.fromJson(jsonReader, HashMap.class);

                String toShow = address.toString();
                if (toShow.startsWith("/"))
                    toShow = toShow.substring(1);

                JoinGameController.addServer((String) sData.get("GAME_NAME"), toShow);
                System.out.println("added " + (String) sData.get("GAME_NAME"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        socket.close();
    }
}
