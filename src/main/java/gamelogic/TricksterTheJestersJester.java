package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterTheJestersJester extends Card
{
	public TricksterTheJestersJester() {
		super();
		super.setId(28);
		super.setName("Trickster - The Jester's Jester");
		super.setImgPath("TricksterCards_01.png");
		super.setDescription("Special: Cancel an enemy Trickster");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setTrigger(TricksterAbility.Trigger.ANYTHING);
		ability.setSpecial(true);
		ability.setAction(TricksterAbility.Action.CANCEL_ENEMY_TRICKSTER);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
