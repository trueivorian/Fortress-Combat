package gamelogic;

public class CastleTortmainFortress extends Card 
{
	public CastleTortmainFortress() {
		super();
		super.setId(4);
		super.setName("Castle - Tortmain Fortree");
		super.setImgPath("CastlerCards_05.png");
		super.setDescription("Tortmain Fortress Once per game, if this castle were to be defeated you can play any soldier from the deck and set castle health to 1");
		super.setType(CardType.CASTLE);
		super.setPoints(75);
	}

}
