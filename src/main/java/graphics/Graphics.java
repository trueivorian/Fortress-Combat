package graphics;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import gamelogic.Card;
import gamelogic.CardType;
import graphics.exceptions.InvalidCardPosException;
import graphics.utils.CardEvent;
import graphics.utils.CardGraphicsObject;
import graphics.utils.CardGraphicsObjectDictionary;
import graphics.utils.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.PopupWindow;
import javafx.stage.WindowEvent;
import ui.Launcher;
import utils.CardPos;
import utils.exceptions.CardEventException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import networking.client.ClientLogic;
import networking.exceptions.CardLogicException;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;


/**
 * @author Osanne Gbayere
 * The window containing a graphical representation of
 * the game board
 *
 * Note: Lambda's have been used in this project in order to edit JFX elements
 * in a seperate thread after initialization
 */
public class Graphics extends Application implements Runnable {

    // Primitive Constants
    private static final float onBoardSF = 0.7f;                          // Scale factor applied to cards on the board
    private static final float handSF = 0.5f;                             // Scale factor applied to cards in your hand

    // Constants
    private final Color canvasColour = Color.rgb(50,50,50);
    private final Color backgroundColour = Color.rgb(192, 192, 192);
    private final Color canvasFontColour = Color.WHITE;

    private boolean awaitingSecondClick;                    // A boolean variable set to true after the first click
    private boolean isRunning;                              // A boolean variable set to true when the thread is running
    private boolean myHealthBarIsInitialised;
    private boolean theirHealthBarIsInitialised;

    private int myMaxCastleHealth;
    private int theirMaxCastleHealth;

    private ArrayList<MenuItem> cardActionMenu;             // An array list of menu items added to the context menu dynamically when a card is clicked on
    private ArrayList<CardPos> validBoardRegionsBuffer;     // A temporary store of the valid board regions for a card to target

    private Button button;                                  // The button that ends a player's turn

    private CardPos buffer;                                 // A buffer holding the CardPos of the last card space clicked on
    private CardPos secondClickLocation;                    // Holds the CardPos of the next card space clicked on

    private ContextMenu cm;                                 // The menu that appears when a player right clicks on a card

    private GraphicsContext bgDisplay;                     // The scene background
    private GraphicsContext cardDisplay;                    // The object onto which the card image is drawn on the LHS of the screen

    private Group myCards;                                  // The Group of Node objects referring to your own cards
    private Group theirCards;                               // The Group of Node objects referring to the other player's cards
    private Group cardSpaces;                               // The Group of Node objects referring to transparent boxes that read target mouse selection card positions
    private Group root;                                     // The Group of all Node objects in the game
    private Group myCastleHealthBarGroup;
    private Group theirCastleHealthBarGroup;

    private String eventMode;                               // This is set in order to dictate which UIController method is called upon
    private Scene gameScene;                                // The Container for all JFX Group objects in the game

    private Text myTurn;
    private Text myUserName;
    private Text theirUserName;
    private Text myCastleHealth;
    private Text theirCastleHealth;

    private ProgressBar myCastleHealthBar;
    private ProgressBar theirCastleHealthBar;

    /**
     * Constructor for the Graphics class that initialises all non JFX related private fields
     * @param stage The container for the JFX application
     */
    public Graphics(Stage stage) {
        eventMode = "";

        awaitingSecondClick = false;
        isRunning = true;
        myHealthBarIsInitialised = false;
        theirHealthBarIsInitialised = false;

        start(stage);
    }

