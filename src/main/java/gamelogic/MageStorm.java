package gamelogic;
public class MageStorm extends Card
{
	public MageStorm() {
		super();
		super.setId(55);
		super.setName("Mage - Storm");
		super.setImgPath("MageCards_14.png");
		super.setDescription("lvl1:On play: Stun an enemy Soldier"
				+ "lvl2:On Lvl: Stun an enemy soldier and Draw 1 card"
				+ "lvl3:In play: Stun an enemy soldier and add 1 soldier from your deck to your hand");
		super.setType(CardType.MAGE);
		super.setPoints(15);
	}

}
