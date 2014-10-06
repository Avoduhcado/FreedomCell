package client.menu;

import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import client.Theater;
import client.utility.Text;

public class Chatlog {

	private float displayTimer;
	private LinkedList<String> chat = new LinkedList<String>();
	private InputBox chatbox;
	private String entry;
	
	private static Chatlog chatlog;
	
	public static void init() {
		chatlog = new Chatlog();
	}
	
	public static Chatlog get() {
		return chatlog;
	}
	
	public void update() {
		if(displayTimer > 0)
			displayTimer -= Theater.getDeltaSpeed(0.025f);
		if(chatbox != null) {
			chatbox.update();
			if((entry = chatbox.input()) != null) {
				if(!entry.trim().matches("")) {
					addMessage(Theater.get().getClient().getClientName(), entry);
					Theater.get().getClient().sendData("Chat;" + entry);
				} else
					displayTimer = 0f;
				chatbox = null;
			}
		}
	}
	
	public void draw() {
		if(displayTimer == -1) {
			for(int x = 0; x<chat.size(); x++) {
				Text.drawString(chat.get(x), 15, 670 - (20 * x), Color.orange);
			}
		} else if(displayTimer > 0) {
			for(int x = 0; x < (chat.size() > 5 ? 5 : chat.size()); x++) {
				Text.drawString(chat.get(x), 15, 670 - (20 * x), Color.orange);
			}
		}
		
		if(chatbox != null) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPushMatrix();
			
			GL11.glTranslatef(10, 690, 0);
			GL11.glColor4f(0f, 0f, 0f, 0.65f);

			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f(940, 0);
				GL11.glVertex2f(940, 25);
				GL11.glVertex2f(0, 25);
			}
			GL11.glEnd();
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			chatbox.draw(15, 690);
		}
	}
	
	public void addMessage(String name, String message) {
		chat.addFirst(name + ": " + message);
		if(chat.size() > 25)
			chat.removeLast();
		displayTimer = 5f;
	}
	
	public void open() {
		displayTimer = -1;
		chatbox = new InputBox(false, "");
		chatbox.setCentered(false);
		entry = null;
	}

	public void close() {
		displayTimer = 5f;
		chatbox = null;
	}
	
	public boolean closed() {
		if(chatbox == null)
			return true;
		
		return false;
	}
	
}