    /**
     * Used to instantiate all JFX elements
     * @param stage
     */
    @Override
    public void start(Stage stage) {

        button = new Button("End Turn");

        // Create board texture
        PhongMaterial boardMaterial = new PhongMaterial();
        Image boardImage = getImage("board.png");
        boardMaterial.setDiffuseColor(Color.WHITE);
        boardMaterial.setDiffuseMap(boardImage);

        // Create the player's board
        float boardScaleFactor = 1.25f;
        Box myBoard = new Box(1920 * boardScaleFactor, 540 * boardScaleFactor, 0);
        myBoard.setTranslateX(960);
        myBoard.setTranslateY(1080);
        myBoard.setTranslateZ(0 + 1080 + 270);
        myBoard.setRotationAxis(Rotate.X_AXIS);
        myBoard.setRotate(-90);
        myBoard.setMaterial(boardMaterial);

        // Create the opponent's board
        Box theirBoard = new Box(1920 * boardScaleFactor, 540 * boardScaleFactor, 0);
        theirBoard.setTranslateX(880);
        theirBoard.setTranslateY(1080);
        theirBoard.setTranslateZ(0);
        theirBoard.setRotationAxis(Rotate.X_AXIS);
        theirBoard.setRotate(-90);
        theirBoard.setTranslateZ(750 * boardScaleFactor + 1080 + 270);
        theirBoard.setRotationAxis(Rotate.X_AXIS);
        theirBoard.setRotate(90);
        theirBoard.setMaterial(boardMaterial);

        // Group boards
        Group boards = new Group();
        boards.getChildren().add(myBoard);
        boards.getChildren().add(theirBoard);

        // Create a light for the board
        PointLight light = new PointLight();
        light.setTranslateX(960);
        light.setTranslateY(0);
        light.setTranslateZ(540 + 1080);
        light.setRotationAxis(Rotate.X_AXIS);
        light.setRotate(-90);

        // Create a Camera to view the Shapes
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(-450 + 90);
        camera.setTranslateY(0 - 200);//*- 200);
        camera.setTranslateZ(1080-500 );//*- 400);
        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(-40);

        // Instantiate card groups
        myCards = new Group();
        theirCards = new Group();
        cardSpaces = getCardSpaces();

        // Instantiate the left hand card display canvas
        Canvas canvas = new Canvas(540, 1080);
        canvas.setTranslateX(-220 - 75);
        canvas.setTranslateY(-500);
        canvas.setRotationAxis(Rotate.X_AXIS);
        canvas.setRotate(-40);
        cardDisplay = canvas.getGraphicsContext2D();

        // Instantiate the left hand card display canvas
        Canvas background = new Canvas(3540, 1080);
        background.setTranslateX(-220 - 75+50+580);
        background.setTranslateY(500);
        background.setTranslateZ(3100);

        bgDisplay = background.getGraphicsContext2D();
        bgDisplay.setFill(backgroundColour);
        bgDisplay.fillRect(0, 0, 1000, 1080);

        // Sets the background colour and font colour of the description component of the cardDisplay
        cardDisplay.setFill(canvasColour);
        cardDisplay.fillRect(0, 0, 540, 1080);
        cardDisplay.setFont(new Font("Goudy Old Style", 18));
        cardDisplay.setFill(canvasFontColour);

        // Ends the user's turn when the button is clicked
        button.setOnAction(value -> {
            UIController.endTurn();
        });


        // Instantiates the Group with all elements
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        button.setRotationAxis(Rotate.X_AXIS); // orients the button to be in line with other scene elements
        button.setRotate(-40);
        button.setTranslateX(((125 + 155 + 780)+(230))/2);
        button.setTranslateY(125 + 135 + 15);

        myTurn = new Text();
        myUserName = new Text();
        theirUserName = new Text();
        myCastleHealth = new Text();
        theirCastleHealth = new Text();

        myCastleHealthBarGroup = new Group();
        myCastleHealthBar = new ProgressBar();
        myCastleHealthBarGroup.getChildren().add(new Group(myCastleHealthBar));

        theirCastleHealthBarGroup = new Group();
        theirCastleHealthBar = new ProgressBar();
        theirCastleHealthBarGroup.getChildren().add(new Group(theirCastleHealthBar));

        myTurn.setFont(new Font("Goudy Old Style", 20));
        myTurn.setRotationAxis(Rotate.X_AXIS);
        myTurn.setRotate(-40);
        myTurn.setTranslateX((((125 + 155 + 780)+(230))/2) - 95);
        myTurn.setTranslateY(125 + 155 + -50);

        myUserName.setFont(new Font("Goudy Old Style", 20));
        myUserName.setRotationAxis(Rotate.X_AXIS);
        myUserName.setRotate(-40);
        myUserName.setTranslateX(230);
        myUserName.setTranslateY(125 + 155 + -50);

        theirUserName.setFont(new Font("Goudy Old Style", 20));
        theirUserName.setRotationAxis(Rotate.X_AXIS); // orients the button to be in line with other scene elements
        theirUserName.setRotate(-40);
        theirUserName.setTranslateX(125 + 155 + 780 - 50 - 15);
        theirUserName.setTranslateY(125 + 155 + -50);

        myCastleHealth.setFont(new Font("Goudy Old Style", 18));
        myCastleHealth.setRotationAxis(Rotate.X_AXIS); // orients the button to be in line with other scene elements
        myCastleHealth.setRotate(-40);
        myCastleHealth.setTranslateX(230 + 45);
        myCastleHealth.setTranslateY(125 + 155 + 37 + -50);

        myCastleHealthBar.setRotationAxis(Rotate.X_AXIS);
        myCastleHealthBar.setRotate(-40);
        myCastleHealthBar.setTranslateX(230);
        myCastleHealthBar.setTranslateY(125 + 155 + 30 - 5 + -50);
        myCastleHealthBar.setStyle(
                "-fx-accent: red;"
        );

        theirCastleHealth.setFont(new Font("Goudy Old Style", 18));
        theirCastleHealth.setRotationAxis(Rotate.X_AXIS); // orients the button to be in line with other scene elements
        theirCastleHealth.setRotate(-40);
        theirCastleHealth.setTranslateX(125 + 155 + 780 + 45 - 50 - 15);
        theirCastleHealth.setTranslateY(125 + 155 + 37 + -50);

        theirCastleHealthBar.setRotationAxis(Rotate.X_AXIS);
        theirCastleHealthBar.setRotate(-40);
        theirCastleHealthBar.setTranslateX(125 + 155 + 780 - 50 - 15);
        theirCastleHealthBar.setTranslateY(125 + 155 + 30 - 5 + -50);
        theirCastleHealthBar.setStyle(
                "-fx-accent: red;"
        );

        root = new Group();

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle(
                "-fx-background-image: url(" + getImagePath("background.png").replace("\\", "/") + ");\n"
        );

        //root.getChildren().add(background);
        root.getChildren().add(boards);
        root.getChildren().add(button);
        root.getChildren().add(canvas);
        root.getChildren().add(cardSpaces);
        root.getChildren().add(light);
        root.getChildren().add(myTurn);
        root.getChildren().add(myCards);
        root.getChildren().add(theirCards);
        root.getChildren().add(myUserName);
        root.getChildren().add(theirUserName);
        root.getChildren().add(myCastleHealth);
        root.getChildren().add(theirCastleHealth);
        root.getChildren().add(myCastleHealthBarGroup);
        root.getChildren().add(theirCastleHealthBar);


        // Creates a 3D Scene with depth buffer enabled
        gameScene = new Scene(root, width, height, true);

        // Set stage dimensions
        stage.setMaxHeight(height);
        stage.setMaxWidth(width);

        // Add the Camera to the Scene
        gameScene.setCamera(camera);

        // Adds the Scene to the Stage
        stage.setScene(gameScene);

        // Maximises the Screen
        //stage.setMaximized(true);

        // Sets the Title of the Stage
        stage.setTitle("Fortress Combat");

        cm = new ContextMenu(); // OnClick menus are attached to this
        cm.hide(); // Hides the context menu

        cardActionMenu = new ArrayList<MenuItem>(); // These are menu items for the OnClick menu

        // Display the Stage
        stage.show();
    }

