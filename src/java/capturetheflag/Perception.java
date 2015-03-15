package capturetheflag;
import java.util.ArrayList;

import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.*;


public class Perception extends MapEnv {
	
	public static final Literal af  = Literal.parseLiteral("at(player,flag)");
	private static final Literal ab  = Literal.parseLiteral("at(player,base)");
	private static final Literal nf  = Literal.parseLiteral("near(flag)");
	private static final Literal nb  = Literal.parseLiteral("near(base)");
	private static final Literal nfh = Literal.parseLiteral("near(flagholder)");
	
//	private static final Literal npf = Literal.parseLiteral("near(player, friendly)");
//	private static final Literal npn = Literal.parseLiteral("near(player, nasty)");
	
	public static ArrayList<String> teamList = new ArrayList<String>();
	
	MapModel perceptModel; 
	Flag 	 perceptFlag;
	
	int 	 id; 
	String   agName; 
	Location lplayer;
	
	public void updatePercepts(String agent, int i){
		perceptModel = MapEnv.model;
		if(agent != null && perceptModel != null){
			id = i; agName = agent; 
			lplayer = perceptModel.getAgPos(i); 
			perceptFlag = perceptModel.flag;
			
			clearPercepts();
			atLocation();
//	        lookAround();
		} else {
			System.out.println("Null Pointer");
			this.stop();
		}
    }
	
	public String getTeam(int id){
		return teamList.get(id);
	}
	
	public static Location getTeamBase(int id){
		//Returns the Agents base, or their own location if something goes wrong
		if(teamList.get(id) == "red"){
			return MapModel.rBase;
		} else if(teamList.get(id) == "blue") {
			return MapModel.bBase;
		} else {
			return null;
		}
	}
	
	
	public static void initTeams(){
		for(int i = 0; i <= MapModel.TotAgt; i++){
			//If the player ID is even, the player is Red
			if(i % 2 == 0){
				teamList.add("blue");
			} else {
			//Else the player is blue
				teamList.add("red");
			}
		}
		System.out.println("Teams Initialised");
	}
	
	private void atLocation(){
//		System.out.println(agName + " Location: " + lplayer);
        if (lplayer.equals(perceptFlag.getFlagLoc())) {
        	//TODO: Not adding percepts.
            addPercept(agName, af);    
        }
        if (lplayer.equals(getTeamBase(id))){
        	addPercept(agName, ab);
        }
		System.out.println("AtLocation Cleared");
	}
	
	private void see(Location l){
		if(perceptModel.getAgAtPos(l) != -1 && l.equals("FLAG"))
			addPercept(nfh);
		if(l.equals("OBSTICLE"))
			addPercept("Wall"); //Fix the pathfinding before this.
		if(l.equals("FLAG"))
			addPercept(nf);
		if(teamList.get(id) == "red" && l.equals("RED_BASE"))
			addPercept(nb);
		if(teamList.get(id) == "blue" && l.equals("BLU_BASE"))
			addPercept(nb);
	}
	
	//TODO: Sight not quite ready yet
	private void lookAround(){
		Location l = lplayer;
		for(int y = -2; y <= 2; y++){
			for(int x = -2; x <= 2; x++){
				l.x = lplayer.x + x;
				l.y = lplayer.y + y;
				see(l);
			}
		}
		for(int y = -3; y <= 3; y+=6){
			for(int x = 3; x <= 3; x+=6){
				l.x = lplayer.x + x;
				l.y = lplayer.y + y;
				see(l);
			}
		}
	}

}
