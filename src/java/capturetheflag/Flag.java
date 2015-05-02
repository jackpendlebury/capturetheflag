package capturetheflag;
import java.util.Random;

import jason.environment.grid.Location;

public class Flag{
	
	//Flag Starts in the center, AgentCarrying = -1 means flag not being carried.
	public int x;
	public int y;
	public boolean flagCarried = false;
	public int agentCarrying = -1;
	
	Random random = new Random();
	
	public Flag(int x, int y){
		this.x = x;
		this.y = y;		
	}
	
	public Location getRandomFlagLoc(){
		int r = random.nextInt(MapModel.GSize);
		x= r; y = round(MapModel.GSize/2);
		return new Location(x,y);
	}

	public Location getFlagLoc() {
		return new Location(x,y);
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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
		flagLoc.x = x;
		flagLoc.y = y;
	}
	
	public void setFlagLoc(int x, int y) {
		this.x = x;
		this.y = y;
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