    /**
     * @param imgName The name of the image attached to the file path
     * @return Returns the path to an image
     */
    private String getImagePath(String imgName) {
        return getClass().getResource("/images/textures/" + imgName).toExternalForm();
    }

    /**
     * @param text The text to be formatted
     * @return Adds a newline to text maintaining a width of 48 characters
     */
    private String formatText(String text) {
        String textBuffer = text;
        String outText = "";

        while (!(textBuffer.length() <= 48)) {
            outText += textBuffer.substring(0, 48) + "\n";
            textBuffer = textBuffer.substring(48);
        }

        outText += textBuffer;

        return outText;
    }

    private void play(CardPos p1, CardPos p2){
        HashMap<CardPos, CardGraphicsObject> cardMap = CardGraphicsObjectDictionary.getAll();
        if((!cardMap.keySet().contains(p2))||(cardMap.get(p2) == null)){
            try {
                UIController.play(p1, p2);
            } catch (CardLogicException e) {
                e.printStackTrace();
            } catch (InvalidCardPosException e) {
                e.printStackTrace();
            }
        }
    }

    private void play(CardPos p1, CardPos p2, CardPos alt){
        HashMap<CardPos, CardGraphicsObject> cardMap = CardGraphicsObjectDictionary.getAll();
        if((!cardMap.keySet().contains(p2))||(cardMap.get(p2) == null)){
            try {
                UIController.play(p1, p2);
            } catch (CardLogicException e) {
                e.printStackTrace();
            } catch (InvalidCardPosException e) {
                e.printStackTrace();
            }
        } else if((!cardMap.keySet().contains(alt))||(cardMap.get(alt) == null)){
            try {
                UIController.play(p1, alt);
            } catch (CardLogicException e) {
                e.printStackTrace();
            } catch (InvalidCardPosException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Spawns cards of a particular shape and orientation depending on its position on the board
     * @param card The 3D Card container
     * @param cardDisplayImg The card's image texture
     * @param cardBackImg The card's back texture
     * @param cardEventHandler  The card's event handler
     * @param cardPos The position of the card in the CardPos space
     */
    public static void spawnCards(Group card, Image cardDisplayImg, Image cardBackImg, EventHandler cardEventHandler, CardPos cardPos){
        if (CardPos.isInMyHand(cardPos)) { // Renders a "HandCard" if a card is in the player's hand
            Platform.runLater(
                    () -> {
                        card.getChildren().clear();
                        card.getChildren().add(renderHandCard(cardDisplayImg, cardEventHandler,
                                CardPos.calcPosition(cardPos)));
                    }
            );
        } else if (CardPos.isInTheirHand(cardPos)) { // Renders a "HandCard" flipped around if a card is in the other player's hand
            Platform.runLater(
                    () -> {
                        card.getChildren().clear();
                        card.getChildren().add(renderHandCard(cardBackImg, cardEventHandler,
                                CardPos.calcPosition(cardPos)));
                    }
            );

        } else if(CardPos.isTheirTrickster(cardPos)){
            // Adds the card to a Group defining a single card
            Platform.runLater(
                    () -> {
                        card.getChildren().clear();
                        card.getChildren().add(renderBoardCard(cardDisplayImg, cardBackImg, cardEventHandler,
                                CardPos.calcPosition(cardPos)));
                    }
            );
        } else if (CardPos.isOnTheBoard(cardPos)||CardPos.isDiscarded(cardPos)) { // Renders a "BoardCard" (i.e. a 2D textured Box) if a card is on the board
            // Adds the card to a Group defining a single card
            Platform.runLater(
                    () -> {
                        card.getChildren().clear();
                        card.getChildren().add(renderBoardCard(cardBackImg, cardDisplayImg, cardEventHandler,
                                CardPos.calcPosition(cardPos)));
                    }
            );
        } /*else if(cardPos.isInTheDeck(cardPos)){
            // Adds the card to a Group defining a single card
            Platform.runLater(
                    () -> {
                        card.getChildren().clear();
                        card.getChildren().add(renderBoardCard(cardDisplayImg, cardBackImg, cardEventHandler,
                                CardPos.calcPosition(cardPos)));
                    }
            );
        }*/
    }

    /**
     * Renders a card on the screen drawn using a Canvas (unaffected by lighting)
     * @param cardDisplayImg The image to be drawn on the card
     * @param cardEventHandler The event handler for click events
     * @param cardPos3D The position of the card in the 3D coordinate space
     * @return The canvas with the card image drawn on
     */
    private static Canvas renderHandCard(Image cardDisplayImg, EventHandler cardEventHandler, Point3D cardPos3D){
        // Creates a canvas the size of a Card and draws the image on to it
        Canvas handItem = new Canvas(270 * handSF, 320 * handSF);
        handItem.setTranslateX(cardPos3D.getX());
        handItem.setTranslateY(cardPos3D.getY());
        handItem.setTranslateZ(cardPos3D.getZ());
        handItem.setRotationAxis(Rotate.X_AXIS);
        handItem.setRotate(-40);
        handItem.addEventFilter(MouseEvent.MOUSE_CLICKED, cardEventHandler);
        GraphicsContext gcHandItem = handItem.getGraphicsContext2D();
        gcHandItem.drawImage(cardDisplayImg, 0, 0, 270 * handSF, 320 * handSF);

        return handItem;
    }

    /**
     * Renders a card on the board as a textured Box
     * @param cardDisplayImg The image to be drawn on the card
     * @param cardBackImg The event handler for click events
     * @param cardEventHandler The position of the card in the CardPos space
     * @param cardPos3D The position of the card in the 3D coordinate space
     * @return The Group with the back and front of the card attached
     */
    private static Group renderBoardCard(Image cardDisplayImg, Image cardBackImg, EventHandler cardEventHandler, Point3D cardPos3D){
        Group cardGroup = new Group();

        // Creates the 3D card and moves it to the appropriate location
        Box cardBox = new Box(270 * onBoardSF, 0.1, 320 * onBoardSF);
        cardBox.setTranslateX(cardPos3D.getX());
        cardBox.setTranslateY(cardPos3D.getY());
        cardBox.setTranslateZ(cardPos3D.getZ());
        cardBox.setRotationAxis(Rotate.Y_AXIS);
        cardBox.setRotate(180);

        // Creates the card material and adds it to the box
        PhongMaterial cardMaterial = new PhongMaterial();
        cardMaterial.setDiffuseColor(Color.WHITE);
        cardMaterial.setDiffuseMap(cardDisplayImg);
        cardBox.setMaterial(cardMaterial);

        // Creates the 3D back of the card and moves it above the card object
        Box cardBack = new Box(270 * onBoardSF, 0.1, 320 * onBoardSF);
        cardBack.setTranslateX(cardPos3D.getX());
        cardBack.setTranslateY(cardPos3D.getY() - 0.9);
        cardBack.setTranslateZ(cardPos3D.getZ());

        // Creates the card back texture and adds it to the cardBack box
        PhongMaterial cardBackMaterial = new PhongMaterial();
        cardBackMaterial.setDiffuseColor(Color.WHITE);
        cardBackMaterial.setDiffuseMap(cardBackImg);
        cardBack.setMaterial(cardBackMaterial);

        // Adds a mouse click event filter to the cardGroup
        cardGroup.addEventFilter(MouseEvent.MOUSE_CLICKED, cardEventHandler);

        // Adds the front and back of the card to the group in a separate thread
        Platform.runLater(
                () -> {
                    cardGroup.getChildren().add(cardBox);
                    cardGroup.getChildren().add(cardBack);
                }
        );

        return cardGroup;
    }

    /**
     * Creates transparent boxes which the user can select in order to obtain the second CardPos during a target
     * selection (e.g *Click* > Select attack > *Second Click on cardSpace*)
     * @return The group of transparent boxes at each position on the board
     */
    private Group getCardSpaces() {
        // Instantiates the group of card spaces to be returned
        Group cardGroup = new Group();

        // Instantiates and adds to the arrayList of all card positions on the board
        ArrayList<CardPos> boardPositions = new ArrayList<CardPos>();
        boardPositions.add(CardPos.MY_MAGE_1);
        boardPositions.add(CardPos.MY_SOLDIER_1);
        boardPositions.add(CardPos.MY_SOLDIER_2);
        boardPositions.add(CardPos.MY_MAGE_2);
        boardPositions.add(CardPos.MY_TRICKSTER_1);
        boardPositions.add(CardPos.MY_CASTLE);
        boardPositions.add(CardPos.MY_TRICKSTER_2);
        boardPositions.add(CardPos.THEIR_DECREE);
        boardPositions.add(CardPos.THEIR_MAGE_1);
        boardPositions.add(CardPos.THEIR_SOLDIER_1);
        boardPositions.add(CardPos.THEIR_SOLDIER_2);
        boardPositions.add(CardPos.THEIR_MAGE_2);
        boardPositions.add(CardPos.THEIR_TRICKSTER_1);
        boardPositions.add(CardPos.THEIR_CASTLE);
        boardPositions.add(CardPos.THEIR_TRICKSTER_2);

        // Iterates through all board positions creating a transparent box that, when clicked on, executes a particular UIController event
        for (int i = 0; i < boardPositions.size(); i++) {
            // Instantiates and orients the box used as the card space
            Box card = new Box(272 * onBoardSF, 0.9, 320 * onBoardSF);
            PhongMaterial mat = new PhongMaterial();
            mat.setDiffuseColor(Color.TRANSPARENT);
            card.setMaterial(mat);
            Point3D pos = CardPos.calcPosition(boardPositions.get(i));
            card.setTranslateX(pos.getX());
            card.setTranslateY(pos.getY());
            card.setTranslateZ(pos.getZ());
            card.setRotationAxis(Rotate.Y_AXIS);
            card.setRotate(180);
            int index = i;

            // Adds the event filter that handles the different circumstances that the card space can be clicked on
            // (e.g. when the event mode is "playing")
            card.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    secondClickLocation = boardPositions.get(index);

                    if (awaitingSecondClick) {
                        System.out.println("eventmode: " + eventMode);
                        // handling card space stuff
                    }
                }
            });
            cardGroup.getChildren().add(card);
        }

        return cardGroup;
    }

