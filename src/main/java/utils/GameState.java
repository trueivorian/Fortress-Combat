package utils;
import java.util.EnumMap;

public enum GameState {
    AWAIT_MULL,
    AWAIT_CHOOSE_COST, // When the server is awaiting the user choosing a cost to spend
    AWAIT_ONPLAY_CHOOSE,
    NOTHING
}
