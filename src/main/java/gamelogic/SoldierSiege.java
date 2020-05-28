package gamelogic;

import networking.server.utils.OnPlayAction;

import java.util.ArrayList;

public class SoldierSiege extends Card
{
	public SoldierSiege() {
		super();
		super.setId(19);
		super.setName("Soldier - Siege");
		super.setImgPath("Deck_10.png");
		super.setDescription("On play: Play at no cost a soldier from the deck but cancel its on-play ability");
		super.setType(CardType.SOLDIER);
		super.setPoints(10);

		ArrayList<OnPlayAction> actions = new ArrayList<>();
		actions.add(OnPlayAction.PLAY_FREE_SOLDIER_CANCEL_ABILITY);
		super.setOnPlayWait(actions);
	}
}