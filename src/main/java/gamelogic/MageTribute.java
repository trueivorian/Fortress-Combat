package gamelogic;
public class MageTribute extends Card
{

	public MageTribute() {
		super();
		super.setId(50);
		super.setName("Mage - Tribute");
		super.setImgPath("MageCards_09.png");
		super.setDescription("lvl1:On play: Draw 2 cards"
				+ "lvl2:On Lvl: This turn reduce the cost of any card by one of any card"
				+ "lvl3:In In play: Destroy this card to destroy 2 enemy cards");
		super.setType(CardType.MAGE);
		super.setPoints(10);
	}
	
	
	
	

}
