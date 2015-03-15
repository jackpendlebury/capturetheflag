package capturetheflag;
import jason.environment.grid.Location;

public class Flag{
	
	//Flag Starts in the center, AgentCarrying = -1 means flag not being carried.
	public Location flagStartingLoc = new Location(round(MapModel.GSize-2), round(MapModel.GSize/2));
	public Location flagLoc = flagStartingLoc;
	public boolean flagCarried = false;
	public int agentCarrying = -1;
	
	public Flag() {
		
	}
		
	public Location getFlagStartingLoc() {
		return flagStartingLoc;
	}

	public Location getFlagLoc() {
		return flagLoc;
	}
	
	public int getFlagLocX() {
		return flagLoc.x;
	}

	public int getFlagLocY() {
		return flagLoc.y;
	}

	public boolean isFlagCarried() {
		return flagCarried;
	}

	public int getAgentCarrying() {
		return agentCarrying;
	}
	
	public void dropFlag(){
		setAgentCarrying(-1);
		flagCarried = false;
	}

	public void setFlagLoc(Location flagLoc) {
		this.flagLoc = flagLoc;
	}

	public void setFlagCarried(boolean flagCarried) {
		this.flagCarried = flagCarried;
	}
	
	public void setAgentCarrying(int agentCarrying) {
		this.agentCarrying = agentCarrying;
	}

	int round(double d){
		int x = (int) d;
		return x;
	}
}
