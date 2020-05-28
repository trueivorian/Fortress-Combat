package gamelogic;
public class MageDefender extends Card
{
	public MageDefender() {
		super();
		super.setId(15);
		super.setName("Mage - Defender");
		super.setImgPath("MageCards_04.png");
		super.setDescription("lvl1:On play: Add a Mage -Attacker your deck to your hand."
				+ "lvl2:On Lvl: Draw 2 card unless Mage - Attacker is in play then draw 3"
				+ "lvl3:In play: In play: Heal 15 damage to an ally castle unless Mage - Attacker is in play then Heal all ally castle damage");
		super.setType(CardType.MAGE);
		super.setPoints(15);
	}

}
