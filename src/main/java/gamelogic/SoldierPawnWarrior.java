package gamelogic;

import networking.server.utils.OnPlayAction;

import java.util.ArrayList;

public class SoldierPawnWarrior extends Card
{
	public SoldierPawnWarrior() {
		super();
		super.setId(10);
		super.setName("Soldier - Pawn Warrior");
		super.setImgPath("Deck_01.png");
		super.setDescription("On play: Draw 2 cards");
		super.setType(CardType.SOLDIER);
		super.setPoints(10);
		super.setHasCost(false);
		ArrayList<OnPlayAction>  actions = new ArrayList<>();
		actions.add(OnPlayAction.DRAW_CARDS_2);
		super.setOnPlayWait(actions);
	}


}
