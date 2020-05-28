package networking.exceptions;

/**
 * Created by Sam Gunner on 25/01/2019.
 */
public class ServerNotFoundException extends Throwable {
    private String address;
    private int port;

    public ServerNotFoundException(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
