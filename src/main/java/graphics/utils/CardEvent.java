package graphics.utils;

import gamelogic.CardType;
import utils.CardPos;
import utils.exceptions.CardEventException;

import java.util.ArrayList;
import java.util.HashMap;

import gamelogic.Card;

/**
 * Represents an event on the board
 */
public class CardEvent {
	private Card card;
	private EventType eventType;
	private ArrayList<CardType> cardTypeList;
	private CardPos startPos;
	private CardPos endPos;
	private HashMap<CardPos, CardPos> moveList;
	private ArrayList<CardPos> boardRegions;
	private String msg = "";
	private int value;
	private boolean flag;

	/**
	 * The default constructor 
	 * @param _card
	 * @param _startPos
	 * @param _endPos
	 * @param _eventType
	 */
	public CardEvent(Card _card, CardPos _startPos, CardPos _endPos, EventType _eventType) {
		card = _card;
		startPos = _startPos;
		endPos = _endPos;
		eventType = _eventType;
	}
	
	/**
	 * Card spawning event
	 * @param _card
	 */
	public CardEvent(Card _card, CardPos _endPos) {
		this(_card, _endPos, _endPos, EventType.SPAWN_CARD);
	}
	
	/**
	 * Constructor for the card flipping or spawning events
	 * @param _card
	 * @param _endPos
	 * @param _event
	 * @throws CardEventException 
	 */
	public CardEvent(Card _card, CardPos _endPos, EventType _event) throws CardEventException {
		this(_card, _endPos, _endPos, _event);
		if((_event!=EventType.SPAWN_CARD)||(_event!=EventType.FLIP_CARD)){
			throw new CardEventException();
		}
	}
	
	/**
	 * Card moving event
	 * @param _card
	 * @param _startPos
	 * @param _endPos
	 */
	public CardEvent(Card _card, CardPos _startPos, CardPos _endPos) {
		this(_card, _startPos, _endPos, EventType.MOVE_CARD);
	}
	
	/**
	 * Card target event
	 * @param _startPos
	 */
	public CardEvent(CardPos _startPos, ArrayList<CardPos> _boardRegions) { // call target when in board regions
		this(null, _startPos, _startPos, EventType.TARGET_CARD);
		boardRegions = _boardRegions;
	}

	public CardEvent(ArrayList<CardType> _cardTypeList){
		cardTypeList = _cardTypeList;
		eventType =  EventType.GET_DECK_CARD;
	}

	public CardEvent(HashMap<CardPos,CardPos> _moveList){
		moveList = _moveList;
		eventType = EventType.MOVE_MULTIPLE;
	}

	public CardEvent(String _msg, EventType _eventType) {
		msg = _msg;
		eventType = _eventType;
	}

	public CardEvent(int _value, EventType _eventType){
		value = _value;
		eventType = _eventType;
	}

	public CardEvent(boolean _flag, EventType _eventType){
		flag = _flag;
		eventType = _eventType;
	}

	public boolean isSpawning() {
		return eventType.equals(EventType.SPAWN_CARD);
	}
	
	public boolean isMoving() {
		return eventType.equals(EventType.MOVE_CARD);
	}
	
	public boolean isFlipping() {
		return eventType.equals(EventType.FLIP_CARD);
	}
	
	public boolean isTarget() {
		return eventType.equals(EventType.TARGET_CARD);
	}

	public EventType getEventType() {
		return eventType;
	}

	public CardPos getStartPos() {
		return startPos;
	}

	public CardPos getEndPos() {
		return endPos;
	}
	
	public Card getCard() {
		return card;
	}

	public ArrayList<CardPos> getValidBoardRegions() {
		return boardRegions;
	}

	public HashMap<CardPos, CardPos> getMoveList(){ return moveList; }

	public ArrayList<CardType> getCardTypeList(){ return cardTypeList; }

	public String getMsg(){
		return msg;
	}

	public int getValue(){ return value; }

	public boolean getFlag(){return flag; }
}