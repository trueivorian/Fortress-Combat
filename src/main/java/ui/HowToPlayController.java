/**
 * Sample Skeleton for 'howtoplay.fxml' Controller Class
 */

package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class HowToPlayController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    void returnToMenu(ActionEvent event) {
        Launcher.launchScreen("main_menu");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    }
}
