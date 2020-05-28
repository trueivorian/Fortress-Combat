package networking.server.utils;

import gamelogic.CardType;

import java.util.ArrayList;

/**
 * Created by Sam Gunner on 26/03/2019.
 */
public class MageAbility {
    public enum AbilityType {
        PLAY_DECK,
        ADD_MAGE_DECK,
        PLAY_FROM_HAND,
        LEVEL_MAGE,
        ADD_MAGE_DEFENDER,
        ADD_MAGE_ATTACKER,
        DRAW_2_UNLESS_DEFENDER,
        DRAW_2_UNLESS_ATTACKER,
        DESTROY_1_UNLESS_DEFENDER,
        HEAL_UNLESS_ATTACKER,
        DAMAGE,
        DAMAGE_ALLY,
        DESTROY,
        DESTROY_THIS

    }

    private AbilityType abilityType;
    private int amount;
    private CardType cardType;

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public void setAbilityType(AbilityType s) {
        abilityType = s;
    }

    public boolean isTarget() {
        return target;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    private boolean target = false;
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
}
