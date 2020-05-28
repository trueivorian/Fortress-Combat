package networking.server.utils;

import gamelogic.Card;
import utils.CardPos;
import utils.Dictionary;

/**
 * Created by Sam Gunner on 19/02/2019.
 */
public class CardDraw {
    private CardPos cardPos;
    private CardPos oldPos;
    private Card card;

    public CardPos getCardPos() {
        return cardPos;
    }

    public void setCardPos(CardPos cardPos) {
        this.cardPos = cardPos;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public CardPos getOldPos() {
        return oldPos;
    }

    public void setOldPos(CardPos oldPos) {
        this.oldPos = oldPos;
    }
}
