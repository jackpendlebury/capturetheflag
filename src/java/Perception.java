import java.util.ArrayList;

import jason.asSyntax.Literal;
import jason.environment.Environment;
import jason.environment.grid.*;

public class Perception extends Environment {
	
	private static final Literal af  = Literal.parseLiteral("at(player,flag)");
	private static final Literal ab  = Literal.parseLiteral("at(player,base)");
	private static final Literal nf  = Literal.parseLiteral("near(flag)");
	private static final Literal nb  = Literal.parseLiteral("near(base)");
	private static final Literal nfh = Literal.parseLiteral("near(flagholder)");
//	private static final Literal npf = Literal.parseLiteral("near(player, friendly)");
//	private static final Literal npn = Literal.parseLiteral("near(player, nasty)");
	
	public static ArrayList<String> teamList = new ArrayList<String>();
	MapModel model;
	
	public void updatePercepts(String agName){
		if(agName != null){
			clearPercepts(agName);
//			System.out.println(agName + " Percepts Cleared");
			atLocation(agName);
//	        lookAround(agName);
		}
    }
	
	public String getTeam(int id){
		return teamList.get(id);
	}
	
	public Location getTeamBase(int id){
		if(teamList.get(id) == "red"){
			return model.rBase;
		} else if(teamList.get(id) == "blue") {
			return model.bBase;
		} else {
			return model.getAgPos(id);
		}
	}
	
	public static void initTeams(){
		for(int i = 0; i <= MapModel.TotAgt; i++){
			if(i % 2 == 0){
				teamList.add("red");
			} else {
				teamList.add("blue");
			}
		}
		System.out.println("Teams Initialised");
	}
	
	private void atLocation(String agName){
		//TODO: This functions throws a NullPointerException when called. Pain in the Arse
		Location lplayer; int id = model.getAgentID(agName);
		lplayer = model.getAgPos(id);
		System.out.println("Player Location: " + lplayer);
        if (lplayer.equals(model.flag.getFlagLoc())) {
             addPercept(agName, af);
        }
        if (lplayer.equals(model.rBase)){
        	addPercept(agName, ab);
        }
		System.out.println("AtLocation Cleared");
	}
	
	private void see(Location l, int id){
		if(model.getAgAtPos(l) != -1 && l.equals("FLAG"))
			addPercept(nfh);
		if(l.equals("OBSTICLE"))
			addPercept("Wall @"); //Fix the if(space full) at MoveTowards before this.
		if(l.equals("FLAG"))
			addPercept(nf);
		if(teamList.get(id) == "red" && l.equals("RED_BASE"))
			addPercept(nb);
		if(teamList.get(id) == "blue" && l.equals("BLU_BASE"))
			addPercept(nb);
	}
	
	//TODO: Sight not quite ready yet
	private void lookAround(String agName){
		int id = model.getAgentID(agName);
		Location agt = model.getAgPos(id); 
		Location l = agt;
		for(int y = -2; y <= 2; y++){
			for(int x = -2; x <= 2; x++){
				l.x = agt.x + x;
				l.y = agt.y + y;
				see(l, id);
			}
		}
		for(int y = -3; y <= 3; y+=6){
			for(int x = 3; x <= 3; x+=6){
				l.x = agt.x + x;
				l.y = agt.y + y;
				see(l, id);
			}
		}
	}

}
