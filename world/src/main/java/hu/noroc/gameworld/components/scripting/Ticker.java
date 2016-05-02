package hu.noroc.gameworld.components.scripting;

import hu.noroc.gameworld.components.behaviour.LivingEntity;

import java.util.ArrayList;
import java.util.List;


public class Ticker {
	
	boolean enabled;
	
	Thread ticker;
	
	public static final int TICK_UNIT = 100; //milliseconds
	
	List<LivingEntity> tickSubscribers = new ArrayList<>();
	
	public void start() {
		if (!enabled) {
			enabled = true;
			createTicker();
			ticker.start();
		}
	}
	
	public void stop(){
		this.enabled = false;
	}
	
	public void subscribe(LivingEntity sbscrbr){
		tickSubscribers.add(sbscrbr);
	}
	
	public void unsubscribe(LivingEntity sbscrbr){
		tickSubscribers.remove(sbscrbr);
		if(tickSubscribers.isEmpty()){
			this.stop();
		}
	}
	
	private void createTicker(){
		ticker = new Thread(()->{
				while(enabled){
					tickSubscribers.forEach(LivingEntity::tick);
					try {
						Thread.sleep(TICK_UNIT);
					} catch (Exception ignored) {}
				}
			
		});
	}
	
}
