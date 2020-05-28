package gamelogic;

public class MageType extends Card{
	String cardType = "Mage";
	int level = 0;
	
	
	public void levelUp() {
		if (level < 3) {
			level++;
		}
	}
	
}
