package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterMirage extends Card
{
	public TricksterMirage() {
		super();
		super.setId(40);
		super.setName("Trickster - Mirage");
		super.setImgPath("TricksterCards_13.png");
		super.setDescription("When an enemy soldier attacks, choose one of your enemy cards for it to target.");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setTrigger(TricksterAbility.Trigger.SOLDIER_ATTACK);
		ability.setAction(TricksterAbility.Action.REDIRECT_SOLDIER_ATTACK);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
