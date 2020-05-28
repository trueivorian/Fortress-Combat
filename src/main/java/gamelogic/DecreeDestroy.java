package gamelogic;

public class DecreeDestroy extends Card
{

	public DecreeDestroy() {
		super();
		super.setId(9);
		super.setName("Decree - Destroy");
		super.setImgPath("DecreerCards_04.png");
		super.setDescription("Decree - Destroy Destroy this card after 3 turns. While this card is on the field, you may destroy an ally card and an enemy card.");
		super.setType(CardType.DECREE);
	}

}
