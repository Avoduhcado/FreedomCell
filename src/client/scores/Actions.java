package client.scores;

import client.Theater;

public class Actions {

	private int[] actions = new int[60];
	private int totalActions;
	private float timer;
	private long startTime;
	private long endTime;
	private long gameTime;
	
	public Actions() {
		for(int x = 0; x<actions.length; x++)
			this.actions[x] = 0;
		this.totalActions = 0;
		this.timer = 0f;
		this.startTime = Theater.getTime();
	}
	
	public void update() {
		int tempTime = (int) timer;
		timer += Theater.getDeltaSpeed(0.025f);
		
		if(timer >= 60f) {
			timer = 0f;
			actions[0] = 0;
		} else if((int) timer > tempTime) {
			actions[(int) timer] = 0;
		}
	}
	
	public void addAction() {
		actions[(int) timer]++;
		totalActions++;
	}
	
	public void end() {
		endTime = Theater.getTime();
		gameTime = endTime - startTime;
	}
	
	public int getAverageAPM() {
		return totalActions / ((1 + (int) ((Theater.getTime() - startTime) / 1000) / 60));
	}
	
	public int getOverallAPM() {
		return totalActions / ((1 + (int) (gameTime / 1000) / 60));
	}
	
	public int getCurrentAPM() {
		int tempActions = 0;
		for(int x = 0; x<actions.length; x++)
			tempActions += actions[x];
		
		return tempActions;
	}
	
}
