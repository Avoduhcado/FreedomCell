package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class SocketThread extends Thread {

	private ServerChat server;
	private Socket socket;
	private String clientName;
	private BufferedReader in;
	private volatile boolean listening;
	private int clientNumber;
	
	public SocketThread(Socket socket, ServerChat server, int number) {
		this.socket = socket;
		this.server = server;
		this.clientNumber = number;
		
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			listening = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		String input;
		
		while(listening) {
			try {
				if((input = in.readLine()) != null) {
					parseRequest(input);
				}
			} catch (SocketException e) {
				listening = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		server.broadcast(clientName + " has disconnected.", this);
		server.removeClient(this);
	}
	
	public void terminate() {
		listening = false;
	}
	
	public void parseRequest(String input) {
		if(input.startsWith("Field")) {
			server.sendField(clientNumber);
		} else if(input.startsWith("MoveCell")) {
			server.updateField(input, this);
		} else if(input.startsWith("MoveFoundation")) {
			server.updateField(input, this);
		} else if(input.startsWith("MoveCascade")) {
			server.updateField(input, this);
		} else if(input.startsWith("Chat")) {
			server.broadcast("Chat;" + clientName + input.substring(4), this);
		} else if(input.startsWith("Kill")) {
			terminate();
		} else if(input.startsWith("Winner")) {
			server.broadcast("Score", null);
		} else if(input.startsWith("Score")) {
			server.updateScores(input + ";" + clientNumber);
		} else {
			server.broadcast(input, this);
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public void setClientName(String name) {
		this.clientName = name;
	}
	
	public int getClientNumber() {
		return clientNumber;
	}
	
	public void setClientNumber(int number) {
		this.clientNumber = number;
	}
	
}
