package gamelogic;
public class MageLight extends Card
{
	public MageLight( ) {
		super();
		super.setId(54);
		super.setName("Mage - Light");
		super.setImgPath("MageCards_13.png");
		super.setDescription("On play: Add a soldier from your deck to your hand"
				+ "lvl2:On lvl: Look at all of your enemy tricksters and choose one to destroy"
				+ "lvl3:In play: Play 1 mage and 1 soldier from the deck but cancel their On play abilities");
		super.setType(CardType.MAGE);
		super.setPoints(15);
	}

}
