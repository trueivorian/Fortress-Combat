package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterReinforcements extends Card
{
	public TricksterReinforcements() {
		super();
		super.setId(29);
		super.setName("Trickster - Reinforcements");
		super.setImgPath("TricksterCards_02.png");
		super.setDescription("When an enemy attacks, destroy the attacking card and add a card of that type to your hand.");
		super.setType(CardType.TRICKSTER);

		ArrayList<TricksterAbility> abilities = new ArrayList<>();
		TricksterAbility ability = new TricksterAbility();
		ability.setTrigger(TricksterAbility.Trigger.ENEMY_ATTACK);
		ability.setAction(TricksterAbility.Action.DESTROY_ATTACK_ADD_ATTACK_CARD);
		abilities.add(ability);
		super.setTricksterAbilities(abilities);
	}
}
