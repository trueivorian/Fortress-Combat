package graphics.utils;

/**
 * Created by Sam Gunner on 21/02/2019.
 */
public enum EventType {
    MOVE_CARD ("MOVE_CARD"),
    MOVE_MULTIPLE ("MOVE_MULTIPLE"),
    SPAWN_CARD ("SPAWN_CARD"),
    TARGET_CARD ("TARGET_CARD"),
    FLIP_CARD ("FLIP_CARD"),
    OPTION_MSG ("OPTION_MSG"),
    MSG ("MSG"),
    TURN_START ("TURN_START"),
    MULLIGAN_MSG ("MULLIGAN_MSG"),
    TRICKSTER_MSG ("TRICKSTER_MSG"),
    MY_CASTLE_HEALTH("MY_CASTLE_HEALTH"),
    THEIR_CASTLE_HEALTH("THEIR_CASTLE_HEALTH"),
    MY_USERNAME("MY_USERNAME"),
    THEIR_USERNAME("THEIR_USERNAME"),
    GET_DECK_CARD("GET_DECK_CARD");

    private String stringValue;
    EventType(String value) {
        stringValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
