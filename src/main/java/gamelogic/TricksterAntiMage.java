package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterAntiMage extends Card
{
	public TricksterAntiMage() {
		super();
		super.setId(30);
		super.setName("Trickster - Anti Mage");
		super.setImgPath("TricksterCards_03.png");
		super.setDescription("Destroy an enemy mage");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setAction(TricksterAbility.Action.DESTROY_ENEMY_MAGE);
		ability.setTrigger(TricksterAbility.Trigger.ANYTHING);
		super.setTricksterAbilities(abilities);
	}
}
