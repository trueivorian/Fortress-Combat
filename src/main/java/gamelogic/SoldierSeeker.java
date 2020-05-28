package gamelogic;

import networking.server.utils.OnPlayAction;

import java.util.ArrayList;

public class SoldierSeeker extends Card
{
	public SoldierSeeker() {
		super();
		super.setId(15);
		super.setName("Soldier - Seeker");
		super.setImgPath("Deck_06.png");
		super.setDescription("On play: destroy an enemy trickster");
		super.setType(CardType.SOLDIER);
		super.setPoints(10);

		ArrayList<OnPlayAction> actions = new ArrayList<>();
		actions.add(OnPlayAction.DESTROY_ENEMY_TRICKSTER);
		super.setOnPlayWait(actions);

	}
}
