package client.entity;

import client.Theater;
import client.utility.Event;

public class LongAnimation extends Animation {

	private int frame;
	private int maxFrame;
	private float animStep = 0f;
	
	public LongAnimation(String ref, float x, float y, boolean paused, int maxFrame) {
		super(ref, x, y, paused);
		this.frame = 1;
		this.maxFrame = maxFrame;
	}
	
	public void update() {
		move();
		
		if(!paused) {
			if(maxFrame > 1) {
				animStep += Theater.getDeltaSpeed(0.025f);
				if (animStep >= 0.05f) {
					animStep = 0f;
					frame++;
					String tempName = name.substring(0, name.lastIndexOf('_') + 1);
					sprite.setTexture(tempName + "00" + (frame < 10 ? "0" : "") + frame);
					if (frame >= maxFrame) {
						frame = 0;
						animating = false;
					}
				}
			}
	
			if(frame == 0 && !animating) {
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

}
