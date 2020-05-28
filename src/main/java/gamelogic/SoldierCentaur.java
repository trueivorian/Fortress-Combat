package gamelogic;

import utils.Cost;

import java.util.ArrayList;

public class SoldierCentaur extends Card
{
	public SoldierCentaur() {
		super();
		super.setId(25);
		super.setName("Soldier - Centaur");
		super.setImgPath("Deck_16.png");
		super.setDescription("Cost: 1 soldier, 1 trickster and 1 mage\nOn play: add a mage from your deck to your hand\nField: mages can attack at any level");
		super.setType(CardType.SOLDIER);
		super.setPoints(25);

		ArrayList<ArrayList<Cost>> masterCosts = new ArrayList<>();
		ArrayList<Cost> costs = new ArrayList<>();
		costs.add(new Cost(CardType.SOLDIER, 1));
		costs.add(new Cost(CardType.TRICKSTER, 1));
		costs.add(new Cost(CardType.MAGE, 1));
		super.setCosts(masterCosts);
	}
}