    /**
     * @param imgName The image name
     * @return Returns the image associated to an image path
     */
    private Image getImage(String imgName) {
        return new Image(getImagePath(imgName));
    }

    /**
     * @param e The card event to be executed
     * @throws CardEventException
     */
    private void executeCardEvent(CardEvent e) throws CardEventException {
        // Gets the eventType associated with the cardEvent
        EventType eventType = e.getEventType();

        if(e.getCard()!=null) { // Executes events acting on card objects
            // Obtain the relevant card images
            Image cardDisplayImg = getImage(e.getCard().getImgName());
            Image cardBackImg = getImage("Back.png");

            EventHandler cardEventHandler = generateCardEventHandler(e.getCard(), cardDisplayImg, cardBackImg);

            switch (eventType) {
                case SPAWN_CARD:
                    Group card = new Group();
                    CardPos sp = e.getStartPos();
                    CardGraphicsObject cgo = new CardGraphicsObject(e.getCard(),card);

                    cgo.setCardEventHandler(generateCardEventHandler(e.getCard(), new Image(e.getCard().getImgPath()), new Image(e.getCard().getImgBackPath())));

                    // Adds the card to the hash map with all the card objects
                    CardGraphicsObjectDictionary.setCardGraphicsObject(sp, cgo);

                    if (sp.compareTo(CardPos.MY_DECK_50) <= 0) {
                        Platform.runLater(
                                () -> {
                                    myCards.getChildren().add(card);
                                }
                        );
                    } else {
                        Platform.runLater(
                                () -> {
                                    theirCards.getChildren().add(card);
                                }
                        );
                    }

                    spawnCards(card, cardDisplayImg, cardBackImg, cardEventHandler, sp);

                    break;
            }
        } else{ // Executes events that don't use card objects (i.e. alert dialogues)
            switch(eventType){

                case OPTION_MSG:

                    Platform.runLater(
                            () -> {
                                Alert alert = new Alert(Alert.AlertType.NONE);

                                alert.setContentText(e.getMsg());

                                ButtonType buttonTypeOne = new ButtonType("Yes");
                                ButtonType buttonTypeTwo = new ButtonType("No");

                                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == buttonTypeOne) {
                                    UIController.option(true);
                                } else if (result.get() == buttonTypeTwo) {
                                    UIController.option(false);
                                }

                                // Prevents the user from being able to close the new window
                                alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                                    @Override
                                    public void handle(DialogEvent event) {
                                        event.consume();
                                    }
                                });
                            }
                    );

