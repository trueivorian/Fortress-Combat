package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterSwap extends Card
{
	public TricksterSwap() {
		super();
		super.setId(39);
		super.setName("Trickster - Swap");
		super.setImgPath("TricksterCards_12.png");
		super.setDescription("You and your enemy choose one Soldier and swap those 2 soldiers");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setTrigger(TricksterAbility.Trigger.ANYTHING);
		ability.setAction(TricksterAbility.Action.SWAP_SOLDIER_WITH_ENEMY);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
