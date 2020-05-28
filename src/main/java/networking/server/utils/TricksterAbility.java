package networking.server.utils;

/**
 * Created by Sam Gunner on 26/03/2019.
 */
public class TricksterAbility {
    public enum Trigger {
        SOLDIER_ATTACK, // When an enemy soldier attacks
        ENEMY_ATTACK,
        ANYTHING, // Any action at all
        ABILITY, // When ana enemy uses an ability
        ADD_DRAW_ABILITY, // When an enemy adds/draws a new card
        ENEMY_TRICKSTER_ACTIVATE, // When an enemy activates a trickster
        IN_PLAY_ON_LVL// When an in-play or on-lvl ability activates
    };

    public enum Action {
        PLAY_SOLDIER,
        CANCEL_ONLVL,
        CANCEL_INPLAY,
        STUN_CARD,
        DESTROY_ATTACKING_SOLDIER,
        CANCEL_ABILITY_DRAW_ADD_CARDS, // cancel the ability and instead, you draw/add those cards
        CANCEL_ABILITY_ADD_TRICKSTER,
        CANCEL_ABILITY_DESTROY_CARD, // cancel the ability and destroy the card
        DESTROY_ENEMY_SOLDIER,
        DESTROY_ENEMY_MAGE,
        DESTROY_FRIENDLY_CARD,
        DRAW_EQUAL_CARDS_ENEMY_MAGE_LEVELS, //Draw cards equal to the combined levels of your enemy's mages
        REDIRECT_SOLDIER_ATTACK, // When an enemy soldier attacks, choose one of your enemy cards for it to target.
        DESTROY_ATTACK_ADD_ATTACK_CARD, // destroy the attacking card and add a card of that type to your hand.
        DRAW_2_CARDS,
        PLAY_SOLDIER_CANCEL_ONPLAY,
        SWAP_SOLDIER_WITH_ENEMY,
        CANCEL_ENEMY_TRICKSTER,
        CHOOSE_DESTROY_TRICKSTER // Look at all of your enemy tricksters and choose one to destroy
    };

    private Trigger trigger;
    private Action action;
    private boolean special = false; // Whether or not the player can activate this card during their own turn (true = they can)

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }
}