                    break;

                case MSG:
                    Platform.runLater(
                            () -> {
                                Alert alert = new Alert(Alert.AlertType.NONE);

                                alert.setContentText(e.getMsg());

                            }
                    );
                    break;

                case TARGET_CARD:
                    if (awaitingSecondClick) {
                        awaitingSecondClick = false;

                    } else {
                        // Display a card selection prompt
                        cardDisplay.setFill(canvasColour);
                        cardDisplay.fillRect(0, 0, 540, 1080);
                        cardDisplay.setFill(canvasFontColour);
                        cardDisplay.fillText(formatText("Select a card to target"), 220, 390, 272);

                        buffer = e.getStartPos();
                        validBoardRegionsBuffer = e.getValidBoardRegions();
                        eventMode = "targeting";
                        awaitingSecondClick = true;
                    }
                    break;

                case MOVE_CARD:
                    System.out.println("Invalid move command");
                    break;

                case MOVE_MULTIPLE:
                    System.out.println("Received Move Multiple");
                    System.out.println(e.getMoveList().size());
                    break;

                case GET_DECK_CARD:
                    System.out.println("Received Get Deck Card");
                    Platform.runLater(
                            () -> {
                                ArrayList<String> choices = new ArrayList<String>();
                                ArrayList<CardPos> posList = new ArrayList<CardPos>();

                                CardPos pos = CardPos.MY_DECK_1;
                                for(int i=0; i<50; i++){
                                    try {
                                        if(CardGraphicsObjectDictionary.getAll().containsKey(pos)) {
                                            Card c = CardGraphicsObjectDictionary.getCardGraphicsObject(pos).getCardInstance();

                                            if(e.getCardTypeList().contains(c.getType())) {
                                                posList.add(pos);
                                                choices.add(c.getName());
                                            }
                                        }
                                    } catch (InvalidCardPosException e1) {
                                        e1.printStackTrace();
                                    }
                                    pos = pos.next();
                                }
                                ChoiceDialog<String> popup = new ChoiceDialog<String>(choices.get(0), choices) {
                                };

                                popup.setTitle("Get a card from the deck");

                                Optional<String> result = popup.showAndWait();
                                result.ifPresent(
                                        cardName -> {
                                            System.out.println("getDeckCard is called on " + posList.get(choices.indexOf(cardName)));
                                            UIController.getDeckCard(posList.get(choices.indexOf(cardName)));
                                        }
                                );

                                popup.setOnCloseRequest(new EventHandler<DialogEvent>() {
                                    @Override
                                    public void handle(DialogEvent event) {
                                        event.consume();
                                    }
                                });
                            }
                    );
                    break;

