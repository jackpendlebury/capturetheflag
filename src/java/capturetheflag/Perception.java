package capturetheflag;

import java.util.ArrayList;

import jason.asSyntax.*;
import jason.environment.grid.*;

public class Perception {
	
	private static final Literal af  = Literal.parseLiteral("at(player,flag)");
	private static final Literal ab  = Literal.parseLiteral("at(player,base)");
	private static final Literal nf  = Literal.parseLiteral("near(flag)");
	private static final Literal nb  = Literal.parseLiteral("near(base)");
	private static final Literal nfh = Literal.parseLiteral("near(flagholder)");
	private static final Literal wal = Literal.parseLiteral("near(wall)");
		
	public static ArrayList<String>  teamList = new ArrayList<String>();
	static MapModel perceptModel;
	
	public Perception(){
		
	}
	
	public static ArrayList<Literal> atLocation(String agName){
		perceptModel = MapEnv.model;
		ArrayList<Literal> atLoc = new ArrayList<Literal>();
		if(perceptModel != null){
			//Initialise the Agent Variables
			Flag  	 perceptFlag = perceptModel.flag;
			int 	 id = perceptModel.getAgentID(agName);
			Location lplayer = perceptModel.getAgPos(id); 

			//Return the Literals for adding the Perceptions
	        if (lplayer.equals(perceptFlag.getFlagLoc())) 
	        	atLoc.add(af);
	        if (lplayer.equals(getTeamBase(id)))
	        	atLoc.add(ab);
	        return atLoc;
		} return null;
	}
	
	//TODO: LookAround still not working
	
	public static ArrayList<Literal> lookAround(String agName){
		perceptModel = MapEnv.model;
		ArrayList<Literal> lookArr  = new ArrayList<Literal>();
		
		int 	 id 	 	  = perceptModel.getAgentID(agName);
		Location lplayer 	  = perceptModel.getAgPos(id); 

		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				Location l = new Location(lplayer.x + x, lplayer.y + y);
//				System.out.println("Player Loc: (" + lplayer.x + "," + lplayer.y + "):: See Loc (" + l.x + "," + l.y + ")");
				if(perceptModel.getAgAtPos(l) != -1 /**&& l.equals("FLAG")*/)
					lookArr.add(nfh);
				if(l.equals("OBSTICLE"))
					lookArr.add(wal); 
				if(l.equals("FLAG"))
					lookArr.add(nf);
				if(teamList.get(id) == "red" && l.equals("RED_BASE"))
					lookArr.add(nb);
				if(teamList.get(id) == "blue" && l.equals("BLU_BASE"))
					lookArr.add(nb);
			}
		}
		for(int y = -3; y <= 3; y+=6){
			for(int x = -3; x <= 3; x+=6){
				see(agName, new Location(lplayer.x + x, lplayer.y + y), lookArr);
			}
		}
		return lookArr;
	}
	
	private static void see(String agName, Location l, ArrayList<Literal> lookArr){
		perceptModel = MapEnv.model;
		int id = perceptModel.getAgentID(agName);

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

}
