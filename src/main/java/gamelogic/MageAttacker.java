package gamelogic;

import networking.server.utils.MageAbility;

import java.util.ArrayList;

public class MageAttacker extends Card
{

	public MageAttacker() {
		super();
		super.setId(44);
		super.setName("Mage - Attacker");
		super.setImgPath("MageCards_03.png");
		super.setDescription("lvl1:On play: Add a Mage - Defender from your deck to your hand."
				+ "lvl2:On Lvl: Draw 2 card unless Mage - Defender is in play then draw 3"
				+ "lvl3:In In play: Destroy one enemy card unless mage defender is in play, then destroy all enemy cards");
		super.setType(CardType.MAGE);
		super.setPoints(15);

		ArrayList<MageAbility> abillities = new ArrayList<>();
		ArrayList<ArrayList<MageAbility>> levels = new ArrayList<>();
		MageAbility first = new MageAbility();
		first.setAbilityType(MageAbility.AbilityType.ADD_MAGE_DEFENDER);
		abillities.add(first);
		levels.add(abillities);

		MageAbility second = new MageAbility();
		second.setAbilityType(MageAbility.AbilityType.DRAW_2_UNLESS_DEFENDER);
		second.setTarget(true);
		ArrayList<MageAbility> two = new ArrayList<>();
		two.add(second);
		levels.add(two);

		MageAbility third = new MageAbility();
		third.setAbilityType(MageAbility.AbilityType.DESTROY_1_UNLESS_DEFENDER);
		third.setTarget(true);
		ArrayList<MageAbility> three = new ArrayList<>();
		three.add(third);

		levels.add(three);
	}

}