                case TURN_START:
                    System.out.println("Received Turn Start");
                    button.setVisible(e.getFlag());
                    if(e.getFlag()){
                        myTurn.setText("");
                    } else{
                        myTurn.setText("It's " + theirUserName.getText() + "'s Turn!");
                    }

                    break;

                case MY_USERNAME:
                    System.out.println("Received my username " + e.getMsg());
                    myUserName.setText(e.getMsg());
                    break;

                case MY_CASTLE_HEALTH:
                    System.out.println("Received Castle Health " + e.getValue());
                    myCastleHealth.setText(String.valueOf(e.getValue()));
                    System.out.println(myCastleHealth.getText());

                    if(!myHealthBarIsInitialised) {
                        myMaxCastleHealth = e.getValue();
                        myHealthBarIsInitialised = true;
                        Platform.runLater(
                                ()->{
                                    myCastleHealthBar.setProgress(e.getValue()/myMaxCastleHealth);
                                }
                        );
                    } else{
                        Platform.runLater(
                                ()->{
                                    myCastleHealthBar.setProgress(e.getValue()/myMaxCastleHealth);
                                }
                        );
                    }

                    break;

                case THEIR_USERNAME:
                    System.out.println("Received username " + e.getMsg());
                    theirUserName.setText(e.getMsg());
                    break;

