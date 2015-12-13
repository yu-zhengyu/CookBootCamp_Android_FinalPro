package ws.remote;

/**
 * The main action that socket thread do
 */

public interface SocketClientInterface {
	
	boolean openConnection();
    void handleSession();
    void closeSession();

}
