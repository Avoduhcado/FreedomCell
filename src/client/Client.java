package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import client.menu.Chatlog;
import client.scores.Army;
import client.scores.ScoreScreen;

public class Client extends Thread {

	private String hostName = "127.0.0.1";
	private int portNumber = 34334;
	private String clientName = "Client";
	private String[] playerNames = new String[]{" ", " ", " ", " "};
	private Socket socket = null;
	private volatile boolean waiting = true;
	private boolean connected = false;
	private BufferedReader in;
	private PrintWriter out;
		
	public Client(String hostName, int port, String name) {
		this.hostName = hostName;
		this.portNumber = port;
		this.clientName = name;
	}
	
	public void run() {
		try {
			socket = new Socket(hostName, portNumber);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socket.setSoTimeout(50);

			// Send client name to server to greet
			out.println(clientName);
			connected = true;
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			waiting = false;
		} catch (IOException e) {
			//System.err.println("Couldn't get I/O for the connection to ..." + hostName);
			System.out.println("Couldn't connect to host, trying localhost");
			try {
				socket = new Socket("localhost", portNumber);
				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				socket.setSoTimeout(50);

				// Send client name to server to greet
				out.println(clientName);
				connected = true;
			} catch (UnknownHostException e2) {
				System.err.println("Don't know about host " + hostName);
				waiting = false;
			} catch (IOException e2) {
				System.out.println("Couldn't get I/O for the connection to ..." + hostName);
				waiting = false;
			}
		}
		
		if(waiting) {
			receiveMessage();
		}
	}
	
	public void terminate() {
		waiting = false;
	}
	
	/**
	 * Listen for new messages from the server and then display any updates to client
	 */
	public void receiveMessage() {		
		while(waiting) {
			try {
				String fromServer;
				if((fromServer = in.readLine()) != null) {
					System.out.println(fromServer);
					parseData(fromServer);
				}
			} catch (SocketTimeoutException e) {
				;
			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e1) {
					System.err.println("Couldn't get I/O for the connection to ......" + hostName);
					System.exit(1);
				}
				System.err.println("Couldn't get I/O for the connection to " + hostName);
				System.exit(1);
			}
		}
		
		if(!waiting && !socket.isClosed()) {
			try {
				sendUpdate("Client " + clientName + " has disconnected.");
				socket.close();
			} catch(SocketException e) {
				System.err.println("Client " + clientName + " has disconnected unexpectedly!");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Send message data to server for processing.
	 * 
	 * @param message
	 */
	public void sendData(String message) {
		out.println(message);
	}
	
	// TODO Add custom name colors
	public void sendChat(String message) {
		out.println(clientName + ": " + message);
	}
	
	/**
	 * Sends a server announcement. Ignore this.
	 * 
	 * @param message
	 */
	public void sendUpdate(String message) {
		out.println("Server: " + message);
	}
	
	public void requestField() {
		out.println("Field");
	}
	
	public void parseData(String message) {
		String[] temp = message.split(";");
		
		if(temp[0].matches("Field")) {
			Theater.get().getStage().temp = temp[1];
		} else if(temp[0].matches("Names")) { 
			setPlayerNames(temp[1]);
			if(temp.length > 2)
				this.clientName = temp[2];
		} else if(temp[0].matches("MoveCell")) {
			Theater.get().getStage().playingField.moveCardCell(Theater.get().getStage().playingField.getCard(temp[1]),
					Integer.parseInt(temp[2]));
		} else if(temp[0].matches("MoveFoundation")) {
			Theater.get().getStage().playingField.moveCardFoundation(Theater.get().getStage().playingField.getCard(temp[1]),
					Integer.parseInt(temp[2]));
		} else if(temp[0].matches("MoveCascade")) {
			Theater.get().getStage().playingField.moveCardCascade(Theater.get().getStage().playingField.getCard(temp[1]),
					Integer.parseInt(temp[2]));
		} else if(temp[0].matches("Chat")) {
			Chatlog.get().addMessage(temp[1], temp[2]);
		} else if(temp[0].matches("Score")) {
			Theater.get().getStage().playingField.calculateScore();
			sendData("Score;" + Army.get().getSummary());
		} else if(temp[0].matches("Finish")) {
			Theater.get().getStage().scoreScreen = new ScoreScreen(temp[1]);
		}
	}
	
	public void processChat() {
		/*if(textField.getText().startsWith("/name ") && textField.getText().trim().length() > 5) {
			String update = clientName;
			clientName = textField.getText().substring(textField.getText().indexOf(' ') + 1, textField.getText().length());
			textField.setText("");
			textField.requestFocus();
			sendUpdate(update + " is now known as: " + clientName);
		} else {
			sendChat(textField.getText());
			textField.setText("");
			textField.requestFocus();
		}*/
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public int getPortNumber() {
		return portNumber;
	}
	
	public void setPortNumber(int port) {
		this.portNumber = port;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public String getPlayerName(int x) {
		return playerNames[x];
	}
	
	public void setPlayerNames(String names) {
		playerNames = names.split("-");
	}
	
	public boolean isConnected() {
		return connected;
	}
	
}
