package networking.server.utils;

public enum OnPlayAction {
    DISCARD_RANDOM_ENEMY_HAND ("DISCARD_RANDOM_ENEMY_HAND", false),
    STUN_ENEMY_CARD ("STUN_ENEMY_CARD", true),
    DESTROY_ENEMY_CARD ("DESTROY_ENEMY_CARD", true),
    DESTROY_ENEMY_TRICKSTER ("DESTROY_ENEMY_TRICKSTER", true),
    STUN_ENEMY_SOLDIER ("STUN_ENEMY_SOLDIER", true),
    PLAY_SOLDIER ("PLAY_SOLDIER", true),
    PLAY_FREE_SOLDIER_CANCEL_ABILITY ("PLAY_FREE_SOLDIER_CANCEL_ABILITY", true),
    PLAY_MAGE_CANCEL_ABILITY ("PLAY_MAGE_CANCEL_ABILITY", true),
    ADD_SOLDIER ("ADD_SOLDIER", true),
    DEAL_CASTLEDAMAGE_2 ("DEAL_CASTLEDAMAGE_2", false),
    DRAW_CARDS_1 ("DRAW_CARDS_1",false),
    DRAW_CARDS_2 ("DRAW_CARDS_2",false),
    ADD_ANY_CARD("ADD_ANY_CARD",false),
    ADD_MAGE("ADD_MAGE",false),
    DESTROY_SELF("DESTROY_SELF",false);

    private String stringValue;
    private boolean requiresTarget;
    OnPlayAction(String str, boolean bool) {
        stringValue = str;
        requiresTarget = bool;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public boolean requiresTarget() {
        return requiresTarget;
    }
}
