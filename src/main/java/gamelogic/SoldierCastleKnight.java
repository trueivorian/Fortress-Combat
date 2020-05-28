package gamelogic;

import networking.server.utils.OnPlayAction;

import java.util.ArrayList;

public class SoldierCastleKnight extends Card
{
	public SoldierCastleKnight() {
		super();
		super.setId(21);
		super.setName("Soldier - Castle Knight");
		super.setImgPath("Deck_12.png");
		super.setDescription("On play: play a soldier from your hand");
		super.setType(CardType.SOLDIER);

		ArrayList<OnPlayAction> actions = new ArrayList<>();
		actions.add(OnPlayAction.PLAY_SOLDIER);
		super.setOnPlayWait(actions);
	}
}
