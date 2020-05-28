package gamelogic;

public class CastleWaretonRidge extends Card
{

	public CastleWaretonRidge() {
		super();
		super.setId(2);
		super.setName("Castle - Wareton Ridge");
		super.setImgPath("CastlerCards_02.png");
		super.setDescription("Wareton Ridge You have no hand limit");
		super.setType(CardType.CASTLE);
		super.setPoints(90);
	}

}
