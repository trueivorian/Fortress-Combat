package gamelogic;
public class MageProtector extends Card
{
	 public MageProtector()
	{
		super();
		super.setId(58);
		super.setName("Mage - Protector");
		super.setImgPath("MageCards_17.png");
		super.setDescription("lvl1:Special: Once when a card were to be destroyed, it is not destroyed."
				+ "lvl2:On Lvl: An ally card of your choice gains 5 points"
				+ "lvl3:In play: Destroy this card and play a mage from your deck and have it gain 10 points");
		super.setType(CardType.MAGE);
		super.setPoints(10);
	}
}
