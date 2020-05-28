package gamelogic;

import networking.server.utils.OnPlayAction;

import java.util.ArrayList;

public class SoldierSacrfice extends Card
{
	public SoldierSacrfice() {
		super();
		super.setId(20);
		super.setName("Soldier - Sacrifice");
		super.setImgPath("Deck_11.png");
		super.setDescription("In play: Destroy this card and draw 3 cards.");
		super.setType(CardType.SOLDIER);
		super.setPoints(15);
		ArrayList<OnPlayAction> actions = new ArrayList<>();
		actions.add(OnPlayAction.DESTROY_SELF);
		actions.add(OnPlayAction.DRAW_CARDS_2);
		actions.add(OnPlayAction.DRAW_CARDS_1);
		super.setOnPlayWait(actions);
	}
}
