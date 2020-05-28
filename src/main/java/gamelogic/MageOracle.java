package gamelogic;
public class MageOracle extends Card
{

	public MageOracle() {
		super();
		super.setId(56);
		super.setName("Mage - Oracle");
		super.setImgPath("MageCards_15.png");
		super.setDescription("lvl1:On play: Look at the top 4 cards of your deck. Pick 2 to add then shuffle the deck. If your opponent has 1 in their deck, they may add it."
				+ "lvl2:On Lvl: Discard 2 cards, look at your oppenents hand and choose 1 card to dis"
				+ "lvl3:On Lvl: choose 2 cards in your deck add 1 to hand and play the other.");
		super.setType(CardType.MAGE);
		super.setPoints(10);
	}
	

}
