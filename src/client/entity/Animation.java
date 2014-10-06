package client.entity;

import client.Theater;
import client.utility.Event;

public class Animation extends Entity {

	protected boolean animating = true;
	protected boolean paused;
	protected boolean loop;
	protected boolean removeWhenFinished = true;
	
	public Animation(String ref, float x, float y, boolean paused) {
		super(ref, x, y);
		this.paused = paused;
	}
	
	public void update() {
		move();
		
		if(!paused) {
			if(sprite.getFrame() == sprite.getMaxFrame() - 1)
				animating = false;
			sprite.animate();
			if(sprite.getFrame() == 0 && !animating) {
				if(removeWhenFinished)
					Theater.get().getStage().removeAnimation(this);
				else if(loop)
					animating = true;
				else
					paused = true;
				Event.get().finishAction();
			} else
				animating = true;
		}
	}
	
	public boolean isAnimating() {
		return animating;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	public void setRemoveWhenFinished(boolean remove) {
		this.removeWhenFinished = remove;
	}
	
}
