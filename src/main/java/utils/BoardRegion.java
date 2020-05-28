package utils;

/**
 * Created by Sam Gunner on 21/02/2019.
 */
public enum BoardRegion {
    MY_DECK ("MY_DECK"),
    MY_HAND ("MY_HAND"),
    MY_BOARD ("MY_BOARD"),
    THEIR_DECK ("THEIR_DECK"),
    THEIR_HAND ("THEIR_HAND"),
    THEIR_BOARD ("THEIR_BOARD");

    private String stringValue;
    BoardRegion(String value) {
        stringValue = value;
    }

    public String toString() {
        return stringValue;
    }
}
