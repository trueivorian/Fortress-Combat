/*
 * Sample Skeleton for 'main_menu.fxml' Controller Class
 */
package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController {

    @FXML
    void exitGame(ActionEvent event) {
        System.exit(42069);
    }

    @FXML
    void openHowToPlay(ActionEvent event) {
        Launcher.launchScreen("howtoplay");
    }

    @FXML
    void openPlayMenu(ActionEvent event) {
        Launcher.launchScreen("play_menu");
    }

    @FXML
    void openSettings(ActionEvent event) {
        Launcher.launchScreen("settings");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }
}
