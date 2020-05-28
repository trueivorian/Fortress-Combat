package gamelogic;

import networking.server.utils.OnPlayAction;
import utils.Cost;

import java.util.ArrayList;

public class SoldierCannonmen extends Card
{
	public SoldierCannonmen() {
		super();
		super.setId(18);
		super.setName("Soldier - Cannonmen");
		super.setImgPath("Deck_09.png");
		super.setDescription("Cost: 1 soldier\nOn play: Deal 2 damage to an enemy castle\nIn play: draw a card");
		super.setType(CardType.SOLDIER);
		super.setPoints(25);
		super.setHasCost(true);
		ArrayList<ArrayList<Cost>> fullCostList = new ArrayList<>();
		ArrayList<Cost> costList = new ArrayList<>();
		Cost cost = new Cost(CardType.SOLDIER,1);
		costList.add(cost);
		fullCostList.add(costList);
		super.setCosts(fullCostList);

		ArrayList<OnPlayAction> actions = new ArrayList<>();
		actions.add(OnPlayAction.DEAL_CASTLEDAMAGE_2);
		super.setOnPlayWait(actions); // Register on play actions
	
	}

	/**
	 * SERVERSIDE
	 */
	@Override
	public void onPlay() {

	}
}