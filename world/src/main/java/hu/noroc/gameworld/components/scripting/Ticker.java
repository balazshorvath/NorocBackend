package hu.noroc.gameworld.components.scripting;

import java.util.ArrayList;
import java.util.List;


public class Ticker {
	
	boolean enabled;
	
	Thread ticker;
	
	final int baseTimeUnit = 100; //milliseconds
	
	List<ScriptedEntity> tickSubscribers = new ArrayList<ScriptedEntity>();
	
	public void start() {
		if (!enabled) {
			enabled = true;
			ticker = createTicker();
			ticker.start();
		}
	}
	
	public void stop(){
		this.enabled = false;
	}
	
	public void subscribe(ScriptedEntity sbscrbr){
		tickSubscribers.add(sbscrbr);
	}
	
	public void unsubscribe(ScriptedEntity sbscrbr){
		tickSubscribers.remove(sbscrbr);
		if(tickSubscribers.isEmpty()){
			this.stop();
		}
	}
	
	private Thread createTicker(){
					
		return new Thread(()->{
				while(enabled){
					for(ScriptedEntity subscriber : tickSubscribers){
						subscriber.tick();							
					}
					try {
						Thread.sleep(baseTimeUnit);
					} catch (Exception e) {}
				}
			
		});
	}
	
}
