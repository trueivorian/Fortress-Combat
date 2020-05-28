package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterIceHandler extends Card
{
	public TricksterIceHandler() {
		super();
		super.setId(35);
		super.setName("Trickster - Ice Handler");
		super.setImgPath("TricksterCards_08.png");
		super.setDescription("Cancel an ability and add a trickster from your deck to your hand");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setTrigger(TricksterAbility.Trigger.ABILITY);
		ability.setAction(TricksterAbility.Action.CANCEL_ABILITY_ADD_TRICKSTER);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
