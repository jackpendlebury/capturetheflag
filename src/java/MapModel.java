import java.util.Random;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class MapModel extends GridWorldModel {
		
	//Constants for the Environment Objects
	public static final int RED_BASE = 8;
	public static final int BLU_BASE = 16;
    public static final int FLAG 	 = 32;
	
	//Grid Size (Keep it odd, or it looks strange)
	public static final int GSize 	 = 17;
	
	//Number of Mobile Agents [MUST REMEMBER TO CHANGE]
	public static int 		TotAgt   = 1;
	
	//Team Bases
	static final Location rBase = new Location(round(GSize/2),round(GSize-1));
	static final Location bBase = new Location(round(GSize/2),0);
	
	//Environmental Variables
	InterfacePercept percept = new Perception();
	Flag flag 		   = new Flag();
	//Scores
	int rscore = 0; int bscore = 0;
		
	public MapModel() {
		//Create the Grid
		super(GSize, GSize, TotAgt);
		//set initial locations and teams of agents
        Perception.initTeams();
		initAgents();
		//Load the Preset Wall Configuration (0 for None)
		initMaps(0);
		//Places the map objects onto the grid
		add(BLU_BASE, bBase);
		add(RED_BASE, rBase);
		add(FLAG, flag.getFlagStartingLoc());
	}
	
	void initMaps(int i){
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
	
	void initAgents() {
		//Detects a player's team, then places them in an appropriate position.
		Random random = new Random();
		if(GSize >= TotAgt){
			for(int i = 1; i <= TotAgt; i++){
				//Each agent is generated a random x coordinate, then placed on the y coordinate based on their team.
				int r = random.nextInt(GSize-1) + 1;
				if(percept.getTeam(i-1) == "red"){
					setAgPos((i-1), r, rBase.y - 1);
				} else if(percept.getTeam(i-1) == "blue"){
					setAgPos((i-1), r, bBase.y + 1);
				}
			}
		} else {
			System.out.println("Grid Size must be greater than the total number of Agents");
		}
	}

	//TODO: Better Round function
	static int round(double d){
		int x = (int) d;
		return x;
	}
	
	//Action Functions
	
	//Agent Movement Action
	boolean moveTo(int id, int dx, int dy){
		Location lplayer = getAgPos(id);
		Location dest = new Location(dx, dy);
//		System.out.println("dest = " + dx + "," + dy);
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
			setAgPos(id, p.x, p.y);
		}

		if (view != null) {
			//Where the flag gets carried by the agent
			if(flag.flagCarried && id == flag.agentCarrying) 
				flag.setFlagLoc(lplayer);
        }
        return true;
	}
	
	public boolean pickupFlag(String agName){
		if(!flag.flagCarried && flag.agentCarrying == -1){
			flag.setFlagCarried(true);
			flag.setAgentCarrying(getAgentID(agName));
			System.out.println("Agent Carrying = " + flag.getAgentCarrying());
			System.out.println(agName + " has the Flag!");
			return true;
		} else {
			return false;
		}
	}
	
	public boolean scoreFlag(String agName){
		//Drop the Flag
		flag.dropFlag();
		//Increase the Score for the respective team.
		if(percept.getTeam(getAgentID(agName)) == "red"){
			rscore++;
		} else if(percept.getTeam(getAgentID(agName)) == "blue") {
			bscore++;
		}
		//Print Score + Reset Flag Location
		System.out.println(agName + " has Scored! The Score is Red - " + rscore + " Blue - " + bscore);
		flag.setFlagLoc(flag.getFlagStartingLoc());
		return true;
	}
		
	public boolean takeFlag(String agName){
		int id = getAgentID(agName);
		Location p = getAgPos(id);
		//If the flag is being carried, and is in a neighbouring space to the agent
		if(flag.flagCarried && flag.getFlagLoc().isNeigbour(p)){
			//Return the victim to their base.
			setAgPos(flag.getAgentCarrying(), percept.getTeamBase(id));
			//Set the tackler as the flagholder, and places them in the victim's place.
			flag.setAgentCarrying(id);
			setAgPos(id, flag.getFlagLocX(), flag.getFlagLocY());
			return true;
		}
		return false;
	}
		
	public int getAgentID(String agName){
		//IMPORTANT: All Agent names must be the same length, with the integer in the same place.
		char[] l = new char[8];
		l = agName.toCharArray();
		//The Number (l[x]) is the location of the number in the name.
		int i = Character.getNumericValue(l[6]); i -= 1;
//		System.out.println(agName + " = ID:" + i);
		return i;
	}
		
}
