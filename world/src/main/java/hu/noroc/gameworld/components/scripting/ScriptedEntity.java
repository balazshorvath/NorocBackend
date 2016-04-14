package hu.noroc.gameworld.components.scripting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class ScriptedEntity {

	/**
	 * The stated of scripted entities 
	 */
	public enum State{
		INIT,
		IDLE,
		COMBAT,
		FLEE,
		DEAD
	}
	
	Map<ScriptedEntity.State,String> stateFiles;
	
	Map<ScriptedEntity.State,List<Command>> stateCommandLists;
	
	int currentTic = 0;
	
	protected ScriptedEntity.State currentState;
	
	protected void stateChanged(ScriptedEntity.State newState){
		
	}
	
	public void tick(){
		if (currentState != State.INIT) {
			currentTic++;
			for (Command cmd : stateCommandLists.get(currentState)) {
				if (cmd.needsToRun(currentTic)) {

				}
			}
		}

	}
	
	public ScriptedEntity(String initFileName, Ticker areaTicker) throws IOException{
		areaTicker.start();
		areaTicker.subscribe(this);
		currentState = State.INIT;
		BufferedReader initFileReader = new BufferedReader(new FileReader(new File(initFileName)));
		while(true){
			String line = initFileReader.readLine();
			if(line == null) break;
			String[] data = line.split(" ");
			if(data[0].equalsIgnoreCase("LOAD")){
				if(data[1].equalsIgnoreCase(State.COMBAT.name())){
					stateFiles.put(State.COMBAT, data[2]);
				}else if(data[1].equalsIgnoreCase(State.IDLE.name())){
					
				}else if(data[1].equalsIgnoreCase(State.DEAD.name())){
					
				}else if(data[1].equalsIgnoreCase(State.FLEE.name())){
					
				}
			}
			
		}
		
		
	}
	
	/**
	 * Run a command on the selected entity from ingame console
	 * @param command
	 */
	public void runCommand(Command command){
		
	}
	
	/**
	 * Initiates a move on the entity from a script
	 * @param x
	 * @param y
	 */
	protected abstract void moveCallback(int x, int y);
	
	/**
	 * Initiates a spellcast on the entity from a script
	 * @param spellID
	 */
	protected abstract void castCallback(String spellID);
	
	/**
	 * Setting deafult entity parameter from the script files
	 * @param parameters
	 */
	protected abstract void setEntityDefaultParameters(Map<String,String> patameters);
	
	
}

