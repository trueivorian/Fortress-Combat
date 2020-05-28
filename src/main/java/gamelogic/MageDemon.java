package gamelogic;
public class MageDemon extends Card
{

	public MageDemon() {
		super();
		super.setId(57);
		super.setName("Mage - Demon");
		super.setImgPath("MageCards_16.png");
		super.setDescription("lvl1:On play: Discard 1 card and stun all enemy soldiers."
				+ "lvl2:On lvl: This card can attack this turn"
				+ "lvl3:in play: Add a mage from the deck. When the added mage is played, reduce its cost by 1. ");
		super.setType(CardType.MAGE);
		super.setPoints(20);
	}

}
