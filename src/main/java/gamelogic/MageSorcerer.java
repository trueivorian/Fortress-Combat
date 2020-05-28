package gamelogic;
public class MageSorcerer extends Card
{
	public MageSorcerer() {
		super();
		super.setId(42);
		super.setName("Mage - Sorcerer");
		super.setImgPath("MageCards_01.png");
		super.setDescription("lvl1:On play: Add a trickster card from your deck to your hand."
				+ "lvl2:On Lvl: Play a Soldier from your deck but cancel its On play ability.."
				+ "lvl3:In play: Add a card from your deck to your hand and destroy an opponents .");
		super.setType(CardType.MAGE);
		super.setPoints(10);
	}

}
