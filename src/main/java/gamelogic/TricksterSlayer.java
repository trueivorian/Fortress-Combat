package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterSlayer extends Card
{
	public TricksterSlayer() {
		super();
		super.setId(36);
		super.setName("Trickster - Slayer");
		super.setImgPath("TricksterCards_09.png");
		super.setDescription("Play a soldier from the deck but cancel its on-play ability.");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setTrigger(TricksterAbility.Trigger.ANYTHING);
		ability.setAction(TricksterAbility.Action.PLAY_SOLDIER_CANCEL_ONPLAY);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
