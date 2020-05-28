package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterTheWanderer extends Card
{
	public TricksterTheWanderer() {
		super();
		super.setId(31);
		super.setName("Trickster - The Wanderer");
		super.setImgPath("TricksterCards_04.png");
		super.setDescription("Look at all of your enemy tricksters and choose one to destroy");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setTrigger(TricksterAbility.Trigger.ANYTHING);
		ability.setAction(TricksterAbility.Action.CHOOSE_DESTROY_TRICKSTER);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
