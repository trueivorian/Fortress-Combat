package gamelogic;

public class SoldierWarrior extends Card {
	public SoldierWarrior() {
		super();
		super.setId(26);
		super.setName("Soldier - Warrior");
		super.setImgPath("Deck_17.png");
		super.setDescription("If this card destroys an enemy card, add that type of card from your deck to your hand.");
		super.setPoints(20);
		super.setType(CardType.SOLDIER);
	}
}
