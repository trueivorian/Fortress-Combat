package gamelogic;
public class MageGlory extends Card
{

	public MageGlory() {
		super();
		super.setId(49);
		super.setName("Mage - Glory");
		super.setImgPath("MageCards_08.png");
		super.setDescription("lvl1:On play: Play a no cost soldier from the hand"
				+ "lvl2:On Lvl: Destroy an enemy trickster."
				+ "lvl3:In play: add a card from the deck to hand");
		super.setType(CardType.MAGE);
		super.setPoints(10);

	}

}
