/**
 * Sample Skeleton for 'join_game.fxml' Controller Class
 */

package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import networking.MultiplayerGame;
import networking.exceptions.ServerNotFoundException;
import networking.lan.ClientBroadcastSender;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class JoinGameController {

    private String user;

    private boolean registered = false;

    @FXML // fx:id="msgLabel"
    private Label msgLabel; // Value injected by FXMLLoader

    private static ObservableList<String> serverList = FXCollections.observableArrayList();

    @FXML // fx:id="ipEntryBox"
    private TextField ipEntryBox; // Value injected by FXMLLoader

    @FXML // fx:id="userEntryBox"
    private TextField userEntryBox; // Value injected by FXMLLoader

    @FXML // fx:id="listView?"
    private ListView<String> listView; // Value injected by FXMLLoader

    private ClientBroadcastSender serverFinder;

    @FXML
    void goBack(ActionEvent event) {
        serverFinder.stopSearching();
        Launcher.launchScreen("play_menu");
    }

    private void connectServerFinder() {
        try {
            serverFinder = new ClientBroadcastSender();
            serverFinder.start();
            serverFinder.startSearching();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void refreshIP(ActionEvent event) {
        listView.setItems(serverList);
    }

    public static void addServer(String roomName, String ipAddress){
        String roomNameAndAddress = roomName + "@" + ipAddress;
        serverList.add(roomNameAndAddress);
    }

    @FXML
    void setName(ActionEvent event) {
        user = userEntryBox.getText();
        if (!user.equals("")) {
            userEntryBox.clear();
            registered = true;
            msgLabel.setText("new username is " + user);
            user = userEntryBox.getText();
        } else {
            registered = false;
            msgLabel.setText("null username");
        }
    }

    @FXML
    void listClicked(MouseEvent event) {
        //System.out.println("i get here");
        String listItemClicked = listView.getSelectionModel().getSelectedItem();
        //System.out.println(listItemClicked);
        String serverAddress = listItemClicked.split("@")[1];
        //System.out.println(serverAddress);
        ipEntryBox.setText(serverAddress);
    }



    @FXML
    void startGame(ActionEvent event) {
        serverFinder.stopSearching();
        Random random = new Random();
        String address = ipEntryBox.getText(); // sam checks ip for errors not me (apart from empty)
        // this auto-generates inputs so that we can test the game faster, in the final product we wont need this
        boolean debugMode = false;
        if (debugMode) {
            registered = true;
            user = "Random user " + random.nextInt(100000000);
            address = "localhost";
            //System.out.println("joining game creating user " + user + "to server " + address);
        }
        if ((registered && (!address.equals("")))) {
            try {
                System.out.println("passing address " + address +" to multiplayergame");
                //System.out.println("joining game with user" + user);
                //System.out.println("joining with default ip " + address);
                MultiplayerGame multiplayerGame = new MultiplayerGame(user, address);
                Launcher.launchGame();
            } catch (ServerNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            msgLabel.setText("Must set a username and server");
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //serverListBox.setItems(serverList);
        assert msgLabel != null : "fx:id=\"msgLabel\" was not injected: check your FXML file 'join_game.fxml'.";
        assert ipEntryBox != null : "fx:id=\"ipEntryBox\" was not injected: check your FXML file 'join_game.fxml'.";
        assert userEntryBox != null : "fx:id=\"userEntryBox\" was not injected: check your FXML file 'join_game.fxml'.";
        assert listView != null : "fx:id=\"serverList\" was not injected: check your FXML file 'join_game.fxml'.";
        connectServerFinder();
    }
}
