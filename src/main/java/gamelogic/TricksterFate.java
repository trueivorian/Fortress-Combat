package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterFate extends Card
{
	public TricksterFate() {
		super();
		super.setId(38);
		super.setName("Trickster - Fate");
		super.setImgPath("TricksterCards_11.png");
		super.setDescription("Destroy any card you control and destroy 1 enemy soldier.");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setAction(TricksterAbility.Action.DESTROY_FRIENDLY_CARD);
		ability.setTrigger(TricksterAbility.Trigger.ANYTHING);
		abilities.add(ability);
		ability = new TricksterAbility();
		ability.setAction(TricksterAbility.Action.DESTROY_ENEMY_SOLDIER);
		ability.setTrigger(TricksterAbility.Trigger.ANYTHING);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
