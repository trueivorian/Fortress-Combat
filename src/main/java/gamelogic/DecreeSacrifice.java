package gamelogic;

public class DecreeSacrifice extends Card
{

	public DecreeSacrifice() {
		super();
		super.setId(6);
		super.setName("Decree - Sacrifice");
		super.setImgPath("DecreerCards_01.png");
		super.setDescription("Decree - Sacrifice An ally soldier can attack twice but  gets destroyed at the end of the turn then destroy this card");
		super.setType(CardType.DECREE);
	}

}
