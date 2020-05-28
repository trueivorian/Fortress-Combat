package gamelogic;

import utils.Cost;

import java.util.ArrayList;

public class SoldierCavalry extends Card
{
	public SoldierCavalry() {
		super();
		super.setId(17);
		super.setName("Soldier - Cavalry");
		super.setImgPath("Deck_08.png");
		super.setDescription("Cost: 1 mage\nIn play: you can add a trickster from your deck to your hand");
		super.setType(CardType.SOLDIER);
		super.setPoints(25);

		ArrayList<ArrayList<Cost>> masterCosts = new ArrayList<>();
		ArrayList<Cost> costs = new ArrayList<>();
		costs.add(new Cost(CardType.MAGE, 1));
		masterCosts.add(costs);
		super.setCosts(masterCosts);
	}
}