package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class SettingsController {

    @FXML // fx:id="soundfxSlider"
    private Slider soundfxSlider; // Value injected by FXMLLoader

    @FXML // fx:id="musicSlider"
    private Slider musicSlider; // Value injected by FXMLLoader

    @FXML
    void updateMusic(MouseEvent event) {
        double musicVolume =  musicSlider.getValue();
        //setMusicSlider(musicVolume);
        Launcher.adjustVolume(musicVolume);
    }

    @FXML
    void updateSFX(MouseEvent event) {
        double vol = soundfxSlider.getValue();
        //setSoundfxSlider(vol);
        Launcher.adjustSfx(vol);
    }

    private void setMusicSlider(double sliderVal) {
        musicSlider.setValue(sliderVal);
    }

    private void setSoundfxSlider(double sfxVal) {
        soundfxSlider.setValue(sfxVal);
    }

    @FXML
    void playTestSfx(ActionEvent event) {
        Launcher.playTestSFX();
    }

    @FXML
    void goBack(ActionEvent event) {
        Launcher.launchScreen("main_menu");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert soundfxSlider != null : "fx:id=\"soundfxSlider\" was not injected: check your FXML file 'settings.fxml'.";
        assert musicSlider != null : "fx:id=\"musicSlider\" was not injected: check your FXML file 'settings.fxml'.";
    }
}
