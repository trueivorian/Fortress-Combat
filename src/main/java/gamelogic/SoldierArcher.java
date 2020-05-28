package gamelogic;

import graphics.UIController;
import graphics.utils.CardEvent;
import graphics.utils.EventType;
import networking.CommandType;
import networking.client.GameClient;
import networking.server.utils.OnPlayAction;
import utils.BoardRegion;
import utils.CardPos;
import utils.Cost;

import java.util.ArrayList;
import java.util.HashMap;

public class SoldierArcher extends Card
{
    public SoldierArcher() {
    	super();
		super.setId(16);
		super.setName("Soldier - Archer");
		super.setImgPath("Deck_07.png");
		super.setDescription("Cost: 1 trickster\nOn play: Destroy an enemy soldier");
		super.setType(CardType.SOLDIER);
		super.setPoints(20);
		super.setHasCost(true);
		ArrayList<ArrayList<Cost>> fullCostList = new ArrayList<>();
		ArrayList<Cost> costList = new ArrayList<>();
		Cost cost = new Cost(CardType.TRICKSTER,1);
		costList.add(cost);
		fullCostList.add(costList);
		ArrayList<OnPlayAction>  actions = new ArrayList<>();
		actions.add(OnPlayAction.DESTROY_ENEMY_CARD);
		super.setOnPlayWait(actions);

//		super.setCosts(fullCostList);
    }
}