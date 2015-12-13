package server;

/**
 * The main action about the socket
 * @author zhengyu
 *
 */

public interface SocketClientInterface {
	
	boolean openConnection();
    void handleSession();
    void closeSession();

}
