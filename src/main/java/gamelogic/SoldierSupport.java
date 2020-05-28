package gamelogic;

import networking.server.utils.OnPlayAction;

import java.util.ArrayList;

public class SoldierSupport extends Card
{
	public SoldierSupport() {
		super();
		super.setId(13);
		super.setName("Soldier - Support");
		super.setImgPath("Deck_04.png");
		super.setDescription("On play: play a mage from your deck but cancel its on play ability");
		super.setType(CardType.SOLDIER);
		super.setPoints(15);

		ArrayList<OnPlayAction> actions = new ArrayList<>();
		actions.add(OnPlayAction.PLAY_MAGE_CANCEL_ABILITY);
		super.setOnPlayWait(actions);
	}
}