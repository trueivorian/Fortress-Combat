package gamelogic;

import networking.server.utils.MageAbility;

import java.util.ArrayList;

public class MageDeath extends Card
{
	public MageDeath() {
		super();
		super.setId(46);
		super.setName("Mage - Death");
		super.setImgPath("MageCards_05.png");
		super.setDescription("lvl1:On play: Deal 10 damage to an Ally castle and destroy any card "
				+ "lvl2:On Lvl: destroy any card"
				+ "lvl3:In play: Destroy this card and 3 cards your opponent controls.");
		super.setType(CardType.MAGE);
		super.setPoints(20);

		ArrayList<MageAbility> abillities = new ArrayList<>();
		ArrayList<ArrayList<MageAbility>> levels = new ArrayList<>();
		MageAbility first = new MageAbility();
		first.setAbilityType(MageAbility.AbilityType.DAMAGE_ALLY);
		first.setAmount(10);
		abillities.add(first);
		MageAbility first2 = new MageAbility();
		first2.setAbilityType(MageAbility.AbilityType.DESTROY);
		first2.setTarget(true);
		abillities.add(first2);
		levels.add(abillities);


		MageAbility second = new MageAbility();
		second.setAbilityType(MageAbility.AbilityType.DESTROY);
		second.setTarget(true);
		ArrayList<MageAbility> two = new ArrayList<>();
		two.add(second);
		levels.add(two);

		MageAbility third = new MageAbility();
		third.setAbilityType(MageAbility.AbilityType.DESTROY_THIS);
		MageAbility third2 = new MageAbility();
		third2.setAbilityType(MageAbility.AbilityType.DESTROY);
		third2.setTarget(true);
		MageAbility third3 = new MageAbility();
		third3.setTarget(true);
		third3.setAbilityType(MageAbility.AbilityType.DESTROY);
		MageAbility third4 = new MageAbility();
		third4.setAbilityType(MageAbility.AbilityType.DESTROY);
		third4.setTarget(true);
		ArrayList<MageAbility> three = new ArrayList<>();
		three.add(third);
		three.add(third2);
		three.add(third3);
		three.add(third4);
		levels.add(three);
	}

}
