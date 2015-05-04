package capturetheflag;
import java.util.Random;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class MapModel extends GridWorldModel {
		
	/** Constants for the Environment Objects */
	public static final int RED_BASE = 32;
	public static final int BLU_BASE = 16;
    public static final int FLAG 	 = 8;
	
	/** Grid Size (Keep it odd, or it looks strange) */
	public static int GSize 	 = 15;
	
	/** Number of Mobile Agents [MUST REMEMBER TO CHANGE] */
	public static int 		TotAgt   = 2;
	
	/** Team Bases */
	public static final Location rBase = new Location(round(GSize/2),round(GSize-1));
	public static final Location bBase = new Location(round(GSize/2),0);
	
	/** Environmental Variables */
	InterfacePercept percept = new Perception();
	public Flag 	 flag 	 = new Flag(round(MapModel.GSize/2), round(MapModel.GSize/2)); 	
	
	/** Game Scores */
	public int rscore = 0; public int bscore = 0;
	
	/** Model Constructor */
	public MapModel() {
		//Create the Grid
		super(GSize, GSize, TotAgt);
		//set initial locations and teams of agents
        Perception.initTeams();
		initAgents();
		//Load the Preset Wall Configuration (0 for None)
		initMap(0);
		//Places the map objects onto the grid
		add(BLU_BASE, bBase);
		add(RED_BASE, rBase);
		add(FLAG, flag.getFlagLoc());
	}
	
	/** Map Initialisation Function
	 * 
	 *  NOTE: Incomplete due to failure to integrate pathfinding algorithm*/
	
	void initMap(int i){
		switch(i){
			case 1:
				//One Horizontal Centre wall
				addWall(0, round(GSize/2), round(GSize/2) - 1, round(GSize/2));
				addWall(round(GSize/2) + 1, round(GSize/2), GSize - 1, round(GSize/2));
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			default:
				break;
		}
	}
	
	/** Agent Initialisation Method
	 * 
	 *  Returns:
	 *  Boolean - Flag to indicate function success
	 *  */
	
	public boolean initAgents() {
		//Detects a player's team, then places them in an appropriate position.
		Random random = new Random();
		if(GSize >= TotAgt && GSize != 0 && TotAgt != 0){
			for(int i = 1; i <= TotAgt; i++){
				//Each agent is generated a random x coordinate, then placed on the y coordinate based on their team.
				int r = random.nextInt(GSize-1) + 1;
				if(percept.getTeam(i-1) == "red"){
					setAgPos((i-1), r, rBase.y - 1);
				} else if(percept.getTeam(i-1) == "blue"){
					setAgPos((i-1), r, bBase.y + 1);
				}
			}
			return true;
		} else {
//			System.out.println("Grid Size must be greater than the total number of Agents");
			return false;
		}
	}
	
	
	/** Double -> Int Rounding Function 
	 * 
	 * Parameters: 
	 * Double d - Double to be Rounded down to next whole Integer
	 * 
	 * Returns:
	 * int - Rounded down double
	 * */
	public static int round(double d){
		int x = (int) d;
		return x;
	}
	
	/** Action Functions */
	
	/** Agent Movement Action Function 
	 * 
	 * Parameters: 
	 * int id - Active Agent's ID
	 * int dx - Destination X Co-Ordinate
	 * int dy - Destination Y Co-Ordinate
	 * 
	 * Returns:
	 * boolean - Function Success Indicator
	 * 
	 * */
	boolean moveTo(int id, int dx, int dy){
		Location lplayer = getAgPos(id);
		Location dest = new Location(dx, dy);
		//If the player is currently at the destination, do nothing.
		if(lplayer == dest){
			return false;
		} else {
			Location p = lplayer;
			
			if (p.x < dest.x){
				p.x++;
			} else if (p.x > dest.x){
				p.x--;
			}

			if (p.y < dest.y){
				p.y++;
			} else if (p.y > dest.y){
				p.y--;
			}
			//If the Location is unoccupied, move into it.
			if(isFree(p)){
				setAgPos(id, p.x, p.y);
			}
		}
		
		if (view != null) {
			//If the Flag is being carried it's location is the same as the Agent Carrying.
			if(flag.flagCarried && id == flag.agentCarrying){
				flag.setFlagLoc(lplayer);
			}
			view.update();
        }
        return true;
	}
	
	/** Pick Up Flag Action Function 
	 * 
	 * Parameters: 
	 * String agName - Active Agent's Full Name
	 * 
	 * Returns:
	 * boolean - Function Success Indicator
	 * 
	 * */
	
	public boolean pickupFlag(String agName){
		if(!flag.flagCarried){
			flag.setFlagCarried(true);
			flag.setAgentCarrying(getAgentID(agName));
			System.out.println("Agent Carrying = " + flag.getAgentCarrying());
			System.out.println(agName + " has the Flag!");
			return true;
		} else {
			return false;
		}
	}
	
	/** Score Flag Action Function 
	 * 
	 * Parameters: 
	 * String agName - Active Agent's Full Name
	 * 
	 * Returns:
	 * boolean - Function Success Indicator
	 * 
	 * */
	
	public boolean scoreFlag(String agName){
		if(flag.agentCarrying == getAgentID(agName)){
			//Drop the Flag
			flag.dropFlag();
			//Increase the Score for the respective team.
			if(percept.getTeam(getAgentID(agName)) == "red"){
				rscore++;
			} else if(percept.getTeam(getAgentID(agName)) == "blue") {
				bscore++;
			}
			//Reset the Flag Object Location
			remove(FLAG, flag.getFlagLoc());
			flag.setFlagLoc(flag.getRandomFlagLoc());
			add(FLAG, flag.getFlagLoc());
			//Print Score + Reset Flag Location
			System.out.println(agName + " has Scored! The Score is Red - " + rscore + " Blue - " + bscore);
			return true;
		} else {
			return false;
		}
	}
	
	/** Tackle Agent Action Function 
	 * 
	 * Parameters: 
	 * String agName - Active Agent's Full Name
	 * String tName - Target Agent's Full Name
	 * 
	 * Returns:
	 * boolean - Function Success Indicator
	 * 
	 * */
		
	public boolean takeFlag(String agName, String tName){
		int target = getAgentID(tName);
		//If the flag is being carried, and is in a neighbouring space to the agent
		if(flag.flagCarried && flag.agentCarrying == target){
			flag.dropFlag();
			remove(FLAG, flag.getFlagLoc());
			//Return the target to their base.
			setAgPos(target, percept.getTeamBase(target));
			return true;
		}
		return false;
	}
	
	/** Function to Convert Agent Full Name to ID 
	 * 
	 * Parameters:
	 * String agName - Agent's Full Name
	 * 
	 * Returns:
	 * int - Relevant Agent's ID
	 * 
	 * */
		
	public int getAgentID(String agName){
		//IMPORTANT: All Agent names must be the same length, with the integer in the same place.
		char[] l = new char[8];
		l = agName.toCharArray();
		//The Number (l[x]) is the location of the number in the name.
		int i = Character.getNumericValue(l[6]); i -= 1;
		return i;
	}
	
	/** Function to Convert Agent ID to Full Name 
	 * 
	 * Parameters:
	 * int - Relevant Agent's ID
	 * String agName - Agent's Full Name
	 * 
	 * Returns:
	 * String - Agent's Full Name
	 * 
	 * */
	
	public String getAgName(int id){
		return "player" + (id+1);
	}
		
}
