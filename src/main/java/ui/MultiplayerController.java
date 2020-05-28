package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import networking.MultiplayerGame;
import networking.exceptions.ServerNotFoundException;

import java.util.Random;

public class MultiplayerController {

    private String username;
    private Boolean registered = false;

//    @FXML // fx:id="gameCombo"
//    private ComboBox<String> gameCombo; // Value injected by FXMLLoader
//
//    @FXML // fx:id="difficultyCombo"
//    private ComboBox<String> difficultyCombo; // Value injected by FXMLLoader

    @FXML // fx:id="userEntryBox"
    private TextField userEntryBox; // Value injected by FXMLLoader

    @FXML
    void setName(ActionEvent event) {
        username = userEntryBox.getText();
        if (!username.equals("")) {
            userEntryBox.clear();
            registered = true;
            //System.out.println("new username is " + username);
        } else {
            registered = false;
            //System.out.println("null username");
        }
    }

    @FXML
    void startGame(ActionEvent event) {
        Random random = new Random();
        boolean debugMode = false;// this autogenerates inputs so that we can test the game faster, in the final
        if (debugMode) {
            //System.out.println("hey there");
            registered = true;
            username = "Random user " + random.nextInt(100000000);
            //System.out.println("debug mode in multiplayer - username set to " + username);
        }
        if (registered) {
//            String difficulty = difficultyCombo.getValue();
//            String gameStyle = gameCombo.getValue();
//            System.out.println("start game with settings " + difficulty + " " + gameStyle + " with username "+ username);
            Launcher.launchGame();
            try {
                MultiplayerGame multiplayerGame = new MultiplayerGame(username);

                // do we need a variable if we do not perform functions? could just do new MultiplayerGame(username);
            } catch (ServerNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            //System.out.println("must pick a username");
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        Launcher.launchScreen("play_menu");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
//        background.fitWidthProperty().bind(Anchor.widthProperty());
//        background.fitHeightProperty().bind(Anchor.heightProperty());
//        gameCombo.getItems().clear();
//        gameCombo.getItems().addAll("1v1", "2v2");
//        gameCombo.setValue("1v1");
//        difficultyCombo.getItems().clear();
//        difficultyCombo.getItems().addAll("Easy", "Medium", "Hard");
//        difficultyCombo.setValue("Easy");
//        assert gameCombo != null : "fx:id=\"gameCombo\" was not injected: check your FXML file 'multiplayer.fxml'.";
//        assert difficultyCombo != null : "fx:id=\"difficultyCombo\" was not injected: check your FXML file 'multiplayer.fxml'.";
        assert userEntryBox != null : "fx:id=\"userEntryBox\" was not injected: check your FXML file 'multiplayer.fxml'.";
    }
}
