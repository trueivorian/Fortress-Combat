package gamelogic;
public class MageTheUndead extends Card
{
	int id=52;
	String Name="Mage - The Undead";
	String img="MageCards_11.png";
	String description="lvl1:On play: Discard 2 card and add 2 mages from your deck to your hand"
			+ "lvl2:On Lvl: Draw 3 cards"
			+ "lvl3:In play: If a card would be destroyed this turn, it is not destroyed";
	 String type="Mage";
	int points=10;
	public MageTheUndead() {
		super();
		super.setId(52);
		super.setName("Mage - The Undead");
		super.setImgPath("MageCards_11.png");
		super.setDescription("lvl1:On play: Discard 2 card and add 2 mages from your deck to your hand"
				+ "lvl2:On Lvl: Draw 3 cards"
				+ "lvl3:In play: If a card would be destroyed this turn, it is not destroyed");
		super.setType(CardType.MAGE);
		super.setPoints(10);
		
	}
	
	
	
	
	

}
