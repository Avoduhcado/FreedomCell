package client.utility;

import client.Theater;
import client.entity.Actor;
import client.entity.Animation;
import client.entity.LongAnimation;
import client.sounds.Ensemble;
import client.sounds.SoundEffect;

public class Event {

	private boolean acting;
	
	private static Event event = new Event();
	
	public static Event get() {
		return event;
	}
	
	public void parseEvent(String event) {
		acting = true;
		
		String[] temp = event.split(";");
		if(temp[0].contains("Achievement")) {
			getAchievement(temp[1]);
		} else if(temp[0].contains("Fade Screen")) {
			fadeScreen(temp[1]);
		} else if(temp[0].contains("Fade While")) {
			fadeScreenWhile(temp[1]);
		} else if(temp[0].contains("Play Sound")) {
			playSound(temp[1]);
		} else if(temp[0].contains("Move Speed")) {
			moveAtSpeed(temp[1], Integer.parseInt(temp[2]), Float.parseFloat(temp[3]), Float.parseFloat(temp[4]));
		} else if(temp[0].contains("Move")) {
			move(temp[1], Integer.parseInt(temp[2]), Float.parseFloat(temp[3]));
		} else if(temp[0].contains("Add Animation")) {
			addAnimation(temp[1], Float.parseFloat(temp[2]), Float.parseFloat(temp[3]), Boolean.parseBoolean(temp[4]));
		} else if(temp[0].contains("Add Long Animation")) {
			addLongAnimation(temp[1], Float.parseFloat(temp[2]), Float.parseFloat(temp[3]), Boolean.parseBoolean(temp[4]), Integer.parseInt(temp[5]));
		} else if(temp[0].contains("Play Animation")) {
			playAnimation(temp[1], Boolean.parseBoolean(temp[2]), Boolean.parseBoolean(temp[3]));
		} else if(temp[0].contains("Remove Animation")) {
			removeAnimation(temp[1]);
		} else if(temp[0].contains("Add Actor")) {
			addActor(temp[1], Float.parseFloat(temp[2]), Float.parseFloat(temp[3]));
		} else if(temp[0].contains("Remove Actor")) {
			removeActor(temp[1]);
		} else if(temp[0].contains("Kill Actor")) {
			killActor(temp[1]);
		} else if(temp[0].contains("Remove Player")) {
			removePlayer();
		} else if(temp[0].contains("Deal")) {
			deal();
		}
		
		else if(temp[0].contains("Hang")) {
			
		} else {
			System.out.println("Unknown event: " + temp[0]);
			acting = false;
		}
	}
	
	public boolean inAction() {
		return acting;
	}
	
	public void finishAction() {
		acting = false;
	}
	
	public void getAchievement(String achievement) {
		if(!Achievements.valueOf(achievement).getGot()) {
			Achievements.valueOf(achievement).get();
			Achievements.valueOf(achievement).showGet(Theater.get().getStage());
		}
		acting = false;
	}
	
	public void fadeScreen(String time) {
		Theater.get().getScreen().setFadeTimer(Float.parseFloat(time));
		Theater.get().getStage().winList.get(0).setAutoComplete(true);
	}
	
	public void fadeScreenWhile(String time) {
		Theater.get().getScreen().setFadeTimer(Float.parseFloat(time));
		acting = false;
	}
	
	public void playSound(String sound) {
		Ensemble.get().playSoundEffect(new SoundEffect(sound, 1f, false));
		
		acting = false;
	}

	public void move(String entity, int direction, float distance) {
		Theater.get().getStage().getEntity(entity).setMovement(direction, distance);
	}
	
	public void moveAtSpeed(String entity, int direction, float distance, float speed) {
		Theater.get().getStage().getEntity(entity).setMovementWithSpeed(direction, distance, speed);
	}
	
	public void addAnimation(String animation, float x, float y, boolean paused) {
		Theater.get().getStage().addAnimation(new Animation(animation, x, y, paused));
		if(paused)
			acting = false;
	}
	
	public void addLongAnimation(String animation, float x, float y, boolean paused, int maxFrames) {
		Theater.get().getStage().addAnimation(new LongAnimation(animation, x, y, paused, maxFrames));
		if(paused)
			acting = false;
	}
	
	public void playAnimation(String animation, boolean removeWhenFinished, boolean loop) {
		Theater.get().getStage().getAnimation(animation).setPaused(false);
		Theater.get().getStage().getAnimation(animation).setRemoveWhenFinished(removeWhenFinished);
		Theater.get().getStage().getAnimation(animation).setLoop(loop);
	}
	
	public void removeAnimation(String animation) {
		Theater.get().getStage().removeAnimation(animation);
		acting = false;
	}
	
	public void addActor(String name, float x, float y) {
		Theater.get().getStage().addActor(new Actor(name, x, y));
		acting = false;
	}
	
	public void removeActor(String name) {
		Theater.get().getStage().removeActor(name);
		acting = false;
	}
	
	public void killActor(String name) {
		removeActor(name);
		acting = true;
	}
	
	public void removePlayer() {
		Theater.get().getStage().removePlayer();
	}
	
	public void deal() {
		Theater.get().getStage().playingField.deal();
	}
	
}
