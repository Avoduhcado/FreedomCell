package client.utility;

import client.Stage;

public enum Achievements {

	QUITGAME (false, "Quit the game!");
	
	private boolean got;
	private String getText;
	
	Achievements(boolean got, String getText) {
		this.got = got;
		this.getText = getText;
	}
	
	public void get() {
		this.got = true;
	}
	
	public boolean getGot() {
		return got;
	}
	
	public String getGetText() {
		return getText;
	}
	
	public void showGet(Stage stage) {
		stage.addAchievementToShow(getGetText());
	}
	
}
