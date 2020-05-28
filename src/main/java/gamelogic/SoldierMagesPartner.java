package gamelogic;

import networking.server.utils.OnPlayAction;
import utils.Cost;

import java.util.ArrayList;

public class SoldierMagesPartner extends Card
{
	public SoldierMagesPartner() {
		super();
		super.setId(27);
		super.setName("Soldier - Mages Partner");
		super.setImgPath("Deck_18.png");
		super.setDescription("In play: add a mage from your deck to your hand\nField: Mages cannot be targeted by abilities or attacks");
		super.setType(CardType.SOLDIER);
		super.setPoints(15);
		ArrayList<OnPlayAction>  actions = new ArrayList<>();
		actions.add(OnPlayAction.ADD_MAGE);
		super.setOnPlayWait(actions);
	}
}
