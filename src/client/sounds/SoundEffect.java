package client.sounds;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.easyogg.OggClip;

public class SoundEffect {

	private OggClip clip;
	private String name;
	private float volume;
	private boolean loop;
	
	public SoundEffect(String ref, float volume, boolean loop) {
		this.name = ref.substring(0, ref.lastIndexOf("."));
		try {
			clip = new OggClip(new FileInputStream(System.getProperty("resources") + "/sounds/" + ref));
			this.volume = volume;
			this.loop = loop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		if(Ensemble.get().getMasterVolume() < volume)
			clip.setGain(Ensemble.get().getMasterVolume());
		else
			clip.setGain(volume);
		clip.play();
	}
	
	public String getName() {
		return name;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}
	
	public void adjustVolume(float masterVolume) {
		if(masterVolume < volume) {
			clip.setGain(masterVolume);
		} else
			clip.setGain(volume);
	}
	
	public void setLoop(boolean loop) {
		this.loop = loop;
		if(loop)
			clip.loop();
	}
	
	public boolean isLoop() {
		return loop;
	}
	
	public OggClip getClip() {
		return clip;
	}
	
}
