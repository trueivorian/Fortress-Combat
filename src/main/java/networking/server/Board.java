package networking.server;

import com.google.gson.Gson;
import gamelogic.Card;
import gamelogic.CardType;
import graphics.utils.CardGraphicsObjectDictionary;
import networking.CommandType;
import networking.server.utils.CardDraw;
import networking.server.utils.OnPlayAction;
import ui.Launcher;
import utils.CardPos;
import utils.Cost;
import utils.Dictionary;
import utils.GameState;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static utils.GameState.AWAIT_CHOOSE_COST;
import static utils.GameState.AWAIT_ONPLAY_CHOOSE;

@SuppressWarnings("Duplicates")
public class Board {
    HashMap<CardPos, Card> inPlay = new HashMap<>();
    int castleHealth;
    int playerID;
    boolean soldierPlayed = false;
    boolean magePlayed = false;
    private boolean locked = false; //You shouldn't be able to activate or play things during someone else's turn, or when there is a message on the screen.
    private GameState state = GameState.NOTHING;
    private ArrayList<Cost> waitingCosts; // Used to store the costs we're waiting for the client to choose
    private int waitingCostsNewCastleHealth; // Used to store the health of the castle in the future if all the costs go through.
    private ArrayList<CardPos> spentCosts; // Used to store the costs the client has already chosen, until we can process them all
    private ArrayList<OnPlayAction> waitingOnPlay; // Used to store the on play actions we're waiting for the client to choose
    private HashMap<OnPlayAction, CardPos> spentOnPlay; // Used to store the on play actions the client has already chosen, until we can process them all
    private int waitingCardId;
    private CardPos destinationPos;
    private CardPos startPos;

