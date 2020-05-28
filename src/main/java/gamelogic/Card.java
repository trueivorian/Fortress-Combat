package gamelogic;

import javafx.event.EventHandler;
import networking.CommandType;
import networking.server.Board;
import networking.client.GameClient;
import networking.server.utils.MageAbility;
import networking.server.utils.OnPlayAction;
import networking.server.utils.TricksterAbility;
import utils.CardPos;
import utils.Cost;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Abstract card class
 *
 * No cost by default
 */
public abstract class Card {
	protected int id = 10;
	protected int uid;
	protected String name;
	protected String imgName;
	protected String description;
	protected CardType type;
	protected int points;
	protected int level;
	protected int stunned = 0; // 2 = just stunned, 0 = not stunned
	protected ArrayList<OnPlayAction> onPlayWait = new ArrayList<>();
	protected ArrayList<ArrayList<Cost>> costs;
	private static int currentUid = 0;
	protected boolean hasCost;
	ArrayList<MageAbility> mageAbilities = new ArrayList<>();
	ArrayList<TricksterAbility> tricksterAbilities = new ArrayList<>();
	private EventHandler cardEventHandler;
    protected boolean attacked = false;

    public boolean hasAttacked() {
        return attacked;
    }

    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }



	public boolean hasInPlay() {
		return inPlay;
	}

	public void setInPlay(boolean inPlay) {
		this.inPlay = inPlay;
	}

	protected boolean inPlay;

	public ArrayList<TricksterAbility> getTricksterAbilities() {
		return tricksterAbilities;
	}

	public void setTricksterAbilities(ArrayList<TricksterAbility> tricksterAbilities) {
		this.tricksterAbilities = tricksterAbilities;
	}

	public boolean isHasCost() {
		return hasCost || costs.size() > 0;
	}

	public void setHasCost(boolean hasCost) {
		this.hasCost = hasCost;
	}

	public ArrayList<MageAbility> getMageAbilities() {
		return mageAbilities;
	}
	public void setMageAbilities (ArrayList<MageAbility> m){
		mageAbilities = m;
	}
	public Card() {
		setUid(currentUid);
		currentUid++;
		costs = new ArrayList<>();
		setHasCost(false);
	}

	public ArrayList<ArrayList<Cost>> getCosts() {
		return costs;
	}

	public void setCosts(ArrayList<ArrayList<Cost>> costs) {
		this.costs = costs;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getStunned() {
	    return stunned;
    }

	public void setStunned(boolean stunned) {
	    this.stunned = 2;
	}

	public boolean isAbilityTriggered() {
		return abilityTriggered;
	}

	public EventHandler getCardEventHandler() {
		return cardEventHandler;
	}

	public void setCardEventHandler(EventHandler _cardEventHandler){
		cardEventHandler = _cardEventHandler;
	}

	public void setAbilityTriggered(boolean abilityTriggered) {
		this.abilityTriggered = abilityTriggered;
	}

	protected boolean abilityTriggered = false;



	public void destroy(CardPos cardPos) {}

	/**
	 * Sends a play message from the client to the server
	 * @param sPos Start position
	 * @param ePos End position
	 * @param cardID Card ID
	 * @param abilityTriggered Whether or not this was triggered by an ability
	 */
	public void play(CardPos sPos, CardPos ePos, int cardID, boolean abilityTriggered) {
    	HashMap<Object, Object> data = new HashMap<>();
    	data.put("COMMAND", CommandType.PLAY_CARD);
    	data.put("START_POS", sPos);
    	data.put("END_POS", ePos);
    	data.put("CARD_ID", cardID);
    	data.put("ABILITY_TRIGGERED", abilityTriggered);
		GameClient.sendMessage(data);
	}

	public void draw() {}


	/* Begin events */
	public void onPlay() {}

	public Board processOnPlay(CardPos cardPos, Board board) {
	    // Do something
        return board;
    }

	public void onPlay(CardPos cardPos) {}

	public void onPlay(CardPos cardPos1, CardPos cardPos2) {}

	public void inPlay() {}

	public void onLevel() {}

	public void onDefeat() {}

    public void add(CardPos cardToAdd){
        
    }
	/* Begin getters and setters */

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgPath() {
		return getClass().getResource("/images/textures/" + imgName).toExternalForm(); //(filePath + "\\resources\\images\\textures\\" + imgName).replace("\\", File.separator);
	}

	public String getImgBackPath() {
		return getClass().getResource("/images/textures/Back.png").toExternalForm();
	}

	public void setImgPath(String name) {
		this.imgName = name;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public CardType getType() {
		return type;
	}

	public void setType(CardType type) {
		this.type = type;
	}

	public boolean hasLeveled() {return false;};

	public String toString() {
		return String.valueOf(id);
	}

    public void setOnPlayWait(ArrayList<OnPlayAction> a) {
	    onPlayWait = a;
    }

    public ArrayList<OnPlayAction> getOnPlayWait() {
	    return onPlayWait;
    }

    public void processTurn() {
	    if (stunned > 0)
	        stunned--;
    }
}