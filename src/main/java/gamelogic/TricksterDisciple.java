package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterDisciple extends Card
{
	public TricksterDisciple() {
		super();
		super.setId(37);
		super.setName("Trickster - Disciple");
		super.setImgPath("TricksterCards_10.png");
		super.setDescription("Draw cards equal to the combined levels of your enemy's mages");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setAction(TricksterAbility.Action.DRAW_EQUAL_CARDS_ENEMY_MAGE_LEVELS);
		ability.setTrigger(TricksterAbility.Trigger.ANYTHING);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
