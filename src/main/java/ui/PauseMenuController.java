/**
 * Sample Skeleton for 'pause_menu.fxml' Controller Class
 */

package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PauseMenuController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="soundfxSlider"
    private Slider soundfxSlider; // Value injected by FXMLLoader

    @FXML // fx:id="musicSlider"
    private Slider musicSlider; // Value injected by FXMLLoader

    @FXML
    void exitGame(ActionEvent event) {
        // exit pause menu idk
    }

    @FXML
    void forfeitGame(ActionEvent event) {
        // tell networking and gamelogic to forfeit game
        System.out.println("to do ");
    }

    @FXML
    void togglePause(ActionEvent event) {
        // exit pause menu idk
    }

    @FXML
    void updateMusic(MouseEvent event) {
        double musicVolume =  musicSlider.getValue();
        Launcher.adjustVolume(musicVolume);
    }

    @FXML
    void updateSFX(MouseEvent event) {
        double vol = soundfxSlider.getValue();;
        Launcher.adjustSfx(vol);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert soundfxSlider != null : "fx:id=\"soundfxSlider\" was not injected: check your FXML file 'pause_menu.fxml'.";
        assert musicSlider != null : "fx:id=\"musicSlider\" was not injected: check your FXML file 'pause_menu.fxml'.";

    }
}
