package networking.client;

import com.google.gson.Gson;
import gamelogic.Card;
import graphics.exceptions.InvalidCardPosException;
import graphics.utils.CardEvent;
import graphics.utils.CardGraphicsObject;
import graphics.utils.CardGraphicsObjectDictionary;
import graphics.utils.EventType;
import utils.CardPos;
import networking.CommandType;
import networking.exceptions.CardLogicException;
import utils.Dictionary;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Client Logic - for processing board-related logic on the client-side
 */
public class ClientLogic {
    private static int currentPlayerTurn;

    public static void setPlayerid(int playerid) {
        ClientLogic.playerid = playerid;
    }

    private static int playerid;

    /**
     * Called when the user has picked a choice - either true or false
     * @param choice
     */
    public void respondToDialogue(boolean choice) {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("COMMAND", CommandType.DIAG_RESP);
        data.put("BOOL", choice);
        GameClient.sendMessage(data);
    }

    /**
     * Mulligans the current hand and requests a new one
     */
    public void mulliganHand() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("COMMAND", CommandType.GAME_START_MULLIGAN);
        GameClient.sendMessage(data);
    }

    /**
     * Accepts the hand drawn by the server
     */
    public void acceptHand() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("COMMAND", CommandType.GAME_START_ACCEPT);
        GameClient.sendMessage(data);
    }

    /**
     * Processes an update to the board
     * @param cardEvent The CardEvent received
     */
    public void update(CardEvent cardEvent) {
        EventType eventType = cardEvent.getEventType();
        Card card;

        // Figure out what type of event it is
        switch(eventType) {
            case MOVE_CARD: // Card moved (e.g. hand -> board)
                CardPos startPos = cardEvent.getStartPos();
                CardPos endPos = cardEvent.getEndPos();
                try {
                    CardGraphicsObject cardGraphicsObject = CardGraphicsObjectDictionary.getCardGraphicsObject(startPos);
                    CardGraphicsObjectDictionary.removeCardGraphicsObject(startPos);
                    CardGraphicsObjectDictionary.setCardGraphicsObject(endPos, cardGraphicsObject);
                    cardGraphicsObject.move(endPos);
                } catch (InvalidCardPosException e) {
                    e.printStackTrace();
                }
                break;
            case MOVE_MULTIPLE:
                handleMoveMultiple(cardEvent.getMoveList(), cardEvent);
                break;
        }
    }

    /**
     * Gets a card instance at a specific position
     * @param cardPos
     */
    @Deprecated
    public Card getCardAtPos(CardPos cardPos) throws CardLogicException, InvalidCardPosException {
        if (CardGraphicsObjectDictionary.containsCardGraphicsObject(cardPos))
            return CardGraphicsObjectDictionary.getCardGraphicsObject(cardPos).getCardInstance();
        else {
            throw new CardLogicException(cardPos);
        }
    }

    /**
     * Processes the targeting of one card by another
     * @param trigger
     * @param target
     */
    public void target(CardPos trigger, CardPos target) throws CardLogicException, InvalidCardPosException {
        if (CardGraphicsObjectDictionary.containsCardGraphicsObject(trigger) && CardGraphicsObjectDictionary.containsCardGraphicsObject(target)) {
            CardGraphicsObject cgo = CardGraphicsObjectDictionary.getCardGraphicsObject(trigger);

            try {
                for (int i = 0; i < 59; i++) {
                    Class<?> sclass = Dictionary.getEntry(i);
                    if (sclass.isInstance(cgo.getCardInstance())) {
                        Card triggerCard = cgo.getCardInstance();
                        sclass.cast(triggerCard).getClass().getMethod("onPlay", CardPos.class, CardPos.class).invoke(triggerCard, trigger, target);
                        break;
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                // do nothing
            }
        } else {
            throw new CardLogicException(trigger, target);
        }
    }

    // Begin attack, level, etc

    /**
     * Sends attack from card in cardPos1 to target in cardPos2
     * @param cardPos1 Attacker
     * @param cardPos2 Target
     * @throws CardLogicException Thrown when cannot be levelled
     */
    public void attack(CardPos cardPos1, CardPos cardPos2) throws CardLogicException {
        if (canAttack(cardPos1, cardPos2)) {
            HashMap<Object, Object> toSend = new HashMap<>();
            toSend.put("COMMAND", CommandType.ATTACK_CARD);
            toSend.put("ATTACKER", cardPos1);
            toSend.put("TARGET", cardPos2);
            toSend.put("PLAYER",playerid);
            GameClient.sendMessage(toSend);
        } else {
            throw new CardLogicException(cardPos1, cardPos2);
        }
    }

    /**
     * Levels a mage
     * @param cardPos The position of the mage
     * @throws CardLogicException Thrown when cannot be levelled
     */
    public void levelMage(CardPos cardPos) throws CardLogicException {
        if (canLevel(cardPos)) {
            HashMap<Object, Object> toSend = new HashMap<>();
            toSend.put("COMMAND", CommandType.LEVEL_MAGE_RESP);
            toSend.put("TARGET", cardPos);
            GameClient.sendMessage(toSend);
        } else {
            throw new CardLogicException(cardPos);
        }
    }

    /**
     * Activates a trickster on the board
     * @param cardPos The position of the trickster
     * @throws CardLogicException
     * @deprecated
     */
    @Deprecated
    public void activateTrickster(CardPos cardPos) throws CardLogicException {
        if (canActivateTrickster(cardPos)) {
           HashMap<Object, Object> data = new HashMap<>();
           data.put("COMMAND", CommandType.ACTIVATE_TRICKSTER);
           data.put("TARGET", cardPos);
           GameClient.sendMessage(data);
        } else {
            throw new CardLogicException(cardPos);
        }
    }

    /**
     * Activates a trickster on the board
     * @param target The target card to activate
     * @param trigger The card that triggered this
     * @param triggeringUserID The ID of the user that played the card that triggered this event
     */
    public void activateTrickster(CardPos target, CardPos trigger, int triggeringUserID) {
        if (canActivateTrickster(target)) {
            HashMap<Object, Object> data = new HashMap<>();
            data.put("COMMAND", CommandType.ACTIVATE_TRICKSTER);
            data.put("TARGET", target);
            data.put("RESPONSE_USER_ID", triggeringUserID);
            data.put("TRICKSTER_POS", trigger);
            GameClient.sendMessage(data);
        }
    }

    /**
     * Plays a card from one position in the hand to another on the board
     * @param cardPos1 The position to play from
     * @param cardPos2 The position to play to
     * @throws CardLogicException Thrown when the card cannot be played
     */
    public void play(CardPos cardPos1, CardPos cardPos2) throws CardLogicException, InvalidCardPosException {
        if (canPlay(cardPos1, cardPos2)) {
            HashMap<Object, Object> data = new HashMap<>();
            data.put("COMMAND", CommandType.PLAY_CARD);
            data.put("START_POS", cardPos1);
            data.put("END_POS", cardPos2);
            data.put("CARD_ID", CardGraphicsObjectDictionary.getCardGraphicsObject(cardPos1).getCardInstance().getId());
            data.put("ABILITY_TRIGGERED", false);
            GameClient.sendMessage(data);
        } else {
            throw new CardLogicException(cardPos1, cardPos2);
        }
    }

    // Begin canAttack, canLevel, etc
    public void getDeckCard(CardPos cp){
        
    }
    /**
     * Returns true if a card in pos1 can attack another in pos2
     * @param cardPos1 Position of attacker
     * @param cardPos2 Position of target
     * @return True if it can, false if not
     */
    public boolean canAttack(CardPos cardPos1, CardPos cardPos2) {
        return CardGraphicsObjectDictionary.containsCardGraphicsObject(cardPos1) &&
                CardGraphicsObjectDictionary.containsCardGraphicsObject(cardPos2);
    }

    /**
     * Returns true if a mage can increase its level
     * @param cardPos Position of mage
     * @return True if it can, false if not
     */
    public boolean canLevel(CardPos cardPos) {
        return CardGraphicsObjectDictionary.containsCardGraphicsObject(cardPos);
    }

    /**
     * Returns true if a
     * @param cardPos
     * @return
     */
    public boolean canActivateTrickster(CardPos cardPos) {
        return CardGraphicsObjectDictionary.containsCardGraphicsObject(cardPos);
    }

    /**
     * Returns true if a card can be played from one slot on the hand to another on the board.
     * @param cardPos1 The position to play from
     * @param cardPos2 The position to play to
     * @return True if it can be played, false if not
     */
    public boolean canPlay(CardPos cardPos1, CardPos cardPos2) {
        return CardGraphicsObjectDictionary.containsCardGraphicsObject(cardPos1) &&
                !CardGraphicsObjectDictionary.containsCardGraphicsObject(cardPos2);
    }

    public void endTurn() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("COMMAND", CommandType.TURN_END);
        GameClient.sendMessage(data);
    }

    /**
     * Used to respond to a dialogue
     * @param choice The user's choice
     */
    public void inPlay(CardPos cp){
        System.out.println("In play activated");
    }
    public void option(boolean choice) {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("COMMAND", CommandType.DIAG_RESP);
        data.put("BOOL", choice);
        GameClient.sendMessage(data);
    }

    public static void setCurrentPlayer(int id) {
        currentPlayerTurn = id;
    }


    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }


    // TODO: Implement
    public String getMyName() {
        return "";
    }

    // TODO: Implement
    public String getTheirName(){
        return "";
    }

    /**
     * Handles multiple card movements at once
     * @param movements
     */
    private void handleMoveMultiple(HashMap<CardPos, CardPos> movements, CardEvent event) {
        try {
            HashMap<CardPos, CardGraphicsObject> added = new HashMap<>();
            ArrayList<CardPos> removed = new ArrayList<>();

            for (Map.Entry<CardPos, CardPos> entry : movements.entrySet()) {
                removed.add(entry.getKey());
                added.put(entry.getValue(),CardGraphicsObjectDictionary.getCardGraphicsObject(entry.getKey()));
            }

            // Loop through all final positions
            for (Map.Entry<CardPos, CardGraphicsObject> entry : added.entrySet()) {
                CardGraphicsObject cardGraphicsObject = entry.getValue();
                removed.remove(entry.getKey());  // If something is being moved out of this spot, we don't want to delete the new contents later
                CardGraphicsObjectDictionary.setCardGraphicsObject(entry.getKey(), cardGraphicsObject); // Finally, replace the CGO
            }

            for (Map.Entry<CardPos, CardGraphicsObject> entry : added.entrySet()) {
                entry.getValue().move(entry.getKey()); // move graphics object
            }

            for (CardPos removal : removed) { // Loop through removed and remove the CGOs
                CardGraphicsObjectDictionary.removeCardGraphicsObject(removal);
            }
        } catch (InvalidCardPosException e) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ) :" + e.getCardPos().toString());
        }
    }
}
