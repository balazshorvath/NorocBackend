package hu.noroc.gameworld.components.scripting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Command {
	
	long time = 0;
	
	static Map<String, Integer> commandTypes = new HashMap<String, Integer>() {
	    {
	        put("SPAWN", 0);
	        put("CAST", 1);
	        put("MOVE", 2);
	        put("RESPAWN", 3);
	        put("WAIT", 4);
	    }
	};
	
	static Map<String, Integer> timingTypes = new HashMap<String, Integer>() {
	    {
	        put("REPEAT", 0);
	        put("ONCE", 1);
	    }
	};
	
	int commandType;
	int timingType;
	
	

	public Command(String commandText){
		
	}
	
	public boolean needsToRun(long tick){
		if(timingType == 1){
			if(tick == time){
				return true;
			}
		}else if(tick%time == 0){
			return true;
		}
		return false;
	}
	
}
