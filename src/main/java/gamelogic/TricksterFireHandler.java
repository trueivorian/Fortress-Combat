package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterFireHandler extends Card
{
	public TricksterFireHandler() {
		super();
		super.setId(34);
		super.setName("Trickster - Fire Handler");
		super.setImgPath("TricksterCards_07.png");
		super.setDescription("When an enemy adds/draws cards from an ability, cancel the ability and instead, you draw/add those cards");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setTrigger(TricksterAbility.Trigger.ADD_DRAW_ABILITY);
		ability.setAction(TricksterAbility.Action.CANCEL_ABILITY_DRAW_ADD_CARDS);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
