package gamelogic;

import networking.server.utils.OnPlayAction;

import java.util.ArrayList;

public class SoldierSquire extends Card
{
	public SoldierSquire() {
		super();
		super.setId(11);
		super.setName("Soldier - Squire");
		super.setImgPath("Deck_02.png");
		super.setDescription("On play: Add one soldier card from the deck to your hand");
		super.setType(CardType.SOLDIER);
		super.setPoints(15);

		ArrayList<OnPlayAction> actions = new ArrayList<>();
		actions.add(OnPlayAction.ADD_SOLDIER);
		super.setOnPlayWait(actions);
	}
}
