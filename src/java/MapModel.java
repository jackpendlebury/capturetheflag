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
	
	//Number of Mobile Agents [MUST REMEBER TO CHANGE]
	public static final int TotAgt = 2;
	
	//Environmental Variables
	boolean carryingFlag = false;
	int rscore = 0; int bscore = 0;
	String currentAgent;
	
	//Object Starting positions
	Location rBase = new Location(round(GSize/2),round(GSize-1));
	Location bBase = new Location(round(GSize/2),0);
	Location flag = new Location(round(GSize/2), round(GSize/2));
	
	public MapModel() {
		//Create the Grid
		super(GSize, GSize, TotAgt);
		
		//set initial locations of agents
		setAgPos(0, round(GSize/2), GSize-3);//Test Position
		initAgents();		
		
		add(BLU_BASE, bBase);
		add(RED_BASE, rBase);
		add(FLAG, flag);
	}
	
	@SuppressWarnings("unused")
	void initAgents() {
		int pos = round(GSize/TotAgt);
		if(GSize >= TotAgt){
			for(int i = 1; i <= TotAgt; i++){
				setAgPos((i-1), 4*i, GSize-1);
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
	
	//Main Agent Movement Action
	boolean moveTowards(Location dest, int id){
		Location p = getAgPos(id);
		if(p == dest){
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
		
		//Where the flag gets moved by the agent
		//TODO: May be broken, can't tell yet
		setAgPos(id, p);
		if(carryingFlag){
			flag = p;
			if (view != null) {
	            view.update(flag.x,flag.y);
	        }
		}
        return true;
	}
	
	//I think this is the problem
	boolean pickupFlag(){
		if(!carryingFlag){
			carryingFlag = true;
			return true;
		} else {
			return false;
		}
	}
	
	boolean scoreFlag(){
		if(carryingFlag && flag == rBase){
			carryingFlag = false;
			rscore++;
			flag = new Location(0,0);
			return true;
		}
		return false;
	}

	public boolean setCurrentAgent(String agName) {
		currentAgent = agName;
		System.out.println(agName + " say's Hello");
		return true;
	}
	
	public int getAgentID(String agName){
		char[] l = agName.toCharArray();
		int i = l[7];
		return i;
	}

}
