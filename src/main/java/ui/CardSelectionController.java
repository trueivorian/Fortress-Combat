/**
 * Sample Skeleton for 'card_selection.fxml' Controller Class
 */

package ui;

import gamelogic.Card;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import utils.Dictionary;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;


public class CardSelectionController {

    @FXML // fx:id="imageHolder"
    private AnchorPane imageHolder; // Value injected by FXMLLoader

    @FXML // fx:id="grid"
    private GridPane grid;

    private int row, column =0;

    private static ObservableList<String> addedCards = FXCollections.observableArrayList();

    private String fileName;

    private static ArrayList<Integer> cardIds = new ArrayList<>();

    @FXML
    private ListView<String> deckList;

    private boolean gridFilled = false;

    final static int MAX_DECK_SIZE = 51;

    @FXML // need to clean this bad boy up
    void addCard(ActionEvent event) {
        if (!gridFilled) {
            addCardType(DeckMenuController.cardGenre);
            gridFilled = true;
        }
        deckList.setItems(addedCards);
    }

    private void addCardType(String filePrefix) {
        File[] cards = collectCards(filePrefix);
        //System.out.println(Arrays.toString(cards));
        for (File cardName : cards) {
            String fileName = cardName.getName();
            populateGridView(fileName);
        }
    }

    private void populateGridView(String fileName) {
        int MAX_CARDS_COLUMNS = 4;
        int MAX_CARDS_ROWS = 5;
        Image cardImage = new Image(getImagePath(fileName));
        ImageView cardViewer = new ImageView();
        cardViewer.setOnMouseClicked((MouseEvent) -> {
            try {
                addCardsToList(fileName);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        });
        cardViewer.setFitHeight(grid.getHeight() / MAX_CARDS_ROWS);
        cardViewer.setFitWidth(grid.getWidth() / MAX_CARDS_COLUMNS);
        cardViewer.setImage(cardImage);
        grid.add(cardViewer,column, row);
        column++;
        if (column== MAX_CARDS_COLUMNS) {
            column=0;
            row++;
        }
    }

    private int reverseEngineerCardId(String fileName) {
        int cardIdOffset = 0;
        //System.out.println("indexing issue" + "   "+ fileName);
        String startOfFileName = fileName.substring(0,4);
        //System.out.println(startOfFileName);
        int cardCount = Integer.parseInt(fileName.substring(fileName.length() - 2));
        //System.out.println("gets before switch case");
        switch (startOfFileName) {
            case "Cast": // I have to use the switch statement on the first 4 letters as they are the only ones that
                // Constant ie. some mages will have "MageAt" or "MageDe" if i use 6 characters
                cardIdOffset = -1;
                DeckMenuController.castleChosen = true;
                break;
            case "Decr":
                cardIdOffset = 5;
                DeckMenuController.decreeChosen = true;
                break;
            case "Deck": // This starts as deck but is actually soldier
                cardIdOffset = 9;
                DeckMenuController.soldierChosen = true;
                break;
            case "Tric":
                cardIdOffset = 27;
                DeckMenuController.tricksterChosen = true;
                break;
            case "Mage":
                cardIdOffset = 41;
                DeckMenuController.mageChosen = true;
                break;
            default:
                //System.out.println("Unrecognised card file name");
                break;
        }
        return cardCount + cardIdOffset;

    }

    private void addCardsToList(String fileName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String cardWithoutFileExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        String fileNameStart = cardWithoutFileExtension.substring(0,6);
        int cardId = reverseEngineerCardId(cardWithoutFileExtension);
        String cardFullName = getFullCardName(cardId);
        boolean currentCardIsDecree = fileNameStart.equals("Decree");
        int decreeCount = currentCardIsDecree ? 1 : 0; // This is 1 if the card is a decree so that you can only have one of each
        boolean currentCardIsCastle = fileNameStart.equals("Castle");
        int deckSize = getDeckSize();
        boolean deckFull = deckSize == MAX_DECK_SIZE;
        if ((Collections.frequency(addedCards, cardFullName) + decreeCount) < 2
                && !(Launcher.castlePicked && currentCardIsCastle) && !(deckFull)) {
            cardIds.add(cardId);
            addedCards.add(cardFullName);
            deckList.setItems(addedCards);
        } else if (Launcher.castlePicked && currentCardIsCastle) {
            Launcher.showAlert("Only one castle is allowed.");
        } else if (deckFull) {
            Launcher.showAlert("Deck Full");
        } else {
            Launcher.showAlert("There can only be a max of one duplicate card in a deck or one of each decree.");
        }
        if (currentCardIsCastle) {
            Launcher.castlePicked = true;
        }
    }

    static int getDeckSize() {
        return cardIds.size();
    }

    static ArrayList<Integer> getCardIds() {
        return cardIds;
    }

    private String getFullCardName(int cardId) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        //System.out.println(cardId);
        Card card = (Card) Dictionary.getEntry(cardId).getConstructor().newInstance();
        //System.out.println(cardFullName);
        return card.getName();
    }

    /**
     * @param cardPrefix The prefix of the filename of the group of cards
     * @return cardsList List of cards to be returned.
     */
    private File[] collectCards(String cardPrefix) {
        File dir = new File("src/main/resources/images/textures/");
        return dir.listFiles((dir1, name) -> name.startsWith(cardPrefix));
    }

    /**
     * @param imgName The name of the image attached to the file path
     * @return Returns the path to an image
     */
    private String getImagePath(String imgName) {
        return getClass().getResource("/images/textures/" + imgName).toExternalForm(); //(filePath + "\\resources\\images\\textures\\" + imgName).replace("\\", File.separator);
    }


    @FXML
    void saveAndQuit(ActionEvent event) {
        Launcher.launchScreen("deck_menu");
        System.out.println(cardIds);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert imageHolder != null : "fx:id=\"imageHolder\" was not injected: check your FXML file 'card_selection.fxml'.";
        assert deckList != null : "fx:id=\"deckList\" was not injected: check your FXML file 'card_selection.fxml'.";
    }
}