                case THEIR_CASTLE_HEALTH:
                    System.out.println("Received Castle Health " + e.getValue());
                    theirCastleHealth.setText(String.valueOf(e.getValue()));
                    System.out.println(theirCastleHealth.getText());

                    if(!theirHealthBarIsInitialised) {
                        theirMaxCastleHealth = e.getValue();
                        myHealthBarIsInitialised = true;
                        Platform.runLater(
                                ()->{
                                    theirCastleHealthBar.setProgress(e.getValue()/theirMaxCastleHealth);
                                }
                        );
                    } else{
                        Platform.runLater(
                                ()->{
                                    theirCastleHealthBar.setProgress(e.getValue()/theirMaxCastleHealth);
                                }
                        );
                    }
                    break;
            }
        }

    }

    public EventHandler generateCardEventHandler(Card card, Image cardDisplayImg, Image cardBackImg){
        return  new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Gets the card position of the selected card
                CardPos currentCardPos = null;
                try {
                    currentCardPos = CardGraphicsObjectDictionary.getCardPosOfUID(card.getUid());
                } catch (InvalidCardPosException e) {
                    e.printStackTrace();
                }

                if((CardPos.isInMyHand(currentCardPos)||CardPos.isOnTheBoard(currentCardPos))&&(!CardPos.isTheirTrickster(currentCardPos))) {
                    // When a card is clicked on, the card Display image is drawn on to the canvas
                    cardDisplay.setFill(canvasColour);
                    cardDisplay.fillRect(0, 0, 540, 1080);
                    cardDisplay.drawImage(cardDisplayImg, 180, 125, 272, 320);
                    cardDisplay.setFill(canvasFontColour);
                    cardDisplay.fillText(formatText(card.getDescription()), 180, 475, 272);
                }

                // Refreshes the on click menu
                cm.getItems().clear();
                cardActionMenu.clear();

                if (CardPos.isOnTheBoard(currentCardPos)) {
                    if (awaitingSecondClick) {
                        secondClickLocation = currentCardPos;

                        if (eventMode.equals("targeting")) {
                            if (validBoardRegionsBuffer.contains(secondClickLocation.name())) {
                                UIController.target(buffer, secondClickLocation);
                                awaitingSecondClick = false;
                            }
                        } else if (eventMode.equals("attacking")) {
                            try {
                                System.out.println("Card at " + buffer + " is attacking the card at " + secondClickLocation);
                                UIController.attack(buffer, secondClickLocation);
                                awaitingSecondClick = false;
                            } catch (CardLogicException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }

                    if ((currentCardPos == CardPos.MY_MAGE_1) || (currentCardPos == CardPos.MY_MAGE_2)) {
                        MenuItem m = new MenuItem("Level Up");
                        m.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                if (awaitingSecondClick) {
                                    awaitingSecondClick = false;
                                } else {
                                    try {
                                        try {
                                            UIController.levelMage(CardGraphicsObjectDictionary.getCardPosOfUID(card.getUid()));
                                        } catch (InvalidCardPosException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (CardLogicException exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        });
                        cardActionMenu.add(m);
                    }

                    if ((currentCardPos.compareTo(CardPos.MY_MAGE_2) <= 0) && (currentCardPos.compareTo(CardPos.MY_MAGE_1) >= 0)) {
                        MenuItem m = new MenuItem("Attack");
                        m.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                if (awaitingSecondClick) {
                                    awaitingSecondClick = false;

                                } else {
                                    try {
                                        buffer = CardGraphicsObjectDictionary.getCardPosOfUID(card.getUid());
                                        System.out.println("Clicked Attack at " + buffer);
                                        eventMode = "attacking";
                                        awaitingSecondClick = true;
                                    } catch (InvalidCardPosException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        });
                        cardActionMenu.add(m);
                    }
                }

                if (currentCardPos.compareTo(CardPos.MY_HAND_10) <= 0) {
                    MenuItem m = new MenuItem("Play");
                    m.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            CardType cardType = card.getType();

                            Launcher.playTestSFX();

                            switch(cardType) {
                                case MAGE:
                                    try {
                                        play(CardGraphicsObjectDictionary.getCardPosOfUID(card.getUid()), CardPos.MY_MAGE_1, CardPos.MY_MAGE_2);
                                    } catch (InvalidCardPosException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case CASTLE:
                                    try {
                                        play(CardGraphicsObjectDictionary.getCardPosOfUID(card.getUid()), CardPos.MY_CASTLE);
                                    } catch (InvalidCardPosException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case DECREE:
                                    try {
                                        play(CardGraphicsObjectDictionary.getCardPosOfUID(card.getUid()), CardPos.MY_DECREE);
                                    } catch (InvalidCardPosException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case SOLDIER:
                                    try {
                                        play(CardGraphicsObjectDictionary.getCardPosOfUID(card.getUid()), CardPos.MY_SOLDIER_1, CardPos.MY_SOLDIER_2);
                                    } catch (InvalidCardPosException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case TRICKSTER:
                                    try {
                                        play(CardGraphicsObjectDictionary.getCardPosOfUID(card.getUid()), CardPos.MY_TRICKSTER_1, CardPos.MY_TRICKSTER_2);
                                    } catch (InvalidCardPosException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }

                        }
                    });
                    cardActionMenu.add(m);
                }

                for (int i = 0; i < cardActionMenu.size(); i++) {
                    cm.getItems().add(cardActionMenu.get(i));
                }

                if (event.getButton() == MouseButton.SECONDARY) {
                    cm.show(root, event.getScreenX(), event.getScreenY());
                } else {
                    cm.hide();
                }
            }
        };
    }
    /**
     * @return Returns the game scene
     */
    public Scene getGameScene() {
        return gameScene;
    }

    @Override
    public void run() {
        while (isRunning) {
            // Thread Operations
            try {
                executeCardEvent(UIController.popEvent());
            } catch (CardEventException e) {
                e.printStackTrace();
            }

        }
    }

}
