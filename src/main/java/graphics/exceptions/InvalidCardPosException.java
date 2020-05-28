package graphics.exceptions;

import utils.CardPos;

/**
 * Created by Sam Gunner on 26/03/2019.
 */
public class InvalidCardPosException extends Throwable {
    private CardPos cardPos;

    public InvalidCardPosException(CardPos cardPos) {
        this.cardPos = cardPos;
    }

    public CardPos getCardPos() {
        return cardPos;
    }
}
