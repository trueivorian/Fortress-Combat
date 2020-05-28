package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterOpportunist extends Card
{
	public TricksterOpportunist() {
		super();
		super.setId(32);
		super.setName("Trickster - Opportunist");
		super.setImgPath("TricksterCards_05.png");
		super.setDescription("When an in-play or on-lvl ability activates, cancel the ability and destroy the card");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setTrigger(TricksterAbility.Trigger.IN_PLAY_ON_LVL);
		ability.setAction(TricksterAbility.Action.CANCEL_ABILITY_DESTROY_CARD);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
