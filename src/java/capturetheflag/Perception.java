package capturetheflag;

import java.util.ArrayList;

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.*;

public class Perception extends Environment implements InterfacePercept {
	
	/** String Literals of possible Perceptions */
	
	private static final Literal af  = Literal.parseLiteral("at(player,flag)");
	private static final Literal ab  = Literal.parseLiteral("at(player,base)");
	private static final Literal ft  = Literal.parseLiteral("flag_taken");
	private static final Literal hf  = Literal.parseLiteral("have(flag)");
	
	/**	ArrayList containing the Team Allocations */
		
	public static ArrayList<String> teamList = new ArrayList<String>();
	static MapModel perceptModel;
	
	/**
	 * Agent Current Location Perceptions Method
	 * 
	 * Parameters: 
	 * agName(String) - Active Agent's full name
	 * 
	 * Returns:
	 * ArrayList<Literal> - ArrayList of all Perceptions to be added by the Current Location.
	 */
	
	public ArrayList<Literal> atLocation(String agName){
		perceptModel = MapEnv.model;
		ArrayList<Literal> atLoc = new ArrayList<Literal>();
		if(perceptModel != null && agName != null){
			//Initialise the Agent Variables
			Flag  	 perceptFlag = perceptModel.flag;
			int 	 id = perceptModel.getAgentID(agName);
			Location lplayer = perceptModel.getAgPos(id); 

			//Call the Literals for adding the Perceptions
			if(perceptModel.flag.agentCarrying == id){
				atLoc.add(hf);
			}
	        if (lplayer.equals(perceptFlag.getFlagLoc()) && !perceptFlag.flagCarried){
	        	atLoc.add(af);
	        } else if(perceptFlag.flagCarried && perceptFlag.agentCarrying != id){
	        	atLoc.add(ft);
	        }
	        if (lplayer.equals(getTeamBase(id)))
	        	atLoc.add(ab);
	        return atLoc;
		} return null;
	}
	
	/**
	 * Agent Nearby Perceptions Method
	 * 
	 * Parameters: 
	 * agName(String) - Active Agent's full name
	 * 
	 * Returns:
	 * ArrayList<Literal> - ArrayList of all Perceptions to be added by nearby Agent's and Objects.
	 */
	
	public ArrayList<Literal> lookAround(String agName){
		perceptModel = MapEnv.model;
		ArrayList<Literal> lookArr  = new ArrayList<Literal>();
		int 	 id 	 	  = perceptModel.getAgentID(agName);
		Location lplayer 	  = perceptModel.getAgPos(id); 
		if(agName != null){
			/** Searching direct neighbours to the Agent's location*/
			for(int y = -1; y <= 1; y++){
				for(int x = -1; x <= 1; x++){
					if(!(x == 0 && y == 0)){
						Location l = new Location(lplayer.x + x, lplayer.y + y);
//						System.out.println("Player 1 Loc: (" + lplayer.x + "," + lplayer.y + "):: See Loc (" + l.x + "," + l.y + ")");
						if(perceptModel.getAgAtPos(l) != -1){
							int agt = perceptModel.getAgAtPos(l);
							String plyr = perceptModel.getAgName(agt);
							/** Detects and adds nearby agents flagholder status perception */
							if(perceptModel.flag.agentCarrying == perceptModel.getAgentID(plyr)){
								Literal nfh = Literal.parseLiteral("flagholder(" + plyr + ")");
								lookArr.add(nfh);
							} else {
								Literal np = Literal.parseLiteral("close(" + plyr + ")"); 
								lookArr.add(np);
							}
						}
					}
				}
			}
			/** Searching an single space further in the cardinal directions*/
			for(int y = -2; y <= 2; y+=4){
				for(int x = -2; x <= 2; x+=4){
					Location l = new Location(lplayer.x + x, lplayer.y + y);
	//				System.out.println("Player Loc: (" + lplayer.x + "," + lplayer.y + "):: See Loc (" + l.x + "," + l.y + ")");
					if(perceptModel.getAgAtPos(l) != -1 ){
						int 	agt = perceptModel.getAgAtPos(l);
						String plyr = perceptModel.getAgName(agt);
						/** Detects and adds nearby agents flagholder status perception */
						if(perceptModel.flag.agentCarrying == perceptModel.getAgentID(plyr)){
							Literal nfh = Literal.parseLiteral("flagholder(" + plyr + ")");
							lookArr.add(nfh);
						} else {
							Literal np = Literal.parseLiteral("close" + plyr + ")"); 
							lookArr.add(np);
						}
					}
				}
			}
			/** Returns completed ArrayList */ 
			return lookArr;
		} else return null;
	}
	
	/** Team Initialisation Function */
	
	public static void initTeams(){
		for(int i = 0; i <= MapModel.TotAgt; i++){
			//If the player ID is even, the player is Blue
			if(i % 2 == 0){
				teamList.add("blue");
			} else {
			//Else the player is Red
				teamList.add("red");
			}
		}
		System.out.println("Teams Initialised");
	}
	
	/** Team Allegiance reference function */

	public String getTeam(int id){
		return teamList.get(id);
	}
	
	/** Team Base Location reference function */
	
	public Location getTeamBase(int id){
		//Returns the Agents base
		if(teamList.get(id) == "red"){
			return MapModel.rBase;
		} else if(teamList.get(id) == "blue") {
			return MapModel.bBase;
		} else {
			return null;
		}
	}

}
