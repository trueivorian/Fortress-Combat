package gamelogic;

import networking.server.utils.OnPlayAction;

import java.util.ArrayList;

public class SoldierWarlock extends Card
{
	public SoldierWarlock() {
		super();
		super.setId(24);
		super.setName("Soldier - Warlock");
		super.setImgPath("Deck_15.png");
		super.setDescription("This card can attack the enemy castle directly.");
		super.setType(CardType.SOLDIER);
		super.setPoints(5);
	}
}