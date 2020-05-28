package gamelogic;
public class MageMut extends Card
{

	public MageMut() {
		super();
		super.setId(53);
		super.setName("Mage - Mut");
		super.setImgPath("MageCards_12.png");
		super.setDescription("lvl1:On play: Reduce the points of an enemy card by 5"
				+ "lvl2:On lvl: Destroy 1 mage and 1 trickster"
				+ "lvl3:In play: In play: Discard 2 cards add 1 card from your deck to your hand and play a trickster from the deck ");
		super.setType(CardType.MAGE);
		super.setPoints(25);
	}


}
