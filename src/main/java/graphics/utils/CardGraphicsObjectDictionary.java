package graphics.utils;

import gamelogic.Card;
import graphics.exceptions.InvalidCardPosException;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import utils.CardPos;

import java.util.HashMap;

import static graphics.Graphics.spawnCards;

/**
 * Created by Sam Gunner on 26/03/2019.
 */
public class CardGraphicsObjectDictionary {
    private static HashMap<CardPos, CardGraphicsObject> mapping = new HashMap<>();
    private static HashMap<Integer, CardPos> reverseMapping = new HashMap<>();

    public static CardGraphicsObject getCardGraphicsObject(CardPos position) throws InvalidCardPosException {
        if (mapping.containsKey(position)) {
            return mapping.get(position);
        }

        throw new InvalidCardPosException(position);
    }

    public static CardPos getCardPosOfUID(int uid) throws InvalidCardPosException {
        if (reverseMapping.containsKey(uid))
            return reverseMapping.get(uid);

        throw new InvalidCardPosException(null);
    }

    public static boolean containsCardGraphicsObject(CardPos pos) {
        return mapping.containsKey(pos);
    }

    public static void setCardGraphicsObject(CardPos cardPos, CardGraphicsObject cardGraphicsObject) {
        int uid = cardGraphicsObject.getCardInstance().getUid();
        reverseMapping.put(uid, cardPos);
        mapping.put(cardPos, cardGraphicsObject);
    }

    public static void removeCardGraphicsObject(CardPos cardPos) throws InvalidCardPosException {
        if (mapping.containsKey(cardPos)) {
            mapping.remove(cardPos);
        } else {
            throw new InvalidCardPosException(cardPos);
        }
    }

    public static HashMap<CardPos, CardGraphicsObject> getAll() {
        return mapping;
    }
}
