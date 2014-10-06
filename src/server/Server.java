package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import client.utility.Config;

public class Server extends Thread {
	
	private int portNumber = 34334;
	private volatile boolean listening;
	private volatile ServerChat serverChat;
	private volatile ServerSocket serverSocket;
	
	public Server(int portNumber) {
		this.portNumber = portNumber;
	}
	
	public void run() {
		listen();
	}
	
	public void terminate() {
		listening = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reset() {
		serverChat.run();
	}
	
	public void listen() {
		listening = true;
		serverChat = new ServerChat();
		serverChat.start();
		
		System.out.println("Server started.\nNow listening on " + portNumber);
				
		while (listening) {
			try {
				serverSocket = new ServerSocket(portNumber);
				serverChat.addClient(serverSocket.accept());
				serverSocket.close();
			} catch (SocketException e) {
				System.out.println("Socket error, server closing.");
				listening = false;
				try {
					serverChat.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				System.err.println("Could not listen on port .." + portNumber);
				System.exit(-1);
			}
		}		
	}
	
	public static void main(String[] args) {
		Server server = new Server(args.length == 0 ? Config.hostPort : Integer.parseInt(args[0]));
		server.listen();
	}
}
