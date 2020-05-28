package gamelogic;

import networking.CommandType;
import networking.server.ClientTable;
import networking.server.utils.OnPlayAction;
import utils.BoardRegion;
import utils.CardPos;
import java.util.ArrayList;
import java.util.HashMap;

public class SoldierBarbarian extends Card
{
	public SoldierBarbarian() {
		super();
		super.setId(23);
		super.setName("Soldier - Barbarian");
		super.setImgPath("Deck_14.png");
		super.setDescription("On play: Stun 1 enemy soldier and discard 1 card randomly from your opponent's hand");
		super.setType(CardType.SOLDIER);
		super.setPoints(10);
		ArrayList<OnPlayAction> onPlayWaitList = new ArrayList<>();
//		onPlayWaitList.add(OnPlayAction.STUN_ENEMY_SOLDIER); // Add the on play (stun an enemy soldier)
		onPlayWaitList.add(OnPlayAction.DISCARD_RANDOM_ENEMY_HAND); // Discard 1 card random from enemy hand
		super.setOnPlayWait(onPlayWaitList); // Indicate that the server needs to wait for an additional target command before playing this card.
	}

	/**
	 * SERVERSIDE
	 */
	public void onPlay(CardPos cardPos) {

	}

	public void onPlay(ArrayList<CardPos> s){
		destroy(s.get(0));
		destroy(s.get(1));
	}
}