    public Board(ArrayList<Integer> newDeck) {
        Integer sadfsd = newDeck.size() - 1;
        Integer castleid = Integer.parseInt(newDeck.get(sadfsd).toString()); // Castle is always last
        newDeck.remove(newDeck.size() - 1);
        Class cl = Dictionary.getEntry(castleid);
        try {
            Card castle = (Card) cl.getConstructor().newInstance();
            inPlay.put(CardPos.MY_CASTLE, castle);

            for (int i = 0; i < newDeck.size(); i++) {
                cl = Dictionary.getEntry(newDeck.get(i));
                Card card = (Card) cl.getConstructor().newInstance();

                inPlay.put(CardPos.valueOf("MY_DECK_" + (i + 1)), card); // Put the card into the deck

            }
            castleHealth = inPlay.get(CardPos.MY_CASTLE).getPoints();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void damage(int damage){
        castleHealth = castleHealth - damage;
        if(castleHealth<=0){

        }
        else{
            HashMap<Object,Object> data = new HashMap<>();
            data.put("COMMAND",CommandType.CASTLE_DAMAGE);
            data.put("MY_HEALTH",castleHealth);
            try {
                ClientTable.sendMessageToUser(playerID,data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            data.clear();
            data.put("COMMAND",CommandType.CASTLE_DAMAGE);
            data.put("THEIR_HEALTH",castleHealth);
            ClientTable.exclusiveBroadcast(playerID,data);
        }
    }
    /**
     * Inverts a card position (my -> theirs)
     *
     * @param cardPos
     * @return
     */



    private static CardPos invertCardPos(CardPos cardPos) {
        String representation = cardPos.toString();
        String[] parts = representation.split("_");
        String finalStr;

        if (representation.startsWith("MY")) {
            finalStr = "THEIR";
        } else {
            finalStr = "MY";
        }


        for (int i = 1; i < parts.length; i++) {
            finalStr += "_" + parts[i];
        }

        return CardPos.valueOf(finalStr);
    }

    /**
     * *
     * @return Hashmap containing the deck in terms of CardPos
     */
    public HashMap<CardPos,Integer> getDeck(){
        int i=2;
        HashMap<CardPos, Integer> tDeck = new HashMap<>();
        CardPos cp = CardPos.MY_DECK_1;
        boolean allOfDeck = false;
        while(!allOfDeck){
            tDeck.put(cp,inPlay.get(cp).getId());
            System.out.println("Next is : " + cp.next().toString());
            String s = "MY_DECK_" + i;
            i++;
            if(!inPlay.containsKey(CardPos.valueOf(s))){
                allOfDeck = true;
            }
            cp = cp.next();
        }
        return tDeck;
    }

    /**
     * Converts list of CardDraw to hashmap that can be turned into json
     *
     * @param drawnCards
     * @return
     */
    private static ArrayList<HashMap<Object, Object>> preprocess(ArrayList<CardDraw> drawnCards) {
        ArrayList<HashMap<Object, Object>> drawn = new ArrayList<>();

        for (CardDraw cardDraw : drawnCards) {
            HashMap<Object, Object> toAdd = new HashMap<>();
            toAdd.put("cardPos", cardDraw.getCardPos().toString());
            toAdd.put("card", cardDraw.getCard().getId());
            drawn.add(toAdd);

        }

        return drawn;
    }

    /**
     * This method re-organises the player's hand such that there are no gaps
     */
    private void shiftHand(){
        HashMap<CardPos,CardPos> h = new HashMap<>(); // from, to
        int emptyPoint = -1;

        for (int i = 1; i < 11; i++) {
            if (!inPlay.containsKey(CardPos.valueOf("MY_HAND_" + i))) {
                emptyPoint = i;
                break;
            }
        }

        if (emptyPoint != -1) {
            System.out.println("was not -1");
            for (int i = emptyPoint + 1; i < 11; i++) {
                CardPos cardPos = CardPos.valueOf("MY_HAND_" + i);
                CardPos endPos = CardPos.valueOf("MY_HAND_" + (i - 1));

                if (!inPlay.containsKey(cardPos)) {
                    System.out.println("ended at " + i);
                    break;
                }

                // Otherwise, shift the card down.
                System.out.println("start: " + cardPos.toString());
                System.out.println("end: " + endPos.toString());
                h.put(cardPos, endPos);


                Card card = inPlay.remove(cardPos);
                inPlay.put(endPos, card);
            }

            move(h);
        }
    }


    private void move(HashMap<CardPos, CardPos> h){
        HashMap<Object,Object> data = new HashMap<>();
        data.put("COMMAND", CommandType.MOVE_CARD);
        data.put("CARD_MOVEMENTS", h);
        try {
            ClientTable.sendMessageToUser(playerID, data);

            HashMap<CardPos, CardPos> inverted = new HashMap<>();
            for (Map.Entry<CardPos, CardPos> entry : h.entrySet()) {
                inverted.put(invertCardPos(entry.getKey()), invertCardPos(entry.getValue()));
            }
            data.replace("CARD_MOVEMENTS", inverted);
            ClientTable.exclusiveBroadcast(playerID, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isSoldierPlayed() {
        return soldierPlayed;
    }

    public void setSoldierPlayed(boolean soldierPlayed) {
        this.soldierPlayed = soldierPlayed;
    }

    public boolean isMagePlayed() {
        return magePlayed;
    }

    public void setMagePlayed(boolean magePlayed) {
        this.magePlayed = magePlayed;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }



    public void shuffle() {
        shuffle(true);
    }

    public void shuffle(boolean shouldSend) {
        HashMap<CardPos, Card> newInPlay = new HashMap<>();
        ArrayList<CardPos> shuffledContents = new ArrayList<>();
        HashMap<CardPos, CardPos> movements = new HashMap<>();
        ArrayList<CardPos> used = new ArrayList<>();

        for (int i = 1; i < 50; i++) {
            CardPos cardPos = CardPos.valueOf("MY_DECK_" + i);
            if (inPlay.containsKey(cardPos)) {
                shuffledContents.add(cardPos);
            } else {
                break;
            }
        }

        Collections.shuffle(shuffledContents);

        for (int i = 0; i < shuffledContents.size(); i++) {
            CardPos cardPos = CardPos.valueOf("MY_DECK_" + (i + 1));
            newInPlay.put(cardPos, inPlay.get(shuffledContents.get(i)));

            movements.put(shuffledContents.get(i), cardPos);
            used.add(shuffledContents.get(i));
        }

        for (Map.Entry<CardPos, Card> entry : inPlay.entrySet()) {
            if (!used.contains(entry.getKey())) {
                newInPlay.put(entry.getKey(), entry.getValue());
            }
        }

        inPlay = new HashMap<>(newInPlay);

        if (shouldSend)
            sendShuffleData(movements);
    }

    private void sendShuffleData(HashMap<CardPos, CardPos> movements) {
        HashMap<Object, Object> toSend = new HashMap<>();
        toSend.put("COMMAND", CommandType.MOVE_CARD);
        toSend.put("CARD_MOVEMENTS", movements);

        try {
            ClientTable.sendMessageToUser(playerID, toSend);

            HashMap<CardPos, CardPos> inverted = new HashMap<>();

            for (Map.Entry<CardPos, CardPos> entry : movements.entrySet()) {
                inverted.put(invertCardPos(entry.getKey()), entry.getValue());
            }

            toSend.replace("CARD_MOVEMENTS", inverted);
            ClientTable.exclusiveBroadcast(playerID, toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the next available hand space
     *
     * @return
     */
    public CardPos nextAvailableHandSpace() {
        for (int i = 1; i < 11; i++) {
            if (!inPlay.containsKey(CardPos.valueOf("MY_HAND_" + i)))
                return CardPos.valueOf("MY_HAND_" + i);
        }

        return null;
    }

    /**
     * Draws a new card from the deck to the hand
     *
     * @return
     */
    public CardDraw draw() {
        CardPos nextCard = getNextAvailableDeckCard();
        CardPos handSpace = nextAvailableHandSpace();
        Card card = inPlay.remove(nextCard);

        inPlay.put(handSpace, card);

        CardDraw cardDraw = new CardDraw();
        cardDraw.setCard(card);
        cardDraw.setCardPos(handSpace);
        cardDraw.setOldPos(nextCard);

        return cardDraw;
    }

    private CardPos getNextAvailableDeckCard() {
        for (int i = 50; i > 0; i--) {
            CardPos cardPos = CardPos.valueOf("MY_DECK_" + i);
            if (inPlay.containsKey(cardPos)) {
                return cardPos;
            }
        }

        return null;
    }

    /**
     * Returns all cards to deck
     */
    public void returnCardsToDeck() {
        HashMap<CardPos, CardPos> movements = new HashMap<>();

        for (int i = 1; i < 11; i++) { // Loop through all hand positions and move them back to the deck
            CardPos cardPos = CardPos.valueOf("MY_HAND_" + i);
            if (inPlay.containsKey(cardPos)) {
                Card card = inPlay.remove(cardPos);
                CardPos newPos = moveCardToDeck(card);
                movements.put(cardPos, newPos);
            }
        }

        HashMap<Object, Object> toSend = new HashMap<>();
        toSend.put("COMMAND", CommandType.MOVE_CARD);
        toSend.put("CARD_MOVEMENTS", movements);
        try { // Send the message to the user and then to the other user
            ClientTable.sendMessageToUser(playerID, toSend);

            HashMap<CardPos, CardPos> inverted = new HashMap<>();
            for (Map.Entry<CardPos, CardPos> entry : movements.entrySet()) {
                inverted.put(invertCardPos(entry.getKey()), invertCardPos(entry.getValue()));
            }
            toSend.replace("CARD_MOVEMENTS", inverted);
            ClientTable.exclusiveBroadcast(playerID, toSend);
        } catch (IOException e) {
            // something went wrong, we can't recover
        }
    }

    /**
     * Takes a card and puts it into the next available space in the deck
     * @param card
     */
    private CardPos moveCardToDeck(Card card) {
        for (int i = 1; i < 51; i++) {
            CardPos cardPos = CardPos.valueOf("MY_DECK_" + i);
            if (!inPlay.containsKey(cardPos)) {
                inPlay.put(cardPos, card);
                return cardPos;
            }
        }

        return null;
    }

    /**
     * Moves a card from a specified location into the next free grave space
     *
     * @param enume
     * @param pid
     */
    public void destroy(CardPos enume, int pid) {
        if (inPlay.containsKey(enume)) {
            try {
                internalDestroyCard(enume, pid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Discards a card in a specified location
     *
     * @param card
     */
    public void discard(CardPos card) {
        HashMap<Object, Object> toSend = new HashMap<>();
        toSend.put("COMMAND", CommandType.MOVE_CARD);
        HashMap<CardPos, CardPos> movements = new HashMap<>();

        CardPos graveSpace = getNextGraveSpace(); // get next available grave space
        inPlay.put(graveSpace, inPlay.get(card)); // put card in grave space
        inPlay.remove(card); // remove card from original pos
        movements.put(card, graveSpace); // register movement to be sent to clients
        toSend.put("CARD_MOVEMENTS", movements);
        try {
            ClientTable.sendMessageToUser(playerID, toSend);

            movements = new HashMap<>();
            movements.put(invertCardPos(card), invertCardPos(graveSpace));
            toSend.replace("CARD_MOVEMENTS", movements);
            ClientTable.exclusiveBroadcast(playerID, toSend);
        } catch (IOException e) {
            // We can't do anything here - network failure
        }
    }

    /**
     * Activates a trickster in a certain position
     * @param target
     */
    public void activateTrickster(CardPos target) {
        // Verify we can actually activate this position
        if (inPlay.containsKey(target) && inPlay.get(target).getType().equals(CardType.TRICKSTER)) {

        }
    }

    /**
     * Levels a mage in a specified location
     *
     * @param mage
     */
    public void level(CardPos mage) {
        if (inPlay.get(mage).getLevel() < 3 && !inPlay.get(mage).hasLeveled()) {

            inPlay.get(mage).setLevel(inPlay.get(mage).getLevel() + 1);
            //TODO send message to client
        } else {
            //TODO send message to client
        }
    }

    /**
     * Returns the next available free grave space
     *
     * @return
     */
    public CardPos getNextGraveSpace() {
        CardPos enume = CardPos.MY_DISCARD_1;
        for (int i = 0; i <= 49; i++) {
            if (!inPlay.containsKey(enume)) {
                return enume;
            }

            enume = enume.next();
        }

        return null;
    }

    /**
     * Stuns a card in a specified location
     *
     * @param pos
     */
    public void stunCard(CardPos pos) {
        Card card = inPlay.get(pos);
        card.setStunned(true);
        inPlay.replace(pos, card);
    }

    /**
     * Un-stuns a card
     *
     * @param pos
     */
    public void unStunCard(CardPos pos) {
        Card card = inPlay.get(pos);
        card.setStunned(false);
        inPlay.replace(pos, card);
    }

    public int getCastleHealth() {
        return castleHealth;
    }

    public void setCastleHealth(int s) {
        castleHealth = s;
    }

    public HashMap<CardPos, Card> get() {
        return inPlay;
    }

    /**
     * Spends a cost
     *
     * @param cardPos
     */
    public void spendCost(CardPos cardPos, Board enemyBoard) {
        spentCosts.add(cardPos);
        waitingCosts.remove(0);

        if (waitingCosts.size() == 0) {
            processCosts(enemyBoard);
        } else {
            Cost cost = waitingCosts.get(waitingCosts.size() - 1);
            waitingCosts.remove(waitingCosts.size() - 1);
            HashMap<Object, Object> toSend = new HashMap<>();
            toSend.put("COMMAND", CommandType.CHOOSE_COST);
            toSend.put("VALID_REGION", getFriendlyCardTypePositions(cost.getCt()));
            try {
                ClientTable.sendMessageToUser(playerID, toSend);
            } catch (IOException e) {
                // todo: do something here
            }
        }
    }

    /**
     * Processes chosen costs and triggers the on play choosing
     */
    private void processCosts(Board enemyBoard) {
        // Loop through the cards and destroy each card
        for (CardPos cardPos : spentCosts) {
            try {
                internalDestroyCard(cardPos, playerID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        castleHealth = waitingCostsNewCastleHealth;
        HashMap<Object, Object> toSend = new HashMap<>();
        toSend.put("COMMAND", CommandType.CASTLE_DAMAGE);
        toSend.put("MY_HEALTH", castleHealth);
        try {
            ClientTable.sendMessageToUser(playerID, toSend);

            toSend.remove("MY_HEALTH");
            toSend.put("THEIR_HEALTH", castleHealth);
            ClientTable.exclusiveBroadcast(playerID, toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Now we've processed the costs, we need to consider any on-play actions the card may have.
        // If any of the on-play actions don't have an applicable card, we'll just ignore them.
        // Excuse the horrible code
        Card card = inPlay.get(startPos);
        try {
            for (int i = 0; i < 59; i++) {
                Class<?> sclass = Dictionary.getEntry(i);
                if (sclass.isInstance(card)) {
                    ArrayList<OnPlayAction> actions;
                    actions =  (ArrayList<OnPlayAction>) sclass.cast(card).getClass().getMethod("onPlay").invoke(card);
                    if (actions != null) {
                        if (actions.size() > 0) { // Check if there are on-play actions to process
                            waitingOnPlay = actions;
                            spentOnPlay = new HashMap<>();

                            // Send out the first on-play action to choose
                            ArrayList<Object> returned = getNextValidWaitingAction(enemyBoard);
                            enemyBoard = (Board) returned.get(1);
                            OnPlayAction action = (OnPlayAction) returned.get(0);

                            if (action != null) {
                                ArrayList<CardPos> region = getValidLocationsFromOnPlay(action);
                                HashMap<Object, Object> data = new HashMap<>();
                                data.put("COMMAND", CommandType.TARGET_CARD);
                                data.put("MSG", "Choose an enemy soldier to stun"); // TODO: process all messages
                                data.put("VALID_REGION", region);
                                state = AWAIT_ONPLAY_CHOOSE;
                                try {
                                    ClientTable.sendMessageToUser(playerID, data);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                processOnPlayActions();
                            }
                        } else {
                            processOnPlayActions();
                        }
                        break;
                    } else {
                        processOnPlayActions();
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spends an on play action that we're currently waiting for
     * @param cardPos The card position to spend
     */
    public ArrayList<Object> spendOnPlay(CardPos cardPos, Board enemyBoard) {
        ArrayList<Object> toReturn = new ArrayList<>();
        OnPlayAction action = waitingOnPlay.remove(0);
        spentOnPlay.put(action, cardPos);

        if (waitingOnPlay.size() == 0) { // If there are no actions left to get/spend, we need to process the choices we have collected
            toReturn.add(0, true);
            toReturn.add(1, enemyBoard);
            return toReturn;
        } else { // Otherwise, we'll get the next action and send it out to be chosen
            ArrayList<Object> returned = getNextValidWaitingAction(enemyBoard);
            OnPlayAction toChoose = (OnPlayAction) returned.get(0);
            enemyBoard = (Board) returned.get(1);

            if (toChoose != null) {
                ArrayList<CardPos> region = getValidLocationsFromOnPlay(toChoose);

                HashMap<Object, Object> data = new HashMap<>();
                data.put("COMMAND", CommandType.TARGET_CARD);
                data.put("MSG", "Choose an enemy soldier to stun");
                data.put("VALID_REGION", region);
                try {
                    ClientTable.sendMessageToUser(playerID, data);
                } catch (IOException e) { // User has lost connection, probably.
                    Launcher.showAlert("Connection unexpectedly lost. Exiting game.");
                }
            } else { // If we can't play this on play, ignore it instead
                toReturn.add(0, true);
                toReturn.add(1, enemyBoard);
                return toReturn;
            }
        }

        toReturn.add(0, false);
        toReturn.add(1, enemyBoard);
        return toReturn;
    }

    /**
     * Returns all valid card poss for a specified OnPlayAction
     * @param action
     * @return
     */
    private ArrayList<CardPos> getValidLocationsFromOnPlay(OnPlayAction action) {
        ArrayList<CardPos> region = new ArrayList<>();

        switch(action) {
            case STUN_ENEMY_SOLDIER:
                region.add(CardPos.THEIR_SOLDIER_1);
                region.add(CardPos.THEIR_SOLDIER_2);
                break;
        }

        return region;
    }

    /**
     * Returns the next valid waiting on play action and deletes any leading up to it that aren't valid.
     *
     * This method also processes any non-targeting actions leading up to the next valid targeting action.
     * @return The next valid action, or null if none
     */
    private ArrayList<Object> getNextValidWaitingAction(Board enemyBoard) {
        ArrayList<Object> toReturn = new ArrayList<>();

        System.out.println("called next valid");
        for (int i = 0; i < waitingOnPlay.size(); i++) { // Loop through all current waiting actions
            OnPlayAction action = waitingOnPlay.get(0);
            System.out.println("trying on play: " + i + " " + action.toString());
            if (!action.requiresTarget()) { // If it's a non-targeting action, we need to process it
                enemyBoard = processNonTargetingOnPlay(waitingOnPlay.remove(0), enemyBoard);
            } else if (isValidOnPlayAction(action, enemyBoard)) { // Otherwise, if it's valid, we need to target it
                System.out.println("was valid - returning action");
                toReturn.add(0, action);
                toReturn.add(1, enemyBoard);
                return toReturn;
            } else { // Otherwise, we'll ignore it
                waitingOnPlay.remove(0);
            }
        }

        return null;
    }
    private static ArrayList<CardDraw> invertCardSet(ArrayList<CardDraw> cardSet) {
        for (int i = 0; i < cardSet.size(); i++) {
            CardDraw cardDraw = cardSet.get(i);
            cardDraw.setCardPos(invertCardPos(cardDraw.getCardPos()));
            cardSet.set(i, cardDraw);
        }

        return cardSet;
    }
    /**
     * Processes an on-play action that doesn't require the user to target a card.
     * @param action
     * @param enemyBoard
     */
    private Board processNonTargetingOnPlay(OnPlayAction action, Board enemyBoard) {
        System.out.println("process non targeting on play");
        switch(action) {
            case DISCARD_RANDOM_ENEMY_HAND: // We need to pick a random enemy card and discard it
                System.out.println("discard rand enemy hand");
                int numCards = enemyBoard.getNumCardsInHand();
                Random random = new Random();
                int toDiscard = random.nextInt(numCards) + 1; // Pick a card at random and account for 0-indexing
                CardPos cardPosDiscard = CardPos.valueOf("MY_HAND_" + toDiscard);
                enemyBoard.discard(cardPosDiscard); // Discard card
                break;
            case DRAW_CARDS_1:
                drawCards(false);
                shiftHand();
            case DRAW_CARDS_2:
                drawCards(false);
                drawCards(false);
                shiftHand();
        }

        enemyBoard.shiftHand();
        return enemyBoard;
    }
    public void drawCards(boolean clearFlag){
        CardDraw cards = draw();
        ArrayList<CardDraw> list = new ArrayList<>();
        list.add(cards);
        ArrayList<HashMap<Object,Object>> data = preprocess(list);
        HashMap<Object,Object> toSend = new HashMap<>();
        toSend.put("COMMAND", CommandType.DRAW_CARD_RESP);
        toSend.put("DRAWN_CARDS", data);
        toSend.put("USER_ID", playerID);
        toSend.put("CLEAR_FLAG",clearFlag);
        try {
            ClientTable.sendMessageToUser(playerID,toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<CardDraw> inverted = invertCardSet(list);
        ArrayList<HashMap<Object,Object>> processed = preprocess(inverted);
        toSend = new HashMap<>();
        toSend.put("COMMAND", CommandType.DRAW_CARD_RESP);
        toSend.put("DRAWN_CARDS", processed);
        toSend.put("USER_ID", playerID);
        ClientTable.exclusiveBroadcast(playerID, toSend);
    }


    /**
     * Returns the number of cards currently in the hand section of the board for this user
     * @return The number of cards in the hand
     */
    public int getNumCardsInHand() {
        String currentPos;

        for (int i = 1; i < 11; i++) {
            currentPos = "MY_HAND_" + i;
            if (!inPlay.containsKey(CardPos.valueOf(currentPos))) {
                return i - 1;
            }
        }

        return 10;
    }

    /**
     * Validates an action and returns true if there is a valid card on the board for this action and false if not.
     * @param action
     * @return
     */
    private boolean isValidOnPlayAction(OnPlayAction action, Board enemyBoard) {
        ArrayList<CardPos> validLocations = getValidLocationsFromOnPlay(action);

        for (int i = 0; i < validLocations.size(); i++) {
            if (enemyBoard.hasCardInPos(invertCardPos(validLocations.get(i)))) // Invert card pos to account for enemy board
                return true;
        }

        return false;
    }

    public boolean hasCardInPos(CardPos pos) {
        return inPlay.containsKey(pos);
    }

    /**
     * Processes chosen on play actions when none have been chosen
     */
    private void processOnPlayActions() {
        try {
            internalPlayCard(waitingCardId, startPos, destinationPos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        state = GameState.NOTHING;
    }

    /**
     * Processes chosen on play actions and plays the relevant card
     * @param enemyBoard The opponent's board - this will be modified
     * @return The modified opponent's board
     */
    public Board processOnPlayActions(Board enemyBoard) {
        // So, all on play actions have been chosen and now we need to process them

        // Loop through all spent actions and figure out what we need to do with them
        for (Map.Entry<OnPlayAction, CardPos> action : spentOnPlay.entrySet()) {
            switch(action.getKey()) {
                case STUN_ENEMY_CARD:
                    enemyBoard.stunCard(action.getValue()); // Use the enemy board to stun this card
                    break;
            }
        }

        // Now we've processed the costs and on play actions, we need to play the card in question
        try {
            System.out.println("hello");
            internalPlayCard(waitingCardId, startPos, destinationPos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Make sure we processNextTurn this
        state = GameState.NOTHING;
        return enemyBoard;
    }

    /**
     * Begins the process of getting any valid on-play card requests for the card in question
     * @param cardPos
     */
    private Board beginOnPlayRequests(CardPos cardPos, Board enemyBoard) {
        if (inPlay.get(cardPos).getOnPlayWait().size() > 0) { // If there are any on play actions to get, we need to start getting them
            System.out.println("got on play wait");
            waitingOnPlay = inPlay.get(cardPos).getOnPlayWait();
            ArrayList<Object> returned = getNextValidWaitingAction(enemyBoard);

            if (returned != null) {
                OnPlayAction action = (OnPlayAction) returned.get(0);
                enemyBoard = (Board) returned.get(1);
                HashMap<Object, Object> data = new HashMap<>();
                data.put("COMMAND", CommandType.TARGET_CARD);
                data.put("VALID_REGION", getValidLocationsFromOnPlay(action));
                data.put("TRIGGER_POS", cardPos);
                data.put("MSG", "Please pick a location to target for this on play action");
                try {
                    ClientTable.sendMessageToUser(playerID, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                state = AWAIT_ONPLAY_CHOOSE;
            } else {
                processOnPlayActions();
            }
        } else { // Otherwise, we can jump straight to processing all the information we've received so far
            processOnPlayActions();
        }

        return enemyBoard;
    }

    /**
     * Plays a card from one location to another
     *
     * @param CardId
     * @param start
     * @param end
     * @param isabillity
     * @param playerid
     * @throws IOException
     */
    public void playCard(int CardId, CardPos start, CardPos end, boolean isabillity, int playerid, Board enemyBoard) throws IOException {
        // stuff
        try {
            Gson gson = new Gson();
            // We need to remember some stuff
            startPos = start;
            destinationPos = end;
            waitingCardId = CardId;

            if (isValidSpace(end, CardId)) { // todo: Check the user can play the card
                waitingCostsNewCastleHealth = castleHealth;
                spentCosts = new ArrayList<>();

                Card card = (Card) Dictionary.getEntry(CardId).getConstructor().newInstance();
                // If this card is already a soldier or mage and we've already played one of that type, discard command
                switch (card.getType()) {
                    case SOLDIER:
                        if (soldierPlayed) {
                            return;
                        }
                        break;
                    case MAGE:
                        if (magePlayed) {
                            return;
                        }
                        break;
                }

                // Now, we consider costs and whether or not the user can satisfy a cost
                // Check if the card has a cost
                if (card.isHasCost()) {
                    System.out.println("HELLO - HAS COST");
                    state = AWAIT_CHOOSE_COST; // we're waitingCosts for the client to send us the relevant targets for the costs

                    if (checkValidCostExists(card)) { // Check if we can satisfy a cost set for the card
                        if (!card.isHasCost()) {
                            beginOnPlayRequests(start, enemyBoard);
                            return;
                        }

                        waitingCosts = card.getCosts().get(0); // set waiting costs

                        System.out.println("number of items: " + waitingCosts.size());

                        waitingCosts = getNextCost(waitingCosts);
                        Cost cost = waitingCosts.get(0);

                        if (waitingCosts.size() > 0) {
                            HashMap<Object, Object> toSend = new HashMap<>();
                            toSend.put("COMMAND", CommandType.TARGET_CARD);
                            HashSet<CardPos> region =new HashSet<>();
                            region.addAll(getFriendlyCardTypePositions(cost.getCt()));
                            toSend.put("VALID_REGION", region);
                            toSend.put("TRIGGER_POS", start);
                            ClientTable.sendMessageToUser(playerid, toSend);
                            System.out.println("Sent target message to user");
                        } else {
                            System.out.println("also skipped costs");
                            beginOnPlayRequests(startPos, enemyBoard);
                        }
                    } else {
                        System.out.println("SERVER | Rejected card play due to unsatisfiable cost.");
                        System.out.println("SERVER | waitingCosts: " + gson.toJson(card.getCosts()));
                    }
                } else {
                    // Some additional checking needs to be done here - the card has no cost, but is being played into a correct space and a soldier has not been played, etc.
                    System.out.println("Skipped costs - begun on play actions gathering");
                    beginOnPlayRequests(startPos, enemyBoard);
                }
            } else {
                System.out.println("Could not play card - not valid space");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the next waiting cost to be processed, assumes waitingCosts has been set
     * @return
     */
    private ArrayList<Cost> getNextCost(ArrayList<Cost> waiting) {
        for (int i = 0; i < waiting.size(); i++) {
            Cost cost = waiting.get(0);

            if (cost.getCt() == CardType.CASTLE) {
                waitingCostsNewCastleHealth = waitingCostsNewCastleHealth - cost.getNum();
                waiting.remove(0);
                System.out.println("ended");
            } else {
                System.out.println("ended");
                return waiting;
            }
        }

        return waiting;
    }

    private boolean isSpecialCost(Cost cost) {
        return cost.getCt() == CardType.CASTLE;
    }

    /**
     * Internal method for playing a card onto this user's board from their hand
     *
     * @param CardId
     * @param start
     * @param end
     * @throws IOException
     */
    private void internalPlayCard(int CardId, CardPos start, CardPos end) throws IOException {
        try {
            inPlay.remove(start); // remove the card from the deck

            // Make note of the fact that we just played this card
            switch (((Card) Dictionary.getEntry(CardId).getConstructor().newInstance()).getType()) {
                case SOLDIER:
                    System.out.println("got to soldier case");
                    soldierPlayed = true;
                    break;
                case MAGE:
                    magePlayed = true;
                    break;
            }

            inPlay.put(end, (Card) Dictionary.getEntry(CardId).getConstructor().newInstance());
            HashMap<Object, Object> data = new HashMap<>();
            data.put("COMMAND", CommandType.PLAY_CARD_RESP);
            data.put("PLAYER_ID", playerID);
            data.put("CARD_ID", CardId);
            data.put("START_POS", start);
            data.put("END_POS", end);
            ClientTable.sendMessageToUser(playerID, data);
            start = invertCardPos(start);
            data.replace("START_POS", start);
            end = invertCardPos(end);
            data.replace("END_POS", end);
            ClientTable.exclusiveBroadcast(playerID, data);
            System.out.println("shiftHand should be called");
            shiftHand();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal method for destroying a card on this user's board
     *
     * @param cardPos
     * @param playerid
     * @throws IOException
     */
    private void internalDestroyCard(CardPos cardPos, int playerid) throws IOException {
        if (inPlay.containsKey(cardPos)) {
            CardPos gravePos = getNextGraveSpace();
            HashMap<Object, Object> data = new HashMap<>();
            data.put("COMMAND", CommandType.DESTROY_CARD_RESP);
            ArrayList<CardPos> locations = new ArrayList<>();
            locations.add(cardPos);
            ArrayList<CardPos> gravePoss = new ArrayList<>();
            gravePoss.add(gravePos);
            data.put("CARD_LOCATIONS", locations);
            data.put("IN_RESPONSE", false);
            data.put("GRAVE_LOCATIONS", gravePoss);
            ClientTable.sendMessageToUser(playerid, data);

            // Now, send the same data to all other users but invert the card locations
            ArrayList<CardPos> inverted = new ArrayList<>();
            for (CardPos cp : locations) {
                inverted.add(invertCardPos(cp));
            }

            ArrayList<CardPos> invertedGravePoss = new ArrayList<>();
            for (CardPos cp : gravePoss) {
                invertedGravePoss.add(invertCardPos(cp));
            }
            data.replace("CARD_LOCATIONS", inverted);
            data.replace("GRAVE_LOCATIONS", invertedGravePoss);
            ClientTable.exclusiveBroadcast(playerID, data);
        }
    }


    /**
     * Returns the list of friendly positions where a certain card type lies
     *
     * @param cardType
     * @return
     */
    private ArrayList<CardPos> getFriendlyCardTypePositions(CardType cardType) {
        ArrayList<CardPos> positions = new ArrayList<>();

        for (CardPos cp : CardPos.values()) {
            if (inPlay.containsKey(cp)) {
                if (inPlay.get(cp).getType().equals(cardType) && !cp.toString().startsWith("MY_HAND")) {
                    positions.add(cp);
                }
            }
        }

        return positions;
    }

    /**
     * Checks if a valid cost set exists for a specified card
     *
     * @param card
     * @return
     */
    public boolean checkValidCostExists(Card card) {
        ArrayList<ArrayList<Cost>> costs = card.getCosts();

        for (ArrayList<Cost> costset : costs) {
            if (costIsSatisfied(costset)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a cost set can be satisfied with the current board
     *
     * @param costs
     * @return
     */
    private boolean costIsSatisfied(ArrayList<Cost> costs) {
        Gson gson = new Gson();
        System.out.println("checking the following costs: " + gson.toJson(costs));
        ArrayList<CardPos> used = new ArrayList<>();

        for (Cost cost : costs) {
            for (Map.Entry<CardPos, Card> entry : inPlay.entrySet()) {
                if (cost.satisfies(entry.getValue()) && !used.contains(entry.getKey())) {
                    used.add(entry.getKey());
                    break;
                }
            }
        }

        return used.size() >= costs.size();
    }

    /**
     * Checks if the cost specified can be applied to a card on the board
     *
     * @param cost
     * @return
     */
    private int getValidCost(Cost cost) {
        for (Map.Entry<CardPos, Card> entrySet : inPlay.entrySet()) {
            if (cost.satisfies(entrySet.getValue())) {
                return entrySet.getValue().getUid();
            }
        }

        return -1;
    }

    /**
     * Checks if a card can be played into a specific space
     *
     * @param cardPos
     * @param cardid
     * @return
     */
    private boolean isValidSpace(CardPos cardPos, int cardid) {
        try {
            CardType cardType = ((Card) Dictionary.getEntry(cardid).getConstructor().newInstance()).getType();
            if (cardType == CardType.SOLDIER && cardPos.toString().startsWith("MY_SOLDIER"))
                return true;
            else if (cardType == CardType.DECREE && cardPos.toString().startsWith("MY_DECREE"))
                return true;
            else if (cardType == CardType.MAGE && cardPos.toString().startsWith("MY_MAGE"))
                return true;
            else if (cardType == CardType.TRICKSTER && cardPos.toString().startsWith("MY_TRICKSTER"))
                return true;
            else
                return false;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Refreshes the board after a turn has ended
     */
    public void processNextTurn() {
        soldierPlayed = false;
        magePlayed = false;

        HashMap<CardPos, Card> dupInPlay = new HashMap<>(inPlay);
        for (Map.Entry<CardPos, Card> entry : dupInPlay.entrySet()) {
            entry.getValue().processTurn();
            inPlay.replace(entry.getKey(), entry.getValue());
        }
    }

    private boolean validSpaceExists(Board tBoard, ArrayList<CardPos> cardPos) {
        for (CardPos c : cardPos) {
            if (tBoard.inPlay.containsKey(c))
                return true;
        }

        System.out.println("No valid spaces found for this action");
        return false;
    }

    /**
     * Returns true if the player's board is currently waitingCosts for them to target a location for them to spend
     *
     * @return
     */
    public boolean isWaitingForCost() {
        return (state == AWAIT_CHOOSE_COST);
    }

    public boolean isWaitingForOnPlay() {
        return state == AWAIT_ONPLAY_CHOOSE;
    }
}
