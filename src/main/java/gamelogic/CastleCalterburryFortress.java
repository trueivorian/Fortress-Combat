package gamelogic;
import java.util.*;
public class CastleCalterburryFortress extends Card
{

	public CastleCalterburryFortress() {
		super();
		super.setId(1);
		super.setName("Castle - Calterburry Fortress");
		super.setImgPath("CastlerCards_02.png");
		super.setDescription("Calterburry Fortress Your opponents cards cannot go above their original points and your soldiers cannot be stunned");
		super.setType(CardType.CASTLE);
		super.setPoints(80);
	}
}
