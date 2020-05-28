package networking.server;

import gamelogic.*;
import graphics.utils.EventType;
import networking.CommandType;
import networking.server.utils.CardDraw;
import networking.server.utils.NetworkingEvent;
import ui.Launcher;
import utils.CardPos;
import utils.GameState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Sam Gunner on 19/02/2019.
 */
@SuppressWarnings("Duplicates")
public class ServerLogic extends Thread {
    private static HashMap<Integer, Board> boards = new HashMap<>(); // Current game states for each player
    private static ArrayList<Integer> clients = new ArrayList<>(); // Currently-connected clients
    private static HashMap<Integer, Boolean> ready = new HashMap<>(); // Currently-readied clients
    private static boolean gameStarted = false;
    private static int player; // represented by MY_...
    private static int player2;
    private static LinkedBlockingQueue<NetworkingEvent> toHandle;
    private static int currentPlayer;

    public ServerLogic(LinkedBlockingQueue<NetworkingEvent> events) {
        toHandle = events;
    }

    @Override
    public void run() {
        try {
            while (true) {
                NetworkingEvent event = toHandle.take();
                CommandType command = event.getCommandType();
                HashMap<Object, Object> structuredData = event.getCommand();

                switch (command) {
                   case GAME_START_ACCEPT: // The user has requested to accept their deck
                        handleGameStartAccept(command, structuredData);
                        break;
                    case GAME_START_MULLIGAN: // The user has requested to mulligan their deck
                        handleGameStartMulligan(command, structuredData);
                        break;
                    case PLAY_CARD: // The user has requested to play a card
                        handlePlayCard(command, structuredData);
                        break;
                    case TURN_END: // The user has ended their turn
                        System.out.println("hello");
                        if (currentPlayer == (Integer) structuredData.get("USER_ID")) {
                            onTurnEnd(currentPlayer);
                            nextTurn();
                        }
                        break;
//                    case DIAG_RESP: // The user has responded to a yes or no dialogue
//                        if(boards.get((Integer) structuredData.get("USER_ID")).getState() == GameState.AWAIT_MULL) {
//                            Boolean response = (Boolean) structuredData.get("BOOL");
//                            if (response) { // if we need to mulligan, handle it
//                                handleGameStartMulligan(command, structuredData);
//                            } else {
//                                handleGameStartAccept(command, structuredData);
//                            }
//                        } else {
//                            Launcher.showAlert("whoops");
//                        }
//                        break;
                    case TARGET_CARD_RESP: // The user has responded to a target card request
                        handleTargetCardResponse(command, structuredData);
                        break;
                    case ACTIVATE_TRICKSTER: // The user has requested to activate a trickster on their part of the board
                        handleActivateTrickster(command, structuredData);
                        break;
                    case ATTACK_CARD:
                        CardPos attacker = CardPos.valueOf((String)structuredData.get("ATTACKER"));
                        CardPos defender = CardPos.valueOf((String)structuredData.get("TARGET"));
                        Double playerD = (Double)structuredData.get("PLAYER");
                        int player = playerD.intValue();
                        int player2 = getOpposingPlayer(player);
                        attack(player,player2,attacker,invertCardPos(defender));
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handle(CommandType command, HashMap structuredData) {
        NetworkingEvent networkingEvent = new NetworkingEvent();
        networkingEvent.setCommand(structuredData);
        networkingEvent.setCommandType(command);
        toHandle.add(networkingEvent);
    }

    /**
     * Called when a user accepts their starting hand
     * @param command
     * @param structuredData
     */
    private static void handleGameStartAccept(CommandType command, HashMap structuredData) throws IOException {
        System.out.println("Handled game start accept");
        Integer userID = (Integer) structuredData.get("USER_ID");
        if (structuredData.containsKey("USER_ID")) {
            ready.replace(userID, true);
        }
        System.out.println("Decks being initialised");
        HashMap<Object,Object> data = new HashMap<>();
        data.put("COMMAND",CommandType.GAME_START);
        data.put("PLAYER_ID",userID);

        HashMap<CardPos, Integer> myDeck = boards.get(userID).getDeck();
        data.put("DECK", myDeck);
        int client1 = clients.get(0);
        int client2 = clients.get(1);
        String name1 = ClientTable.getUsers().get(client1).getName();
        String name2 = ClientTable.getUsers().get(client2).getName();
        data.put("MY_NAME",name1);
        data.put("THEIR_NAME",name2);

        System.out.println("DECKS SENT TO SPAWN");
        ClientTable.sendMessageToUser(userID,data);
        data.clear();
        //boards.get(userID).newShuffle();
        //System.out.println("Calling Shuffle");

        int myCastle = boards.get(userID).get().get(CardPos.MY_CASTLE).getId();
        int theirCastle = boards.get(userID == player ? player2 : player).get().get(CardPos.MY_CASTLE).getId();
        data.put("COMMAND",CommandType.SPAWN_CASTLES);
        data.put("MY_CASTLE",myCastle);
        data.put("THEIR_CASTLE",theirCastle);

        ClientTable.sendMessageToUser(userID,data);
        data.clear();
        data.put("COMMAND", CommandType.SPAWN_CASTLES);
        data.put("MY_CASTLE",theirCastle);
        data.put("THEIR_CASTLE",myCastle);
        ClientTable.exclusiveBroadcast(userID == player ? player2 : player,data);
        data.clear();
        data.put("COMMAND",CommandType.GAME_START);
        HashMap<CardPos,Integer> myDeckForOpponent = new HashMap<>();
        boolean deckDone = false;
        CardPos cp = CardPos.MY_DECK_1;
        while(!deckDone){
            if(!myDeck.containsKey(cp)){
                deckDone = true;
            }
            else{
                myDeckForOpponent.put(invertCardPos(cp),myDeck.get(cp));
            }
            cp = cp.next();
        }
        data.put("MY_NAME",name2);
        data.put("THEIR_NAME",name1);
        data.put("DECK",myDeckForOpponent);
        ClientTable.exclusiveBroadcast(userID,data);
        triggerGameStart();
    }

    /**
     * Handles the playing of a card
     * @param s The command type enum
     * @param t All parsed data sent from client
     */
    private void handlePlayCard(CommandType s, HashMap<Object, Object> t){
        // Check we've got all the required params
        if(t.containsKey("USER_ID")&& t.containsKey("START_POS") && t.containsKey("END_POS") && t.containsKey("CARD_ID") && t.containsKey("ABILITY_TRIGGERED")){
            int userid = ((Integer) t.get("USER_ID"));
            int cardid = ((Double) t.get("CARD_ID")).intValue();
            CardPos sPos = CardPos.valueOf((String) t.get("START_POS"));
            CardPos ePos = CardPos.valueOf((String) t.get("END_POS"));
            playCard(userid,cardid,sPos, ePos,(Boolean)t.get("ABILITY_TRIGGERED"));
        }
    }
    /**
     * Called when a user wishes to perform a mulligan
     * @param command
     * @param structuredData
     */
    private static void handleGameStartMulligan(CommandType command, HashMap structuredData) {
        // We need to loop through their cards and return them to the deck
        if (structuredData.containsKey("USER_ID")) {
            int userid = (int) structuredData.get("USER_ID");
            Board board = boards.get(userid);
            board.returnCardsToDeck(); // returns all cards in hand to deck and sends relevant message to the clients
            board.shuffle();
            ArrayList<CardDraw> drawnCards = drawCards(5, userid);
            HashMap<CardPos, CardPos> movements = new HashMap<>();

            for (CardDraw cardDraw : drawnCards) {
                movements.put(cardDraw.getOldPos(), cardDraw.getCardPos());
            }

            sendMovements(userid, movements);
        }

        triggerGameStart();
    }

    private static void sendMovements(int userid, HashMap<CardPos, CardPos> movements) {
        HashMap<Object, Object> toSend = new HashMap<>();
        toSend.put("COMMAND", CommandType.MOVE_CARD);
        toSend.put("CARD_MOVEMENTS", movements);
        try {
            ClientTable.sendMessageToUser(userid, toSend);

            HashMap<CardPos, CardPos> inverted = new HashMap<>();
            for (Map.Entry<CardPos, CardPos> entry : movements.entrySet()) {
                inverted.put(invertCardPos(entry.getKey()), invertCardPos(entry.getValue()));
            }
            toSend.replace("CARD_MOVEMENTS", inverted);
            ClientTable.exclusiveBroadcast(userid, toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param command
     * @param data
     */
    private void handleActivateTrickster(CommandType command, HashMap<Object, Object> data) {
        if (data.containsKey("TARGET") && data.containsKey("USER_ID")) {
            int userid = (int) data.get("USER_ID");
            CardPos target = CardPos.valueOf((String) data.get("TARGET"));

            Board board = boards.get(userid);
            board.activateTrickster(target);
            boards.replace(userid, board);
        }
    }

    /**
     * Moves to the next turn
     */
    private void nextTurn() throws IOException {
        // This can be improved for 4 players
        if (currentPlayer == player) {
            currentPlayer = player2;
        } else {
            currentPlayer = player;
        }
        boards.get(currentPlayer).processNextTurn();
        HashMap<Object, Object> data = new HashMap<>();
        data.put("COMMAND", CommandType.TURN_START);
        System.out.println("TURN START SENT");
        data.put("PLAYER_ID", currentPlayer);
        HashMap<Object, Object> toSend = new HashMap<>();
        //ClientTable.sendMessageToUser(currentPlayer,data);
        toSend.clear();
        data.clear();

        data.put("COMMAND", CommandType.TURN_START);
        System.out.println("TURN START SENT");
        data.put("PLAYER_ID", currentPlayer);
        toSend = new HashMap<>();
        //ClientTable.exclusiveBroadcast(currentPlayer,data);
        data.clear();

        ArrayList<CardDraw> draws = drawCards(1, currentPlayer);
        ArrayList<HashMap<Object, Object>> processed = preprocess(draws);
        toSend = new HashMap<>();
        toSend.put("COMMAND", CommandType.DRAW_CARD_RESP);
        toSend.put("DRAWN_CARDS", processed);
        toSend.put("USER_ID", currentPlayer);
        ClientTable.sendMessageToUser(currentPlayer, toSend);



        ArrayList<CardDraw> inverted = invertCardSet(draws);
        processed = preprocess(inverted);
        toSend = new HashMap<>();
        toSend.put("COMMAND", CommandType.DRAW_CARD_RESP);
        toSend.put("DRAWN_CARDS", processed);
        toSend.put("USER_ID", currentPlayer);
        ClientTable.exclusiveBroadcast(currentPlayer, toSend);
        boards.get(currentPlayer).setLocked(false);
        toSend.clear();
        data.clear();

        data = new HashMap<>();
        data.put("COMMAND", CommandType.TURN_START);
        data.put("PLAYER_ID",currentPlayer);

        data.put("USERNAME",ClientTable.getUsers().get(currentPlayer).getUserName());
        try {
            ClientTable.broadcastMessage(data);
        } catch (IOException e) {
            //
        }
    }

    /**
     * Converts list of CardDraw to hashmap that can be turned into json
     * @param drawnCards
     * @return
     */
    private static ArrayList<HashMap<Object, Object>> preprocess(ArrayList<CardDraw> drawnCards) {
        ArrayList<HashMap<Object, Object>> drawn = new ArrayList<>();

        for (CardDraw cardDraw : drawnCards) {
            HashMap<Object, Object> toAdd = new HashMap<>();
            toAdd.put("cardPos", cardDraw.getCardPos().toString());
            toAdd.put("oldPos", cardDraw.getOldPos().toString());
            toAdd.put("card", cardDraw.getCard().getId());
            drawn.add(toAdd);
        }

        return drawn;
    }

    /**
     * Called when a method needs to possibly trigger a game start
     */
    private static void triggerGameStart() {
        int count = 0;

        for (Map.Entry<Integer, Boolean> entry : ready.entrySet()) {
            if (entry.getValue())
                count++;
        }

        // If all clients are ready, send the TURN_START command
        if (count == clients.size()) {
            triggerTurnStart();
        }
    }

    /**
     * Called when a method needs to send out a turn start - ONLY FOR BEGINNING OF GAME
     */
    private static void triggerTurnStart() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("COMMAND", CommandType.TURN_START);
        System.out.println("TURN START SENT");
        data.put("PLAYER_ID", clients.get(0));
        currentPlayer = clients.get(0);
        boards.get(currentPlayer).setLocked(false);
        //try {
            //ClientTable.broadcastMessage(data);
        //} catch (IOException e) {
            //
        //}
    }

    /**
     * Draws a number of cards from a specified user's deck
     * @param i The number of cards to draw
     * @param userid The user's id
     * @return The drawn cards
     */
    private static ArrayList<CardDraw> drawCards(int i, int userid) {
        ArrayList<CardDraw> drawnCards = new ArrayList<>();
        Board board = boards.get(userid);

        for (int j = 0; j < i; j++) {
            CardDraw cardDraw = board.draw();
            drawnCards.add(cardDraw);
        }

        boards.replace(userid, board);
        return drawnCards;
    }

    /**
     * Called when a player ends their turn
     * @param playerid The id of the player that ended their turn
     */
    private static void onTurnEnd(int playerid) {
        Board board = boards.get(playerid);
        board.setMagePlayed(false);
        board.setSoldierPlayed(false);
        boards.replace(playerid, board);
        Board b = boards.get(playerid);
        if(b.get().containsKey(CardPos.MY_SOLDIER_1)){
            b.get().get(CardPos.MY_SOLDIER_1).setAttacked(false);
        }
        if(b.get().containsKey(CardPos.MY_SOLDIER_2)){
            b.get().get(CardPos.MY_SOLDIER_2).setAttacked(false);
        }


    }

    /**
     * Inverts a whole set of card positions
     * @param cardSet
     * @return
     */
    private static ArrayList<CardDraw> invertCardSet(ArrayList<CardDraw> cardSet) {
        for (int i = 0; i < cardSet.size(); i++) {
            CardDraw cardDraw = cardSet.get(i);
            cardDraw.setCardPos(invertCardPos(cardDraw.getCardPos()));
            cardSet.set(i, cardDraw);
        }

        return cardSet;
    }

    /**
     * Inverts a card position (my -> theirs)
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
     * Converts from an integer to a card pos
     * @param intRep
     * @return
     */
    private static CardPos convertIntToCardPos(int intRep) {
        String rep = "MY_DECK_";
        rep = rep + Integer.toString(intRep);
        return CardPos.valueOf(rep);
    }

    /**
     * Converts from a CardPos to an integer
     * @param cardPos
     * @return
     */
    private static Integer convertCardPosToInt(CardPos cardPos) {
        String stringRep = cardPos.toString();
        String[] parts = stringRep.split("_");
        if (parts.length == 3) {
            return Integer.parseInt(parts[2]);
        } else {
            return null;
        }
    }

    public static boolean gameHasStarted() {
        return gameStarted;
    }

    /**
     * Starts the game, shuffles decks, sends cards
     */
    public static void gameStart() {
        gameStarted = true;

        // Loop through the users and register them as having connected
        ArrayList<User> users = ClientTable.getConfirmedUsers();
        int i = 0;
        for (User user : users) {
            if (i == 0)
                player = user.getUserId();
            else
                player2 = user.getUserId();
            clients.add(user.getUserId());
            ready.put(user.getUserId(), false);
            i++;
        }

        // Loop through decks and add them to their respective boards
        HashMap<Integer, ArrayList<Integer>> decks = ClientTable.getDecks();
        for (Map.Entry<Integer, ArrayList<Integer>> deck : decks.entrySet()) {
            Board board = new Board(deck.getValue());
            board.setPlayerID(deck.getKey());
            boards.put(deck.getKey(), board);
        }

        try {
            for (int j = 0; j < users.size(); j++) {
                User user = users.get(j);
                ArrayList<CardDraw> draws = drawCards(6, user.getUserId());
                ArrayList<HashMap<Object, Object>> processed = preprocess(draws);
                HashMap<Object, Object> toSend = new HashMap<>();
                toSend.put("COMMAND", CommandType.DRAW_CARD_RESP);
                toSend.put("DRAWN_CARDS", processed);
                toSend.put("USER_ID", user.getUserId());
                ClientTable.sendMessageToUser(user.getUserId(), toSend);

                ArrayList<CardDraw> inverted = invertCardSet(draws);
                processed = preprocess(inverted);
                toSend = new HashMap<>();
                toSend.put("COMMAND", CommandType.DRAW_CARD_RESP);
                toSend.put("DRAWN_CARDS", processed);
                toSend.put("USER_ID", user.getUserId());
                ClientTable.exclusiveBroadcast(user.getUserId(), toSend);

                toSend.clear();
                toSend.put("COMMAND",CommandType.DIAG_OFFER);
                toSend.put("MSG","Would you like to mulligan your hand?");
                ClientTable.sendMessageToUser(user.getUserId(),toSend);
                boards.get(user.getUserId()).setState(GameState.AWAIT_MULL);
                boards.get(user.getUserId()).setLocked(true);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean canAttack(Integer AttackingPlayer,Integer defendingPlayer, CardPos attacker ,CardPos defender){
        Board b1 = boards.get(AttackingPlayer);
        Board b2 = boards.get(defendingPlayer);
        HashMap<CardPos,Card> inPlay1 = b1.get();
        HashMap<CardPos,Card> inPlay2 = b2.get();
        boolean pawnWarrriorOnBoard = false;
        boolean centaurOnBoard = false;
        if(inPlay2.containsKey(CardPos.MY_SOLDIER_1)){
            if(inPlay2.get(CardPos.MY_SOLDIER_1).getId()==10){
                pawnWarrriorOnBoard = true;
            }
            if(inPlay2.get(CardPos.MY_SOLDIER_1).getId()==25){
                centaurOnBoard = true;
            }

        }
        else{
             if(inPlay2.containsKey(CardPos.MY_SOLDIER_2)){
                 if(inPlay2.get(CardPos.MY_SOLDIER_2).getId()==10){
                     pawnWarrriorOnBoard = true;
                  }
                 if(inPlay2.get(CardPos.MY_SOLDIER_2).getId()==25){
                     centaurOnBoard = true;
                 }
                }
            }
        if(inPlay1.containsKey(CardPos.MY_SOLDIER_1)){
            if(inPlay1.get(CardPos.MY_SOLDIER_1).getId()==25){
                centaurOnBoard = true;
            }
        }
        if(inPlay1.containsKey(CardPos.MY_SOLDIER_2)){
            if(inPlay1.get(CardPos.MY_SOLDIER_2).getId()==25) {
                centaurOnBoard = true;
            }
        }

        if(defender.toString().contains("THEIR")){
            System.out.println("Cannot attack your own cards");
            return false;
        }
        if(boards.get(AttackingPlayer).isLocked()){
            System.out.println("The Attacker is locked");
            return false;
        }
        if(AttackingPlayer != currentPlayer){
            System.out.println("Not the attackers turn");
            return false;
        }
        if(inPlay1.get(attacker).getStunned() > 0){
            System.out.println("attacker is stunned");
            return false;
        }

        System.out.println("ATTACKER IS " + attacker.toString());
        if (attacker == CardPos.MY_MAGE_1 || attacker == CardPos.MY_MAGE_2){
            if(inPlay1.get(attacker).getLevel()==3||centaurOnBoard){
                return true;
            }
            else{
                return false;
            }
        }

        if(pawnWarrriorOnBoard){
            if(inPlay2.get(defender).getId()== 10){
                System.out.println("Defender is pawn warrior");
                return true;
            }
            else{
                System.out.println("Defender should be pawn warrior but is + : "  + inPlay2.get(defender).getName() + "AT : " + defender);
                return false;
            }
        }

        if((inPlay1.get(attacker).getId() == 24) && (defender == CardPos.MY_CASTLE)){
            System.out.println("attacker is warlock");
            return true;
        }
        if (defender == CardPos.MY_CASTLE){
            if(inPlay2.containsKey(CardPos.MY_SOLDIER_1)||inPlay2.containsKey(CardPos.MY_SOLDIER_2)){
                System.out.println("Cannot attack past soldier : " + inPlay2.get(CardPos.MY_SOLDIER_1).getName());
                return false;
            }
            else{
                return true;
            }

            }
        if(defender == CardPos.MY_TRICKSTER_1||defender == CardPos.MY_TRICKSTER_2||defender == CardPos.THEIR_TRICKSTER_1||defender == CardPos.THEIR_TRICKSTER_2){
            return false;
        }
        return true;
    }
    public void attack(Integer AttackingPlayer,Integer defendingPlayer, CardPos attacker ,CardPos defender){
        if(AttackingPlayer != currentPlayer){
            System.out.println("RETURNING");
            return;
        }
        if(canAttack(AttackingPlayer, defendingPlayer,attacker,defender))
       {

           Board b1 = boards.get(AttackingPlayer);
           Board b2 = boards.get(defendingPlayer);

           Card attackingCard = b1.get().get(attacker);
           Card defendingCard = b2.get().get(defender);
           if(!attackingCard.hasAttacked()){

           }
           else
           {
               return;
           }

           if(invertCardPos(defender).toString().equals("THEIR_CASTLE")){
                if(b2.get().containsKey(CardPos.MY_SOLDIER_1)||b2.get().containsKey(CardPos.MY_SOLDIER_1)){
                    return;
                }
                else{
                    b2.damage(attackingCard.getPoints());
                    attackingCard.setAttacked(true);
                    return;
                }

           }
           if(attackingCard.getPoints() > defendingCard.getPoints()){
               System.out.println("FOR b1 attacker is : " + attacker);
               System.out.println("for b1 DEFENDER IS  :" + defender);
                b1.destroy(invertCardPos(defender),AttackingPlayer );
                b2.destroy(defender,defendingPlayer);

                boards.replace(AttackingPlayer,b1);
                boards.replace(defendingPlayer,b2);
                onDestroy(AttackingPlayer, attacker, defender);
               attackingCard.setAttacked(true);
               System.out.println("Attacking player destroys defending player");
           }
           else if(attackingCard.getPoints() == defendingCard.getPoints()){
               b1.destroy(attacker, AttackingPlayer);
               b2.destroy(defender, defendingPlayer);

               boards.replace(AttackingPlayer,b1);
               boards.replace(defendingPlayer,b2);
               onDestroy(AttackingPlayer, attacker, defender);
               onDestroy(defendingPlayer, defender, attacker);
               attackingCard.setAttacked(true);
               System.out.println("Both cards should be destroyed");
           }
           else if(b1.get().get(attacker).getId() == 24 && defender == CardPos.MY_CASTLE){
                b2.damage(5);
               attackingCard.setAttacked(true);
           }
           else{
               b2.destroy(invertCardPos(attacker), defendingPlayer);
               b1.destroy(attacker,AttackingPlayer);
               boards.replace(AttackingPlayer,b1);
               boards.replace(defendingPlayer,b2);
               onDestroy(defendingPlayer, defender, attacker);
               System.out.println(" defending player destroys attacking player");
           }
       }
       else{
            System.out.println("CANNOT ATTACK");
       }
    }

    /**
     * Called whenever a card has been destroyed
     * @param player
     * @param card1
     * @param target
     */
    private void onDestroy(Integer player, CardPos card1, CardPos target){
        if(target.toString().startsWith("MY")){
            if(currentPlayer == player){
                if(boards.get(player).get().get(CardPos.MY_CASTLE).getId() == 0 && !boards.get(player).get().get(CardPos.MY_CASTLE).isAbilityTriggered() && target.toString().contains("SOLDIER")){
                    Board board = boards.get(player);
                    board.drawCards(false);
                    boards.replace(player, board);
                }
            }
        }
    }

    /**
     * Handles the playing of a card by a user
     * @param playerID
     * @param CardId
     * @param start
     * @param end
     * @param isAbility
     */
    private void playCard(int playerID ,int CardId, CardPos start,CardPos end, boolean isAbility){
        try {
            if(playerID !=currentPlayer){
                return;
            }
            Board board = boards.get(playerID);
            int ouserid = getOpposingPlayer(playerID);
            Board enemyBoard = boards.get(ouserid);
            board.playCard(CardId, start,end,isAbility, playerID, enemyBoard);
            boards.replace(playerID, board);
            boards.replace(ouserid, enemyBoard);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the response from a user to a TARGET_CARD command
     * @param commandType
     * @param data
     */
    private void handleTargetCardResponse(CommandType commandType, HashMap<Object, Object> data) {
        // First, verify that the board is waiting for the required input
        Integer userid = (Integer) data.get("USER_ID");
        if (boards.get(userid).isWaitingForCost()) {
            if (data.containsKey("CARD_POS")) {
                CardPos cardPos = CardPos.valueOf((String) data.get("CARD_POS"));

                Board board = boards.get(userid);
                int ouserid = getOpposingPlayer(userid);
                Board enemyBoard = boards.get(ouserid);
                board.spendCost(cardPos, enemyBoard);
                boards.replace(userid, board);
                boards.replace(ouserid, enemyBoard);
            }
        } else if (boards.get(userid).isWaitingForOnPlay()) {
            if (data.containsKey("CARD_POS")) {
                Board board = boards.get(userid);
                int ouserid = getOpposingPlayer(userid);
                Board enemyBoard = boards.get(ouserid);
                CardPos cardPos = CardPos.valueOf((String) data.get("CARD_POS"));
                ArrayList<Object> returned = board.spendOnPlay(cardPos, enemyBoard);
                Boolean result = (Boolean) returned.get(0);
                enemyBoard = (Board) returned.get(0);

                if (result) { // Check if we need to process all the on-play actions
                    enemyBoard = board.processOnPlayActions(enemyBoard);
                }
                boards.replace(userid, board);
                boards.replace(ouserid, enemyBoard);
            }
        }
    }

    private int getOpposingPlayer(int userid) {
        return userid == player ? player2 : player;
    }
}
