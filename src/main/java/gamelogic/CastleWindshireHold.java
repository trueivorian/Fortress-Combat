package gamelogic;

public class CastleWindshireHold extends Card
{

	public CastleWindshireHold() {
		super();
		super.setId(0);
		super.setName("Castle - Windshire Hold");
		super.setImgPath("CastlerCards_01.png");
		super.setDescription("Once during your turn when an ally soldier is destroyed you can draw a card");
		super.setType(CardType.CASTLE);
		super.setPoints(60);
	}
}
