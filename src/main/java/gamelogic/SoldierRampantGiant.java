package gamelogic;

import networking.CommandType;
import networking.client.GameClient;
import utils.CardPos;
import networking.server.utils.OnPlayAction;
import utils.Cost;

import java.util.ArrayList;
import java.util.HashMap;

public class SoldierRampantGiant extends Card
{
	public SoldierRampantGiant() {
		super();
		super.setId(12);
		super.setName("Soldier - Rampant Giant");
		super.setImgPath("Deck_03.png");
		super.setDescription("Cost: 15 Castle Health, 1 Soldier. On play: Destroy an enemy card of your choice");
		super.setType(CardType.SOLDIER);
		super.setPoints(25);

		ArrayList<ArrayList<Cost>> masterCosts = new ArrayList<>();
		ArrayList<Cost> costs = new ArrayList<>();
		costs.add(new Cost(CardType.CASTLE, 15)); // 15 castle health
		costs.add(new Cost(CardType.SOLDIER, 1));
		masterCosts.add(costs);
		super.setCosts(masterCosts);
		ArrayList<OnPlayAction>  actions = new ArrayList<>();
		actions.add(OnPlayAction.DESTROY_ENEMY_CARD);
		super.setOnPlayWait(actions);
	}


	/**
	 * SERVERSIDE
	 */
	public void onPlay(CardPos card1, CardPos cardPos2)
	{
		HashMap<Object, Object> data = new HashMap<>();
		data.put("COMMAND", CommandType.TARGET_CARD_RESP);
		data.put("CARD_POS", cardPos2);
		data.put("TRIGGER_POS", card1);
		data.put("ABILITY_TRIGGERED", true);
		GameClient.sendMessage(data);
	}
}
