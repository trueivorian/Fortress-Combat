package gamelogic;
public class MageSoldiersPartner extends Card
{
	public MageSoldiersPartner()
	{
		super();
		super.setId(47);
		super.setName("Mage - Soldiers Partner");
		super.setImgPath("MageCards_06.png");
		super.setDescription("lvl1:On play: Play a soldier from the deck but cancel its on play ability"
				+ "lvl2:In play: A soldier of your choice can attack twice this turn."
				+ "lvl3:On Lvl: Draw 2 cards, Add a soldier from the deck to your hand");
		super.setType(CardType.MAGE);
		super.setPoints(15);
	}

}
