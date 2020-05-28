package networking.exceptions;

import utils.CardPos;

public class CardLogicException extends Throwable {
    private CardPos cardPos1;
    private CardPos cardPos2;

    public CardLogicException(CardPos cardPos1) {
        this.cardPos1 = cardPos1;
    }

    public CardLogicException(CardPos cardPos1, CardPos cardPos2) {
        this.cardPos1 = cardPos1;
        this.cardPos2 = cardPos2;
    }
}
