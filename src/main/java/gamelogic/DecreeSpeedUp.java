package gamelogic;

public class DecreeSpeedUp extends Card 
{
	
	public DecreeSpeedUp() {
		super();
		super.setId(8);
		super.setName("Decree - Speed Up");
		super.setImgPath("DecreerCards_03.png");
		super.setDescription("Decree - Speed Up While this card is on the field, once per turn, you may draw an extra card.	");
		super.setType(CardType.DECREE);
		super.setPoints(8);
	}


}
