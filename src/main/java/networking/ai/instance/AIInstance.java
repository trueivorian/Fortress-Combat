package networking.ai.instance;

import ai.AI;

/**
 * Created by Sam Gunner on 29/03/2019.
 *
 * Controls a specific AI server instance
 */
public class AIInstance {
    public static void main(String[] args) {
        AI ai = new AI();
        Thread aiThread = new Thread(ai);
        aiThread.run();
    }
}
