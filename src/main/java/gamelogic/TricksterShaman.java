package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterShaman extends Card
{
	public TricksterShaman() {
		super();
		super.setId(33);
		super.setName("Trickster - Shaman");
		super.setImgPath("TricksterCards_06.png");
		super.setDescription("Draw 2 cards");
		super.setType(CardType.TRICKSTER);

        ArrayList<TricksterAbility> abilities = new ArrayList<>();
        TricksterAbility ability = new TricksterAbility();
        ability.setTrigger(TricksterAbility.Trigger.ANYTHING);
        ability.setAction(TricksterAbility.Action.DRAW_2_CARDS);
        abilities.add(ability);
        super.setTricksterAbilities(abilities);
	}
}