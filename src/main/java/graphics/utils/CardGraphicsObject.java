package graphics.utils;

import gamelogic.Card;
import graphics.exceptions.InvalidCardPosException;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import utils.CardPos;

import static graphics.Graphics.spawnCards;

/**
 * Created by Sam Gunner on 26/03/2019.
 */
public class CardGraphicsObject {
    private Card cardInstance;
    private Group group;

    public CardGraphicsObject(Card _cardInstance, Group _group){
        cardInstance = _cardInstance;
        group = _group;
    }

    public Card getCardInstance() {
        return cardInstance;
    }

    public void setCardInstance(Card cardInstance) {
        this.cardInstance = cardInstance;
    }

    public void setCardEventHandler(EventHandler cardEventHandler){
        cardInstance.setCardEventHandler(cardEventHandler);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void move(CardPos end) throws InvalidCardPosException {
        Card card = CardGraphicsObjectDictionary.getCardGraphicsObject(end).getCardInstance();
        Group group = CardGraphicsObjectDictionary.getCardGraphicsObject(end).getGroup();
        spawnCards(group, new Image(card.getImgPath()), new Image(card.getImgBackPath()), card.getCardEventHandler(), end);
    }
}
