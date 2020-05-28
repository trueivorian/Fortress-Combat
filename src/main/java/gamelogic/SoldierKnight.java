package gamelogic;

import networking.server.utils.OnPlayAction;
import utils.Cost;

import java.util.ArrayList;

public class SoldierKnight extends Card
{
	public SoldierKnight() {
		super();
		super.setId(22);
		super.setName("Soldier - Knight");
		super.setImgPath("Deck_13.png");
		super.setDescription("Cost: 1 squire\nOn play: draw 1 card and destroy 1 enemy card\nIn play: cancel an enemy trickster");
		super.setType(CardType.SOLDIER);
		super.setPoints(25);

		ArrayList<ArrayList<Cost>> masterCosts = new ArrayList<>();
		ArrayList<Cost> costs = new ArrayList<>();
		Cost cost = new Cost(CardType.SOLDIER, 1);
		cost.setId(11);
		masterCosts.add(costs);
		super.setCosts(masterCosts);

		ArrayList<OnPlayAction> actions = new ArrayList<>();
		actions.add(OnPlayAction.DRAW_CARDS_1);
		actions.add(OnPlayAction.DESTROY_ENEMY_CARD);
		super.setOnPlayWait(actions);
	}
}