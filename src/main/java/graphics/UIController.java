package graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import gamelogic.Card;
import gamelogic.CastleCalterburryFortress;
import graphics.exceptions.InvalidCardPosException;
import graphics.utils.CardEvent;
import graphics.utils.EventType;
import networking.CommandType;
import networking.client.ClientLogic;
import networking.client.GameClient;
import networking.exceptions.CardLogicException;
import networking.exceptions.ServerNotFoundException;
import utils.CardPos;

/**
 * 
 * @author Osanne Gbayere
 * The UI-to-network interface
 * card events are pushed onto the queue from the network
 * and popped from the queue in the game client via the UI
 *
 */
public class UIController {
	private static BlockingQueue<CardEvent> cardEventQueue = new LinkedBlockingQueue<CardEvent>();
	private static ClientLogic clientLogic = new ClientLogic();
	
	/**
	 * 
	 * @param event An event to be added to the queue
	 */
	public static synchronized void pushEvent(CardEvent event) {
		try {
			cardEventQueue.put(event);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return Returns the event removed from the queue
	 */
	public static CardEvent popEvent() {
		try {
			CardEvent event = cardEventQueue.take();
			clientLogic.update(event);
			return event;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void attack(CardPos p1, CardPos p2) throws CardLogicException {
		if(clientLogic.canAttack(p1, p2)) {
			clientLogic.attack(p1, p2);
		}
	}
	
	public static void activateTrickster(CardPos p1) throws CardLogicException {
		if(clientLogic.canActivateTrickster(p1)) {
			clientLogic.activateTrickster(p1);
		}
	}
	
	public static void levelMage(CardPos p1) throws CardLogicException {
		if(clientLogic.canLevel(p1)) {
			clientLogic.levelMage(p1);
		}
	}
	
	public static void play(CardPos p1, CardPos p2) throws CardLogicException, InvalidCardPosException {
		if(clientLogic.canPlay(p1,p2)) {
			clientLogic.play(p1,p2);
		}
	}

	public static void endTurn(){
		clientLogic.endTurn();
	}

	public static void option(boolean choice){
		clientLogic.option(choice);
	}
	
	public static void target(CardPos p1, CardPos p2) {
		try {
			clientLogic.target(p1, p2);
		} catch (CardLogicException e) {
			e.printStackTrace();
		} catch (InvalidCardPosException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a JOIN_GAME command to the server to register this client
	 * @param username The username to be used
	 * @param deckData The deck to be used for this game
	 */
	public static void joinGame(String username, ArrayList<Integer> deckData) throws ServerNotFoundException
	{
		GameClient gameClient=new GameClient(username,"localhost");


		HashMap<Object, Object> data = new HashMap<>();
		data.put("COMMAND", CommandType.JOIN_GAME);
		data.put("USERNAME", username);
		data.put("DECK_DATA", deckData);
		GameClient.sendMessage(data);
	}

	public static void inPlay(CardPos pos){
		clientLogic.inPlay(pos);
	}

	public static void update(CardEvent event){
		clientLogic.update(event);
	}

	public static void getDeckCard(CardPos pos){
		clientLogic.getDeckCard(pos);
	}



}
