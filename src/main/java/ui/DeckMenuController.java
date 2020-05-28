package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class DeckMenuController {

    @FXML // fx:id="progressBar"
    private ProgressBar progressBar; // Value injected by FXMLLoader

    @FXML // fx:id="castleLabel"
    private Label castleLabel; // Value injected by FXMLLoader

    @FXML // fx:id="decreeLabel"
    private Label decreeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="soldierLabel"
    private Label soldierLabel; // Value injected by FXMLLoader

    @FXML // fx:id="mageLabel"
    private Label mageLabel; // Value injected by FXMLLoader

    @FXML // fx:id="tricksterLabel"
    private Label tricksterLabel; // Value injected by FXMLLoader

    @FXML // fx:id="deckLabel"
    private Label deckLabel; // Value injected by FXMLLoader

    @FXML // fx:id="deckCounter"
    private Label deckCounter; // Value injected by FXMLLoader

    static String cardGenre;

    static boolean castleChosen, mageChosen, decreeChosen, soldierChosen, tricksterChosen,deckFull = false;

    @FXML
    void deckFinish(ActionEvent event) {
        if (deckFull){
            ArrayList<Integer> ids = CardSelectionController.getCardIds();
            //System.out.println("unshuffled");
            //ids.forEach(id -> System.out.print(id + ","));
            int castleIndex = ids.indexOf(Collections.min(ids));
            int castle = ids.get(castleIndex);
            ids.remove(castleIndex);
            Collections.shuffle(ids); // shuffles deck and puts castle at the end
            ids.add(castle);
            //ids.forEach(id -> System.out.print(id + ","));
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
                String fileName = "Deck_" + formatter.format(new Date()) + ".deck";
                PrintWriter writer = new PrintWriter(fileName);
                ids.forEach(writer::println);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            deckLabel.setText("Deck Not Finished Mate");
        }
    }

    @FXML
    void openCastleSelect(ActionEvent event) {
        cardGenre = "Castle";
        Launcher.launchScreen("card_selection");
    }

    @FXML
    void openDecreeSelect(ActionEvent event) {
        cardGenre = "Decree";
        Launcher.launchScreen("card_selection");
    }

    @FXML
    void goBack(ActionEvent event) {
        Launcher.launchScreen("play_menu");
    }

    @FXML
    void openMageSelect(ActionEvent event) {
        cardGenre = "Mage";
        Launcher.launchScreen("card_selection");
    }

    @FXML
    void openSoldierSelect(ActionEvent event) {
        cardGenre = "Deck";
        Launcher.launchScreen("card_selection");
    }

    @FXML
    void openTricksterSelect(ActionEvent event) {
        cardGenre = "Trickster";
        Launcher.launchScreen("card_selection");
    }

    private void setDeckPercentage() {
        int deckSize = CardSelectionController.getDeckSize();
        double percentage = (double) deckSize / CardSelectionController.MAX_DECK_SIZE;
        progressBar.setProgress(percentage);
        deckFull = percentage == 1.0;
        if (deckFull) {
            deckLabel.setText("Deck Valid");
        }
    }

    private void setDetectionLabel(boolean detected, Label label) {
        String message = detected ? "✅" : "Not Found";
        label.setText(message);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        setDeckPercentage();
        setDetectionLabel(castleChosen, castleLabel);
        System.out.println(castleChosen);
        setDetectionLabel(mageChosen, mageLabel);
        setDetectionLabel(tricksterChosen, tricksterLabel);
        setDetectionLabel(decreeChosen,decreeLabel);
        setDetectionLabel(soldierChosen, soldierLabel);
        deckCounter.setText("Deck size at " + (CardSelectionController.getDeckSize()) + "/51");
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'Untitled'.";
        assert castleLabel != null : "fx:id=\"castleLabel\" was not injected: check your FXML file 'Untitled'.";
        assert decreeLabel != null : "fx:id=\"decreeLabel\" was not injected: check your FXML file 'Untitled'.";
        assert soldierLabel != null : "fx:id=\"soldierLabel\" was not injected: check your FXML file 'Untitled'.";
        assert mageLabel != null : "fx:id=\"mageLabel\" was not injected: check your FXML file 'Untitled'.";
        assert tricksterLabel != null : "fx:id=\"tricksterLabel\" was not injected: check your FXML file 'Untitled'.";
        assert deckLabel != null : "fx:id=\"deckLabel\" was not injected: check your FXML file 'Untitled'.";
        assert deckCounter != null : "fx:id=\"deckCounter\" was not injected: check your FXML file 'Untitled'.";

    }
}
