package networking.exceptions;

/**
 * Created by Sam Gunner on 25/01/2019.
 */
public class UserIDNotFoundException extends Throwable {
    private int id;

    public UserIDNotFoundException(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
