package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import client.entity.Actor;
import client.entity.Animation;
import client.entity.Entity;
import client.entity.Prop;
import client.entity.cards.Card;
import client.menu.Chatlog;
import client.scores.ScoreScreen;
import client.sounds.Ensemble;
import client.sounds.Track;
import client.table.PlayingField;
import client.utility.WinCommand;

public class Stage {

	private Actor player;
	public Prop backdrop;
	public volatile PlayingField playingField;
	public volatile ScoreScreen scoreScreen;
	private Card selectedCard;
	public Card hoverCard;
	private ArrayList<Actor> actors = new ArrayList<Actor>();
	private ArrayList<Prop> props = new ArrayList<Prop>();
	private ArrayList<Animation> animations = new ArrayList<Animation>();
	public ArrayList<Entity> scenery = new ArrayList<Entity>();
	public List<WinCommand> winList = new LinkedList<WinCommand>();
	public Queue<String> achievementsToShow = new LinkedList<String>();
	private float showTimer;
		
	public String temp;
	
	public Stage() {
		loadStage("Casino");
		//playingField = new PlayingField();
		//playingField.deal();
	}
	
	public void loadStage(String ref) {
		actors.clear();
		props.clear();
		animations.clear();
		winList.clear();
		scenery.clear();
		
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/stages/" + ref));
			String line;
			String[] temp;
			while((line = reader.readLine()) != null) {
				if(line.matches("<BACKDROP>")) {
					temp = reader.readLine().split(";");
					backdrop = new Prop(temp[0], 0, 0);
				} else if(line.matches("<BGM>")) {
					temp = reader.readLine().split(";");
					Ensemble.get().swapBackground(new Track(temp[0], Float.parseFloat(temp[1]), Float.parseFloat(temp[2]), Boolean.parseBoolean(temp[3])));
				} else if(line.matches("<PROP>")) {
					temp = reader.readLine().split(";");
					props.add(new Prop(temp[0], Float.parseFloat(temp[1]), Float.parseFloat(temp[2])));
				} else if(line.matches("<PLAYER>") && player == null) {
					temp = reader.readLine().split(";");
					player = new Actor(temp[0], Float.parseFloat(temp[1]), Float.parseFloat(temp[2]));
				} else if(line.matches("<ACTOR>")) {
					temp = reader.readLine().split(";");
					actors.add(new Actor(temp[0], Float.parseFloat(temp[1]), Float.parseFloat(temp[2])));
				} else if(line.matches("<WIN>")) {
					reader.readLine();
					while(!(line = reader.readLine()).matches("<END>")) {
						temp = line.split(";");
						if(temp[0].startsWith("@")) {
							winList.get(winList.size() - 1).addEvent(line);
						} else if(!temp[0].startsWith("#")) {
							winList.add(new WinCommand(temp[0], temp[1]));
						}
					}
				}
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
	    	System.err.println("The scene file has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.err.println("Scene file failed to load!");
	    	e.printStackTrace();
	    }
		
		if(player != null || !props.isEmpty() || !actors.isEmpty())
			addScenery();
	}
	
	public void act() {
		Chatlog.get().update();
		for(int x = 0; x<scenery.size(); x++) {
			scenery.get(x).update();
		}
		
		if(temp != null) {
			playingField = new PlayingField(temp);
			temp = null;
		}
		
		if(playingField != null)
			playingField.update();
		
		if(scoreScreen != null) {
			scoreScreen.update();
		}
		
		if(selectedCard != null) {
			if(Mouse.isInsideWindow() && Input.mouseHeld) {
				selectedCard.setX(selectedCard.getX() + Mouse.getDX());
				selectedCard.setY(selectedCard.getY() + -Mouse.getDY());				
			}
		}
				
		/*if(!winList.isEmpty()) {
			winList.get(0).update();
			if(winList.get(0).isCompleted()) {
				winList.remove(0);
				if(winList.isEmpty()) {
					loadStage(nextStage);
				}
			} else if(winList.get(0).isWon() && !winList.get(0).getEvents().isEmpty()) {
				if(!Event.get().inAction()) {
					Event.get().parseEvent(winList.get(0).getEvent());
					winList.get(0).getEvents().remove(0);
				}
			}
		}*/
		
		/*if(!achievementsToShow.isEmpty()) {
			if(showTimer == 3f)
				Ensemble.get().playSoundEffect(new SoundEffect("Ding!.ogg", 1f, false));
			showTimer -= Theater.getDeltaSpeed(0.025f);
			if(showTimer <= 0f) {
				achievementsToShow.poll();
				if(!achievementsToShow.isEmpty())
					showTimer = 3f;
			}
		}*/
	}
	
	public void addScenery() {
		scenery.clear();
		
		if(player != null)
			scenery.add(player);
		if(!actors.isEmpty())
			scenery.addAll(actors);
		if(!props.isEmpty())
			scenery.addAll(props);
	}
	
	public void loadPlayingField(String field) {
		this.playingField = new PlayingField(field);
	}
	
	public void addAchievementToShow(String achievement) {
		achievementsToShow.add(achievement);
		if(showTimer <= 0f)
			showTimer = 3f;
	}
	
	public Card getSelectedCard() {
		return selectedCard;
	}

	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
		if(selectedCard != null)
			this.selectedCard.setHighlight(new Vector3f(0f, 1f, 1f));
		playingField.highlightOptions(selectedCard);
	}

	public void removePlayer() {
		scenery.remove(player);
		player = null;
	}
	
	public Entity getEntity(String name) {
		for(int x = 0; x<scenery.size(); x++) {
			if(scenery.get(x).getName().matches(name))
				return scenery.get(x);
		}
		
		return null;
	}
	
	public Actor getActor(String name) {
		for(int x = 0; x<actors.size(); x++) {
			if(actors.get(x).getName().matches(name))
				return actors.get(x);
		}
		
		return null;
	}
	
	public void addActor(Actor actor) {
		this.actors.add(actor);
		this.scenery.add(actor);
	}
	
	public void removeActor(String name) {
		for(int x = 0; x<actors.size(); x++) {
			if(actors.get(x).getName().matches(name)) {
				scenery.remove(actors.get(x));
				actors.remove(x);
			}
		}
	}
	
	public void addAnimation(Animation animation) {
		this.animations.add(animation);
		scenery.add(animation);
	}
	
	public Animation getAnimation(String animation) {
		for(int x = 0; x<animations.size(); x++) {
			if(animations.get(x).getName().matches(animation))
				return animations.get(x);
		}
		
		return null;
	}
	
	public void removeAnimation(Animation animation) {
		this.animations.remove(animation);
		scenery.remove(animation);
	}
	
	public void removeAnimation(String animation) {
		for(int x = 0; x<animations.size(); x++) {
			if(animations.get(x).getName().matches(animation)) {
				scenery.remove(animations.get(x));
				this.animations.remove(animations.get(x));
			}
		}
	}
	
}
