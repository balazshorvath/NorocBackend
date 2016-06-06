package hu.noroc.gameworld.components.scripting;

import hu.noroc.gameworld.components.behaviour.ActingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Ticker {
	
	boolean enabled;
	
	Thread ticker;
	
	public static final int TICK_UNIT = 100; //milliseconds
	
	List<ActingEntity> tickSubscribers = new CopyOnWriteArrayList<>();
	
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
	
	public void subscribe(ActingEntity sbscrbr){
		tickSubscribers.add(sbscrbr);
	}
	
	public void unsubscribe(ActingEntity sbscrbr){
		tickSubscribers.remove(sbscrbr);
		if(tickSubscribers.isEmpty()){
			this.stop();
		}
	}
	
	private void createTicker(){
		ticker = new Thread(()->{
				while(enabled){
					tickSubscribers.forEach(actingEntity -> {
						try{
                            actingEntity.tick();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
					});
					try {
						Thread.sleep(TICK_UNIT);
					} catch (Exception ignored) {}
				}
			
		});
	}
	
}
