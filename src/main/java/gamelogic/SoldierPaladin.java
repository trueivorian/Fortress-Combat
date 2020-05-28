package gamelogic;

import networking.server.utils.OnPlayAction;
import utils.Cost;

import java.util.ArrayList;

public class SoldierPaladin extends Card
{
	public SoldierPaladin() {
		super();
		super.setId(14);
		super.setName("Soldier - Paladin");
		super.setImgPath("Deck_05.png");
		super.setDescription("Cost: 1 soldiers, 1 mage, 1 trickster\nOn play: You can add any card from your deck to your hand\nIn play: Heal 5 castle health");
		super.setType(CardType.SOLDIER);
		super.setPoints(30);

		ArrayList<ArrayList<Cost>> masterCosts = new ArrayList<>();
		ArrayList<Cost> costs = new ArrayList<>();
		costs.add(new Cost(CardType.SOLDIER, 1));
		costs.add(new Cost(CardType.MAGE, 1));
		costs.add(new Cost(CardType.TRICKSTER, 1));
		masterCosts.add(costs);
		super.setCosts(masterCosts);
		ArrayList<OnPlayAction>  actions = new ArrayList<>();
		actions.add(OnPlayAction.ADD_ANY_CARD);
		//actions.add() in play
		super.setOnPlayWait(actions);
	}
}