package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import dblayout.DatabaseIO;

/**
 * @version 1.0
 * @author YuZheng
 * @Date 10/1/2015
 * 
 *       This is the main function that can start the server.
 * 
 */

public class Server implements SocketClientConstants{

	public static void main(String[] args) {
		
		int port = iDAYTIME_PORT;
		ServerSocket serverSocket = null;
		DatabaseIO db = new DatabaseIO();
		System.out.println("The server is starting....");
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Listening on the port: " + port);
		System.out.println("Server has started, waiting for client....");

		DefaultSocketClient defale;
		try {
			while (true) {
				Socket client = serverSocket.accept();
				defale = new DefaultSocketClient(client, db);
				defale.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("END");
		
	}

}
