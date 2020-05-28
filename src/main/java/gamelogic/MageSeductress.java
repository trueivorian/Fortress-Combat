package gamelogic;
public class MageSeductress extends Card
{	

	public MageSeductress() {
		super();
		super.setId(51);
		super.setName("Mage - Seductress");
		super.setImgPath("MageCards_10.png");
		super.setDescription("lvl1:On play: Add a soldier from your deck to your hand"
				+ "lvl2:On Lvl: Play a soldier from your hand"
				+ "lvl3:On Lvl: Take an enemy soldier, which stays on your board until this card is destroyed");
		super.setType(CardType.MAGE);
		super.setPoints(20);
	}

}
