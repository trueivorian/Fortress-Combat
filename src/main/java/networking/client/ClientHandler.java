package networking.client;

import com.google.gson.Gson;

import gamelogic.Card;
import gamelogic.CardType;
import graphics.UIController;
import graphics.exceptions.InvalidCardPosException;
import graphics.utils.CardEvent;
import graphics.utils.CardGraphicsObjectDictionary;
import graphics.utils.EventType;
import utils.CardPos;
import networking.CommandType;
import utils.Dictionary;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sam Gunner on 18/02/2019.
 *
 * Handles incoming messages from the server
 */
public class ClientHandler {
    private ArrayList<User> users;
    private int playerid;
    private boolean mull = false;
    private int currentPlayerTurn;
    public ClientHandler(String username) {
        users = new ArrayList<>();
    }

    /**
     * Handles an incoming commands
     * @param command The command
     * @param data The rest of the data
     */
    public void handle(CommandType command, HashMap data) throws NoSuchMethodException {
        switch(command) {
            case TERMINATE_CONN:
                break;
            case JOIN_GAME:
                break;
            case PLAYER_JOINED: // Another player has joined the game
                if (data.containsKey("USERNAME") && data.containsKey("PLAYER_ID")) { // Check all required data has been specified
                    User user = new User();
                    user.setId(((Double) data.get("PLAYER_ID")).intValue());
                    user.setName((String) data.get("USERNAME"));
                    users.add(user);

                    // inform graphics
                    CardEvent cardEvent = new CardEvent(user.getName(), EventType.THEIR_USERNAME);
                }
                break;

            case JOIN_GAME_RESP: // Server has responded to our JOIN_GAME request
                if (data.containsKey("SUCCESS") && data.containsKey("PLAYER_LIST") && data.containsKey("PLAYER_ID")) {
                    // Handle valid connection formed
                    Map<Object, Object> players = (Map<Object, Object>) data.get("PLAYER_LIST");
                    for (Map.Entry<Object, Object> player : players.entrySet()) {
                        User user = new User();
                        user.setId(Double.parseDouble((String) player.getKey()));
                        user.setName((String) player.getValue());
                        users.add(user);
                    }

                    this.playerid = ((Double) data.get("PLAYER_ID")).intValue();
                    ClientLogic.setPlayerid(this.playerid);
                    break;
                } else if (data.containsKey("SUCCESS") && data.containsKey("MESSAGE")) {
                    // todo: show error message to user via graphics
                }

                // Handle invalid data
                // todo: show 'malformed command' message to user, disconnect from server
                break;

            case GAME_START:
                System.out.println("Recieved Game Start");
                if(data.containsKey("DECK")){
                    Map<String, Double> deck = (Map<String, Double>) data.get("DECK");
                    for (Map.Entry<String, Double> entry : deck.entrySet()) {
                        int cardID = entry.getValue().intValue();
                        try {
                            Card card = (Card) Dictionary.getEntry(cardID).getConstructor().newInstance();
                            CardPos cardPos = CardPos.valueOf(entry.getKey());
                            CardEvent cardEvent = new CardEvent(card, cardPos); // spawning card event
                            UIController.pushEvent(cardEvent);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    System.out.println("doesnt contain deck??????????\n");
                }
                if(data.containsKey("MY_NAME") && data.containsKey("THEIR_NAME")){
                    String myName = (String)data.get("MY_NAME");
                    String theirName = (String)data.get("THEIR_NAME");
                    System.out.println("USername1 : " + myName);
                    System.out.println("USername2 : " + theirName);
                    CardEvent ce = new CardEvent(myName,EventType.MY_USERNAME);
                    CardEvent ce2 = new CardEvent(theirName,EventType.THEIR_USERNAME);
                    UIController.pushEvent(ce);
                    UIController.pushEvent(ce2);

                }
                else{
                    System.out.println("NO NAMES");
                }

                break;

            case DRAW_CARD_RESP: // A card is being moved from deck -> hand
                if (data.containsKey("DRAWN_CARDS")) {
                    if (data.containsKey("CASTLE")) {
                        ArrayList<Map<Object, Object>> drawnCards = (ArrayList<Map<Object, Object>>) data.get("DRAWN_CARDS");
                        Integer castleid = ((Double) data.get("CASTLE_ID")).intValue();
                        try {
                            Card castleObj = (Card) Dictionary.getEntry(castleid).getConstructor().newInstance();
                            int userid;
                            if (data.containsKey("USER_ID")) {
                                userid = ((Double) data.get("USER_ID")).intValue();
                            } else {
                                userid = playerid;
                            }

                            CardPos cardPos;
                            if (userid == playerid)
                                cardPos = CardPos.MY_CASTLE; //todo: change
                            else
                                cardPos = CardPos.THEIR_CASTLE;
                            CardEvent ce = new CardEvent(castleObj, cardPos);
                            UIController.pushEvent(ce);
                            for (int i = 0; i < drawnCards.size(); i++) {
                                Card cardObj = (Card) Dictionary.getEntry((Integer) drawnCards.get(i).get("card")).getConstructor().newInstance();
                                cardPos = CardPos.valueOf((String) drawnCards.get(i).get("cardPos"));
                                CardEvent cardEvent = new CardEvent(cardObj, cardPos);
                                UIController.pushEvent(cardEvent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (data.containsKey("CLEAR_FLAG")) {
                            // TODO: Clear the hand
                        }
                        ArrayList<Map<Object, Object>> drawnCards = (ArrayList<Map<Object, Object>>) data.get("DRAWN_CARDS");
                        Gson gson = new Gson();
                        for (int i = 0; i < drawnCards.size(); i++) {
                            Map<Object, Object> drawnCard = drawnCards.get(i);
                            CardPos cardPos = CardPos.valueOf((String) drawnCard.get("cardPos"));
                            System.out.println("i: " + i);
                            int cardID = ((Double) drawnCard.get("card")).intValue();
                            try {
                                Card cardObj = (Card) Dictionary.getEntry(cardID).getConstructor().newInstance(); // Try creating a new instance and adding to the graphics queue
                                CardEvent cardEvent = new CardEvent(cardObj, cardPos);
                                UIController.pushEvent(cardEvent);
                            } catch (NullPointerException e) {
                                System.out.println("null: " + cardID);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        HashMap<Object, Object> toSend = new HashMap<>();
                        if (!mull) {
                            toSend.put("COMMAND", CommandType.GAME_START_ACCEPT);
                            mull = true;
                        }
                        GameClient.sendMessage(toSend);
                    }
                }
                break;

            case TURN_START: // A user has started their turn
                if (data.containsKey("PLAYER_ID")) { // Check required params are set
                    int userid = ((Double) data.get("PLAYER_ID")).intValue();
                    ClientLogic.setCurrentPlayer(userid);
                    if (userid == playerid) {
                        CardEvent ce = new CardEvent(true, EventType.TURN_START);
                        UIController.pushEvent(ce);
                    } else {
                        CardEvent ce = new CardEvent(false, EventType.TURN_START);
                        UIController.pushEvent(ce);
                    }
                }
                break;
            case CASTLE_DAMAGE:
                if(data.containsKey("MY_HEALTH")){
                    Double health = (Double)data.get("MY_HEALTH");
                    int h = health.intValue();
                    CardEvent ce1 = new CardEvent(h,EventType.MY_CASTLE_HEALTH);
                    UIController.pushEvent(ce1);
                }
                else{
                    System.out.println("NO DATA FOR MY CASTLE" );
                }
                if(data.containsKey("THEIR_HEALTH")){
                    Double health = (Double)data.get("THEIR_HEALTH");
                    int h = health.intValue();
                    CardEvent ce1 = new CardEvent(h,EventType.THEIR_CASTLE_HEALTH);
                    UIController.pushEvent(ce1);
                }
                else{
                    System.out.println("NO DATA FOR THEIR CASTLE" );
                }
                break;
            case DIAG_OFFER: // Offer the user a 'yes' or 'no' choice
                if (data.containsKey("MSG")) {
                    String message = (String) data.get("MSG");
                    CardEvent cardEvent = new CardEvent(message, EventType.OPTION_MSG);
                    UIController.pushEvent(cardEvent);
                }
                break;
            case RETURN_CARD:
                break;
            case PLAY_CARD_RESP: // A card is being moved from hand -> board
                if (data.containsKey("START_POS") && data.containsKey("END_POS") && data.containsKey("CARD_ID") && data.containsKey("PLAYER_ID")) {
                    CardPos sPos = CardPos.valueOf((String) data.get("START_POS"));
                    CardPos ePos = CardPos.valueOf((String) data.get("END_POS"));
                    int playerid = ((Double) data.get("PLAYER_ID")).intValue();
                    int cardid = ((Double) data.get("CARD_ID")).intValue();

                    try {
                        Card card = (Card) Dictionary.getEntry(cardid).getConstructor().newInstance();
                        CardEvent event = new CardEvent(card, sPos, ePos);
                        UIController.pushEvent(event); // Push to ui controller
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case TARGET_CARD: // Sent when the client needs to pick some location to target
                if (data.containsKey("VALID_REGION")) {
                    ArrayList<CardPos> valid = (ArrayList<CardPos>) data.get("VALID_REGION");
                    CardPos cardPos = CardPos.valueOf((String) data.get("TRIGGER_POS"));
                    CardEvent cardEvent = new CardEvent(cardPos, valid);
                    UIController.pushEvent(cardEvent);
                }
                break;

            case DESTROY_CARD_RESP: // Sent when at least 1 card has been destroyed by another card
                if (data.containsKey("CARD_LOCATIONS") && data.containsKey("GRAVE_LOCATIONS")) {
                    ArrayList<String> cardPoss = (ArrayList<String>) data.get("CARD_LOCATIONS");
                    ArrayList<String> graveposs = (ArrayList<String>) data.get("GRAVE_LOCATIONS");
                    for (String cardPos : cardPoss) {
                        int i = 0;
                        try {
                            CardEvent ce = new CardEvent(CardGraphicsObjectDictionary.getCardGraphicsObject(CardPos.valueOf(cardPos)).getCardInstance(),CardPos.valueOf(cardPos),CardPos.valueOf(graveposs.get(i)));
                            UIController.pushEvent(ce);
                        } catch (InvalidCardPosException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                }
                break;
            case SPAWN_CASTLES:
                if(data.containsKey("MY_CASTLE") && data.containsKey("THEIR_CASTLE")){
                    Double cardD = (Double)data.get("MY_CASTLE");
                    Double cardD2 = (Double)data.get("THEIR_CASTLE");
                    int cardId =  cardD.intValue();
                    int cardId2 = cardD2.intValue();
                    try {
                        Card card = (Card)Dictionary.getEntry(cardId).getConstructor().newInstance();
                        Card card2 = (Card)Dictionary.getEntry(cardId2).getConstructor().newInstance();
                        int myHealth = card.getPoints();
                        int theirHealth = card2.getPoints();
                        CardEvent ce1 = new CardEvent(myHealth,EventType.MY_CASTLE_HEALTH);
                        CardEvent ce2 = new CardEvent(theirHealth,EventType.THEIR_CASTLE_HEALTH);
                        UIController.pushEvent(ce1);
                        UIController.pushEvent(ce2);
                        CardEvent ce3 = new CardEvent(card,CardPos.MY_CASTLE);
                        CardEvent ce4 = new CardEvent(card2,CardPos.THEIR_CASTLE);
                        UIController.pushEvent(ce3);
                        UIController.pushEvent(ce4);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                else{

                }
                break;
            case MOVE_CARD:
                if (data.containsKey("CARD_MOVEMENTS")) {
                    Map<String, String> movements = (Map<String, String>) data.get("CARD_MOVEMENTS");
                    HashMap<CardPos, CardPos> movementsPos = new HashMap<>();
                    for (Map.Entry<String, String> entry : movements.entrySet()) {
                        movementsPos.put(CardPos.valueOf(entry.getKey()), CardPos.valueOf(entry.getValue()));
                    }

                    CardEvent cardEvent = new CardEvent(movementsPos);
                    UIController.pushEvent(cardEvent);
                }
                break;
            default:
                handleGameLogicAction(command, data);
                break;
        }
    }

    /**
     * Handles any actions to do with in-depth game logic.
     * @param command
     * @param data
     */
    private void handleGameLogicAction(CommandType command, HashMap<Object, Object> data) {
        switch(command) {
            case PLAY_CARD_RESP:
                handlePlayCardResp(data);
                break;
        }
    }

    /**
     * Handles a PLAY_CARD_RESP command from the server
     * @param data
     */
    private void handlePlayCardResp(HashMap<Object, Object> data) {
        if (data.containsKey("PLAYER_ID") && data.containsKey("START_POS") && data.containsKey("END_POS") && data.containsKey("CARD_ID")) {
            Integer playerid = ((Double) data.get("PLAYER_ID")).intValue();
            Integer cardid = ((Double) data.get("CARD_ID")).intValue();
            CardPos startPos = (CardPos) data.get("START_POS");
            CardPos endPos = (CardPos) data.get("END_POS");
            try {
                Card card = (Card) Dictionary.getEntry(cardid).getConstructor().newInstance();
                CardEvent cardEvent = new CardEvent(card, startPos, endPos);
                UIController.pushEvent(cardEvent);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
