package capturetheflag;
import java.util.ArrayList;

import jason.asSyntax.*;
import capturetheflag.MapEnv;
import jason.environment.*;
import jason.environment.grid.*;


public class Perception {
	
	private static final Literal af  = Literal.parseLiteral("at(player,flag)");
	private static final Literal ab  = Literal.parseLiteral("at(player,base)");
	private static final Literal nf  = Literal.parseLiteral("near(flag)");
	private static final Literal nb  = Literal.parseLiteral("near(base)");
	private static final Literal nfh = Literal.parseLiteral("near(flagholder)");
	
//	private static final Literal npf = Literal.parseLiteral("near(player, friendly)");
//	private static final Literal npn = Literal.parseLiteral("near(player, nasty)");
	
	public static ArrayList<String> teamList = new ArrayList<String>();
	
	public Perception(){
		
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

	public static ArrayList<Literal> atLocation(String agName){
		MapModel perceptModel = MapEnv.model;
		ArrayList<Literal> atLoc = new ArrayList<Literal>();
		if(perceptModel != null){
			//Initialise the Agent Variables
			int 	 id = perceptModel.getAgentID(agName);
			Location lplayer = perceptModel.getAgPos(id); 
			Flag  	 perceptFlag = perceptModel.flag;
			
			//Return the Literals for adding the Perceptions
	        if (lplayer.equals(perceptFlag.getFlagLoc())) {
	        	atLoc.add(af);
	        }
	        if (lplayer.equals(getTeamBase(id))){
	        	atLoc.add(ab);
	        }
	        return atLoc;
		} return null;
	}
	
	//TODO: Sight not quite ready yet
	private void lookAround(){
//		Location l = lplayer;
		for(int y = -2; y <= 2; y++){
			for(int x = -2; x <= 2; x++){
//				l.x = lplayer.x + x;
//				l.y = lplayer.y + y;
//				see(l);
			}
		}
		for(int y = -3; y <= 3; y+=6){
			for(int x = 3; x <= 3; x+=6){
//				l.x = lplayer.x + x;
//				l.y = lplayer.y + y;
//				see(l);
			}
		}
	}
	
	private void see(Location l){
//		if(perceptModel.getAgAtPos(l) != -1 && l.equals("FLAG"))
//			perceptEnv.addPercept(nfh);
//		if(l.equals("OBSTICLE"))
//			perceptEnv.addPercept("Wall"); //Fix the pathfinding before this.
//		if(l.equals("FLAG"))
//			perceptEnv.addPercept(nf);
//		if(teamList.get(id) == "red" && l.equals("RED_BASE"))
//			perceptEnv.addPercept(nb);
//		if(teamList.get(id) == "blue" && l.equals("BLU_BASE"))
//			perceptEnv.addPercept(nb);
	}

}
