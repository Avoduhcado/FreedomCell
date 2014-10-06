package client.sounds;

import java.util.ArrayList;

public class Ensemble {

	private ArrayList<SoundEffect> soundEffects = new ArrayList<SoundEffect>();
	private ArrayList<Track> tracks = new ArrayList<Track>();
	private Track background;
	private float masterVolume = 1.0f;
	private boolean adjustVolume;
	
	private static Ensemble ensemble = new Ensemble();

	public static Ensemble get() {
		return ensemble;
	}
	
	public Ensemble() {
		setMasterVolume(1f);
	}

	public void update() {
		// If master volume has been changed, update all volumes
		if(adjustVolume) {
			// Sound effects
			for(int x = 0; x<soundEffects.size(); x++) {
				soundEffects.get(x).adjustVolume(masterVolume);
			}
			// Tracks
			for(int x = 0; x<tracks.size(); x++) {
				tracks.get(x).adjustVolume(masterVolume);
			}
			if(background != null)
				background.adjustVolume(masterVolume);
			
			// Reset volume adjustment variable
			adjustVolume = false;
		}
		
		// Handling sound effect clean up
		for(int x = 0; x<soundEffects.size(); x++) {
			if(soundEffects.get(x).getClip().stopped() && !soundEffects.get(x).isLoop()) {
				soundEffects.get(x).getClip().close();
				soundEffects.remove(x);
				x--;
			}
		}
		
		// Handling track clean up
		for(int x = 0; x<tracks.size(); x++) {
			tracks.get(x).update();
			if(tracks.get(x).getClip().stopped()) {
				tracks.get(x).getClip().close();
				tracks.remove(x);
				x--;
			}
		}
		
		if(background != null) {
			background.update();
			if(background.getClip().stopped())
				background.play();
		}
	}
	
	public void pause() {
		for(int x = 0; x<soundEffects.size(); x++) {
			soundEffects.get(x).getClip().pause();
		}
		for(int x = 0; x<tracks.size(); x++) {
			tracks.get(x).getClip().pause();
		}
		background.getClip().pause();
	}
	
	public void resume() {
		for(int x = 0; x<soundEffects.size(); x++) {
			soundEffects.get(x).getClip().resume();
		}
		for(int x = 0; x<tracks.size(); x++) {
			tracks.get(x).getClip().resume();
		}
		background.getClip().resume();
	}
	
	public void mute() {
		if(masterVolume == 0f)
			masterVolume = 1f;
		else
			masterVolume = 0f;
		
		adjustVolume = true;
	}
	
	public Track getBackground() {
		return background;
	}
	
	public void setBackground(Track track) {
		this.background = track;
	}
	
	public void swapBackground(Track track) {
		if(background != null) {
			this.background.getClip().stop();
			this.background.getClip().close();
		}
		this.background = track;
		this.background.play();
	}
	
	public Track getTrack(String name) {
		for(int x = 0; x<tracks.size(); x++) {
			if(tracks.get(x).getName().matches(name))
				return tracks.get(x);
		}
		
		return null;
	}
	
	public void playTrack(Track track) {
		tracks.add(track);
		tracks.get(tracks.size() - 1).play();
	}
	
	public void removeTrack(String name) {
		for(int x = 0; x<tracks.size(); x++) {
			if(tracks.get(x).getName().matches(name)) {
				tracks.get(x).getClip().stop();
				tracks.get(x).getClip().close();
				tracks.remove(x);
			}
		}
	}
	
	public SoundEffect getSoundEffect(String name) {
		for(int x = 0; x<soundEffects.size(); x++) {
			if(soundEffects.get(x).getName().matches(name))
				return soundEffects.get(x);
		}
		
		return null;
	}
	
	public void playSoundEffect(SoundEffect soundEffect) {
		soundEffects.add(soundEffect);
		if(soundEffects.get(soundEffects.size() - 1).isLoop())
			soundEffects.get(soundEffects.size() - 1).getClip().loop();
		else
			soundEffects.get(soundEffects.size() - 1).play();
	}
	
	public void removeSoundEffect(String name) {
		for(int x = 0; x<soundEffects.size(); x++) {
			if(soundEffects.get(x).getName().matches(name)) {
				soundEffects.get(x).getClip().stop();
				soundEffects.get(x).getClip().close();
				soundEffects.remove(x);
			}
		}
	}
	
	public float getMasterVolume() {
		return masterVolume;
	}

	public void setMasterVolume(float masterVolume) {
		this.masterVolume = masterVolume;
		adjustVolume = true;
	}
	
}
