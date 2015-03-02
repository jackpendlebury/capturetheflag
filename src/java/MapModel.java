//JMoise is IMPORTABLE :D
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class MapModel extends GridWorldModel {
		
	//Constants for the Environment Objects
	public static final int BLU_BASE = 32;
	public static final int RED_BASE = 16;
    public static final int FLAG  = 64;
	
	//Grid Size (try to keep it odd, or it looks strange)
	public static final int GSize = 13;
	
	//Number of Mobile Agents [MUST REMEMBER TO CHANGE]
	public static int TotAgt = 1;
	
	//Environmental Variables
	Flag flag = new Flag();
	int rscore = 0; int bscore = 0;
	
	//Object Starting positions
	Location rBase = new Location(round(GSize/2),round(GSize-1));
	Location bBase = new Location(round(GSize/2),0);
	
	public MapModel() {
		//Create the Grid
		super(GSize, GSize, TotAgt);
		
		//set initial locations of agents
		initAgents();		
		
		//Places the map objects onto the grid
		add(BLU_BASE, bBase);
		add(RED_BASE, rBase);
		add(FLAG, flag.getFlagLoc());
	}
	
	void initAgents() {
		//Evenly spaces the agents out. Will later be placed in 2 different places based on team.
		int pos = round(GSize/TotAgt) - round(GSize/5);
		if(GSize >= TotAgt){
			for(int i = 1; i <= TotAgt; i++){
				System.out.println("player" + (i-1));
				setAgPos((i-1), pos*i, GSize-2);
			}
		} else {
			System.out.println("Grid Size must be greater than the total number of Agents");
		}
	}

	//TODO: Better Round function
	int round(double d){
		int x = (int) d;
		return x;
	}
	
	//Action Functions
	
	//Agent Movement Action
	boolean moveTowards(Location dest, String agName){
		int id = getAgentID(agName);
		Location p = getAgPos(id);
		if(p == dest){
			System.out.println("At destination");
			return false;
		}
		
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
		
		//Updates the Objects 
		setAgPos(id, p);
		if (view != null) {
			//Where the flag gets moved by the agent
			if(flag.flagCarried) flag.setFlagLoc(p);
			view.update();
        }
        return true;
	}
	
	public boolean pickupFlag(String agName){
		if(!flag.flagCarried){
			flag.setFlagCarried(true);
			flag.setAgentCarrying(getAgentID(agName));
			view.update();
			System.out.println(agName + " has the Flag!");
			return true;
		} else {
			return false;
		}
	}
	
	public boolean scoreFlag(String agName){
		//Drop the Flag
		flag.flagCarried = false;
		flag.setAgentCarrying(-1);
		//Increase the Score for the respective team.
		rscore++;
		//Reset Flag Location
		flag.setFlagLoc(new Location(round(GSize/2),round(GSize/2)));
		System.out.println(agName + " has Scored! The Score is Red - " + rscore + " Blue - " + bscore);
		return true;
	}
		
	public boolean takeFlag(String agName){
		int id = getAgentID(agName);
		Location p = getAgPos(id);
		if(flag.flagCarried && flag.getFlagLoc().isNeigbour(p)){
			setAgPos(id, p.x+1, p.y+1);
			flag.setAgentCarrying(id);
			return true;
		}
		return false;
	}
		
	public int getAgentID(String agName){
		//INCREDABLEY IMPORTANT: All Agent names must be the same lenght, with the integer in the same place.
		char[] l = agName.toCharArray();
		//The Number below (l[x]) is the location of the number in the name.
		int i = Character.getNumericValue(l[6]); i -= 1;
//		System.out.println(agName + " = ID:" + i);
		return i;
	}
	
	//TODO: Implement Team detection
	
	public String getAgentTeam(String agName){
		//Need an 2D array of id's w/ respective team. e.g. 0 - red, 1 - blue etc.
		return null;
	}

}
