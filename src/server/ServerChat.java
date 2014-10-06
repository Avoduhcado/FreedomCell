package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import server.scores.Scores;
import server.table.ServerField;

public class ServerChat extends Thread {

	private SocketThread[] clients = new SocketThread[4];
	private PrintWriter out;
	private ServerField serverField = new ServerField();
	
	public void run() {
		serverField = new ServerField();
		Scores.init();
	}
	
	public void broadcast(String message, SocketThread client) {
		for(int x = 0; x<clients.length; x++) {
			try {
				if(clients[x] != client && clients[x] != null) {
					out = new PrintWriter(clients[x].getSocket().getOutputStream(), true);
					out.println(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		//System.out.println("Server: " + message);
	}
	
	public void greet(int client) {
		String input = null;
		// Get client name from new client
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clients[client].getSocket().getInputStream()));
			input = in.readLine();
			clients[client].setClientName(input.matches("Client") ? sortName(client) : input);
			System.out.println("Server: " + clients[client].getClientName() + " has connected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Announce to other clients a new client has connected
		broadcast("User " + clients[client].getClientName() + " has joined!", clients[client]);
		broadcast("Names;" + getClientNames(), clients[client]);
		
		// Welcome the new client to the server
		try {
			out = new PrintWriter(clients[client].getSocket().getOutputStream(), true);
			out.println("Welcome to the server!");
			out.println(serverField.toString() + client);
			out.println("Names;" + getClientNames() + ";" + clients[client].getClientName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String sortName(int client) {
		switch(client) {
		case(0):
			return "Greaser";
		case(1):
			return "Cardshark";
		case(2):
			return "Patron";
		case(3):
			return "Bill";
		}
		return "Client";
	}
	
	
	public void sendField(int client) {
		try {
			out = new PrintWriter(clients[client].getSocket().getOutputStream(), true);
			out.println(serverField.toString() + client);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateField(String update, SocketThread client) {
		String[] temp = update.split(";");
		if(temp[0].matches("MoveCell")) {
			serverField.placeCardCell(temp[1], Integer.parseInt(temp[2]));
			broadcast("MoveCell;" + temp[1] + ";" + Integer.parseInt(temp[2]), client);
		} else if(temp[0].matches("MoveFoundation")) {
			serverField.placeCardFoundation(temp[1], Integer.parseInt(temp[2]));
			broadcast("MoveFoundation;" + temp[1] + ";" + Integer.parseInt(temp[2]), client);
		} else if(temp[0].matches("MoveCascade")) {
			serverField.placeCard(temp[1], Integer.parseInt(temp[2]));
			broadcast("MoveCascade;" + temp[1] + ";" + Integer.parseInt(temp[2]), client);
		}
	}
	
	public void updateScores(String update) {
		String[] temp = update.split(";");
		Scores.get().addScore(Float.parseFloat(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
				Float.parseFloat(temp[4]), Integer.parseInt(temp[5]));
		broadcast("Finish;" + Scores.get().toString(), null);
	}
	
	public boolean hasFreeClient() {
		for(int x = 0; x<clients.length; x++) {
			if(clients[x] == null)
				return true;
		}
			
		return false;
	}
	
	public void addClient(Socket socket) {
		if(hasFreeClient()) {
			for(int x = 0; x<clients.length; x++) {
				if(clients[x] == null) {
					clients[x] = new SocketThread(socket, this, x);
					clients[x].start();
					greet(x);
					//sendField(x);
					break;
				}
			}
		} else {
			System.out.println("Excess client failed to connect.");
		}
	}
	
	public void removeClient(SocketThread socketThread) {
		System.out.println("Server: " + socketThread.getClientName() + " has disconnected.");
		for(int x = 0; x<clients.length; x++) {
			if(clients[x] == socketThread) {
				clients[x] = null;
				break;
			}
		}
		
		broadcast("Names;" + getClientNames(), socketThread);
	}
	
	public String getClientNames() {
		String temp = clients[0] == null ? " " : clients[0].getClientName().replaceAll("-", " ");
		for(int x = 1; x<clients.length; x++) {
			if(clients[x] != null)
				temp += "-" + clients[x].getClientName().replaceAll("-", " ");
			else
				temp += "- ";
		}
		
		return temp;
	}
	
}
