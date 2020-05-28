package gamelogic;

import networking.server.utils.TricksterAbility;

import java.util.ArrayList;

public class TricksterPlague extends Card
{
	public TricksterPlague() {
		super();
		super.setId(41);
		super.setName("Trickster - Plague");
		super.setImgPath("TricksterCards_14.png");
		super.setDescription("Stun a card");
		super.setType(CardType.TRICKSTER);

        ArrayList<TricksterAbility> abilities = new ArrayList<>();
        TricksterAbility ability = new TricksterAbility();
        ability.setTrigger(TricksterAbility.Trigger.ANYTHING);
        ability.setAction(TricksterAbility.Action.STUN_CARD);
        abilities.add(ability);
        super.setTricksterAbilities(abilities);
	}
}
