package gamelogic;

public class DecreeCallforArms extends Card
{
	public DecreeCallforArms() {
		super();
		super.setId(7);
		super.setName("Decree - Call for Arms");
		super.setImgPath("DecreerCards_02.png");
		super.setDescription("Decree - Call for Arms Play any card from the deck (Costs still apply) Destroy the card after 2 turns Then destroy this card");
		super.setType(CardType.DECREE);
	}

}
