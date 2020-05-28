package gamelogic;

import networking.server.utils.MageAbility;

import java.util.ArrayList;

public class MageApprentice extends Card
{

	public MageApprentice() {
		super();
		super.setId(43);
		super.setName("Mage - Apprentice");
		super.setImgPath("MageCards_02.png");
		super.setDescription("lvl1:On play: Add a mage from your deck to your hand."
				+ "\nlvl2:On Lvl: Play a mage from the hand and immediately level it to 2."
				+ "\nlvl3:On Lvl: Destroy this card then play a mage from the hand or deck and immediately level it to 3.");
		super.setType(CardType.MAGE);
		super.setPoints(10);

		ArrayList<MageAbility> abillities = new ArrayList<>();
		ArrayList<ArrayList<MageAbility>> levels = new ArrayList<>();
		MageAbility first = new MageAbility();
		first.setAbilityType(MageAbility.AbilityType.ADD_MAGE_DECK);
		abillities.add(first);
		levels.add(abillities);
		MageAbility second = new MageAbility();
		second.setAbilityType(MageAbility.AbilityType.PLAY_FROM_HAND);
		second.setTarget(true);
		ArrayList<MageAbility> two = new ArrayList<>();
		two.add(second);
		MageAbility second2 = new MageAbility();
		second2.setAbilityType(MageAbility.AbilityType.LEVEL_MAGE);
		second2.setTarget(true);
		second2.setAmount(2);
		levels.add(two);

		MageAbility third = new MageAbility();
		third.setAbilityType(MageAbility.AbilityType.PLAY_FROM_HAND);
		third.setTarget(true);
		ArrayList<MageAbility> three = new ArrayList<>();
		three.add(third);
		MageAbility third2 = new MageAbility();
		third2.setAbilityType(MageAbility.AbilityType.LEVEL_MAGE);
		third2.setTarget(true);
		third2.setAmount(3);
		levels.add(three);

	}



}
