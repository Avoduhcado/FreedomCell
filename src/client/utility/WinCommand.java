package client.utility;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;

import client.Theater;
import client.utility.Text;
import client.utility.keyboard.Keybinds;

public class WinCommand {

	private String promptText;
	private String winText;
	private boolean won;
	private boolean completed;
	private List<String> events = new LinkedList<String>();
	private boolean autoComplete = false;
	
	public WinCommand(String promptText, String winText) {
		this.promptText = promptText;
		this.winText = winText;
	}
	
	public void update() {
		if(Keybinds.X.clicked()) {
			if(!won) {
				win();
			} else {
				if(events.isEmpty() && !Event.get().inAction())
					completed = true;
			}
		} else if(won && events.isEmpty() && !Event.get().inAction() && autoComplete) {
			completed = true;
		}
	}
	
	public void draw() {
		if(won) {
			Text.drawCenteredString(winText, (float) (Theater.get().getScreen().camera.getWidth() / 2), 500, Color.white);
			Text.drawCenteredString("", (float) (Theater.get().getScreen().camera.getWidth() / 2), 550, Color.decode("#A8A8A8"));
		} else {
			Text.drawCenteredString("" + promptText, (float) (Theater.get().getScreen().camera.getWidth() / 2), 500, Color.white);
		}
	}
	
	public void win() {
		won = true;
	}
	
	public void addEvent(String event) {
		this.events.add(event);
	}
	
	public String getEvent() {
		return events.get(0);
	}
	
	public List<String> getEvents() {
		return events;
	}
	
	public void setAutoComplete(boolean autoComplete) {
		this.autoComplete = autoComplete;
	}
	
	public boolean isWon() {
		return won;
	}
	
	public boolean isCompleted() {
		return completed;
	}
}
