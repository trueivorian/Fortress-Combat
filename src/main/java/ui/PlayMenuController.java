package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import networking.MultiplayerGame;


import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;


public class PlayMenuController {

    @FXML // fx:id="deckCombo"
    private ComboBox<String> deckCombo; // Value injected by FXMLLoader

    @FXML // fx:id="deckDetectionLabel"
    private Label deckDetectionLabel; // Value injected by FXMLLoader

    private static String deckFileName;

    private ObservableList<String> detectedDecks = FXCollections.observableArrayList();

    private boolean deckChosen = false;

    @FXML
    void openDeckEdit(ActionEvent event) {
        Launcher.launchScreen("deck_menu");
    }

    @FXML
    void openHostGame(ActionEvent event) {
        if (deckChosen) {
            Launcher.launchScreen("multiplayer");
        } else {
            Launcher.showAlert("Deck must be chosen to play");
        }
    }

    @FXML
    void openJoinGame(ActionEvent event) {
        if (deckChosen) {
            Launcher.launchScreen("join_game");
        } else {
            Launcher.showAlert("Deck must be chosen to play");
        }
    }

    @FXML
    void playSoloOne(ActionEvent event) {
        if (deckChosen) {
            Launcher.launchAIGame();
        } else {
            Launcher.showAlert("Deck must be chosen to play");
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        Launcher.launchScreen("main_menu");
    }

    @FXML
    void setDeck(ActionEvent event) {
        deckFileName = deckCombo.getValue();
        if (deckFileName != null) {
            deckChosen = true;
            deckDetectionLabel.setText("Loaded "+ deckFileName);
        } else {
            deckDetectionLabel.setText("No Deck");
            deckChosen = false;
        }
        //System.out.println(deckFileName);
    }

    boolean isDeckChosen() {
        return deckChosen;
    } // 

    public static String getDeck() {
        return deckFileName;
    }

    private void fillDeckCombo() {
        File[] deckList = getAllDecks();
        for (File f : deckList) {
            detectedDecks.add(f.getName());
        }
        deckCombo.getItems().addAll(detectedDecks);
    }



    private File[] getAllDecks() {
        File dir = new File(System.getProperty("user.dir"));
        return dir.listFiles((dir1, filename) -> filename.endsWith(".deck"));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //System.out.println("hello again");
        deckCombo.getItems().clear();
        fillDeckCombo();
        assert deckCombo != null : "fx:id=\"deckCombo\" was not injected: check your FXML file 'play_menu.fxml'.";
        assert deckDetectionLabel != null : "fx:id=\"deckDetectionLabel\" was not injected: check your FXML file 'play_menu.fxml'.";
    }
}

