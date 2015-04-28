
import java.util.ArrayList;

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.*;

public class Perception extends Environment implements InterfacePercept {
	
	private static final Literal af  = Literal.parseLiteral("at(player,flag)");
	private static final Literal ab  = Literal.parseLiteral("at(player,base)");
	private static final Literal nfh = Literal.parseLiteral("near(flagholder)");
	private static final Literal npf  = Literal.parseLiteral("near(player,friendly)");
	private static final Literal npe  = Literal.parseLiteral("near(player,enemy)");
		
	public static ArrayList<String>  teamList = new ArrayList<String>();
	static MapModel perceptModel;
	static MapEnv mapenv;
	
	public ArrayList<Literal> atLocation(String agName){
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
	
	public ArrayList<Literal> lookAround(String agName){
		perceptModel = MapEnv.model;
		ArrayList<Literal> lookArr  = new ArrayList<Literal>();
		int 	 id 	 	  = perceptModel.getAgentID(agName);
		Location lplayer 	  = perceptModel.getAgPos(id); 

		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				if(x != 0 && y != 0){
					Location l = new Location(lplayer.x + x, lplayer.y + y);
	//				System.out.println("Player Loc: (" + lplayer.x + "," + lplayer.y + "):: See Loc (" + l.x + "," + l.y + ")");
					if(perceptModel.getAgAtPos(l) != -1 ){
						int agt = perceptModel.getAgAtPos(l);
						if(l.equals("FLAG")){
							lookArr.add(nfh);
						}
						if(getTeam(agt) == getTeam(id)){
							lookArr.add(npf);
						} else if(getTeam(agt) != getTeam(id)) {
							lookArr.add(npe);
						}
					}
				}
			}
		}
		for(int y = -2; y <= 2; y+=4){
			for(int x = -2; x <= 2; x+=4){
				Location l = new Location(lplayer.x + x, lplayer.y + y);
//				System.out.println("Player Loc: (" + lplayer.x + "," + lplayer.y + "):: See Loc (" + l.x + "," + l.y + ")");
				if(perceptModel.getAgAtPos(l) != -1 ){
					int agt = perceptModel.getAgAtPos(l);
					if(l.equals("FLAG")){
						lookArr.add(nfh);
					}
					if(getTeam(agt) == getTeam(id)){
						lookArr.add(npf);
					} else if(getTeam(agt) != getTeam(id)) {
						lookArr.add(npe);
					}
				}
			}
		}
		return lookArr;
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
	
	public Location getTeamBase(int id){
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
