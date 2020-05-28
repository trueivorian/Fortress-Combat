package gamelogic;
public class MageWizard extends Card
{

	public MageWizard() {
		super();
		super.setId(48);
		super.setName("Mage - Wizard");
		super.setImgPath("MageCards_07.png");
		super.setDescription("lvl1:On play: Destroy an enemy card and immediately level this card to lvl 2"
				+ "lvl2:On Lvl: Increase this cards points by 5 for each enemy mage. "
				+ "lvl3:In play: Discard 2 cards from your hand and destroy 2 enemy cards, then draw 2 cards.");
		super.setType(CardType.MAGE);
		super.setPoints(20);
	}
}
