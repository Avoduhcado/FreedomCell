package client.sounds;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.easyogg.OggClip;

import client.Theater;

public class Track {

	private OggClip clip;
	private String name;
	private float volume;
	private float fading;
	private float fadeIn;
	private float fadeOut;
	private boolean loop;
	
	public Track(String ref, float volume, float fadeIn, boolean loop) {
		try {
			clip = new OggClip(new FileInputStream(System.getProperty("resources") + "/sounds/" + ref));
			name = ref;
			this.volume = volume;
			if(fadeIn != 0) {
				this.fadeIn = fadeIn;
				this.volume = 0f;
				fading = fadeIn;
			}
			this.loop = loop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		if(fading != 0 && Ensemble.get().getMasterVolume() > 0f) {
			if(fading > 0) {
				fadeIn += Theater.getDeltaSpeed(0.025f);
				clip.setGain(volume += (((Ensemble.get().getMasterVolume() != 1f ? Ensemble.get().getMasterVolume() : volume) / fading) * Theater.getDeltaSpeed(0.025f)));
				if(fadeIn >= fading) {
					fading = 0;
					fadeIn = 0;
					clip.setGain(1f);
				}
			} else if(fading < 0) {
				fadeOut -= Theater.getDeltaSpeed(0.025f);
				clip.setGain(volume += (((Ensemble.get().getMasterVolume() != 1f ? Ensemble.get().getMasterVolume() : volume) / fading) * Theater.getDeltaSpeed(0.025f)));
				if(fadeOut <= fading || clip.getGain() == 0f) {
					fading = 0;
					fadeOut = 0;
					clip.setGain(0f);
				}
			}
		}
	}
	
	public void play() {
		if(Ensemble.get().getMasterVolume() < volume)
			clip.setGain(Ensemble.get().getMasterVolume());
		else
			clip.setGain(volume);
		clip.play();
	}
	
	public void pause() {
		clip.pause();
	}
	
	public void resume() {
		if(clip.isPaused())
			clip.resume();
	}
	
	public void adjustVolume(float masterVolume) {
		if(masterVolume < volume) {
			clip.setGain(masterVolume);
		} else
			clip.setGain(volume);
	}
	
	public OggClip getClip() {
		return clip;
	}
	
	public String getName() {
		return name;
	}

	public float getFadeIn() {
		return fadeIn;
	}
	
	public void setFadeIn(float fadeIn) {
		fading = fadeIn;
	}
	
	public float getFadeOut() {
		return fadeOut;
	}
	
	public void setFadeOut(float fadeOut) {
		fading = fadeOut;
	}
	
	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
}
