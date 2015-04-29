import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.*;

public class MapEnv extends Environment {
	
	public static final Literal pf  = Literal.parseLiteral("pickup(flag)");
	public static final Literal tk  = Literal.parseLiteral("tackle");
	public static final Literal sf  = Literal.parseLiteral("score(flag)");
	public static final Literal p   = Literal.parseLiteral("pause");

	public static MapModel model;
	public static MapView view;
	public static InterfacePath pathFinder;
	public static InterfacePercept percept;
	public static Path[] paths = new Path[4];

    private Logger logger = Logger.getLogger("capturetheflag."+MapEnv.class.getName());
    
    @Override
    public void init(String[] args) {
    	model = new MapModel();
    	if (args.length == 1 && args[0].equals("gui")) { 
            view  = new MapView(model);
            model.setView(view);
        }
    	pathFinder = new Pathfinder(10);
    	percept = new Perception();
        super.init(args);
    }
    
    public void updatePercepts(String agName){
    	clearPercepts(agName);
    	
    	ArrayList<Literal> atLocation = percept.atLocation(agName);
    	if(!atLocation.isEmpty()){
    		for(int i=1; i <= atLocation.size(); i++){
    			addPercept(agName, atLocation.get(i-1));
    		}
    	}
    	
    	ArrayList<Literal> lookAround = percept.lookAround(agName);
    	if(!lookAround.isEmpty()){
    		for(int i=1; i <= lookAround.size(); i++){
    			addPercept(agName, lookAround.get(i-1));
    		}
    	}
    }
    
    @SuppressWarnings("unused")
    public boolean executeAction(String agName, Structure action) {
    	int id = model.getAgentID(agName);
    	Location lplayer = model.getAgPos(id);
   		boolean result = false;
    	updatePercepts(agName);
    	if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
            
            if (l.equals("base")) {
            	dest = percept.getTeamBase(model.getAgentID(agName));
            }
            if (l.equals("flag")){
            	dest = model.flag.getFlagLoc();
            }
            
//          Node next = pathFinder.findPath(id, lplayer.x, lplayer.y, dest.x, dest.y);
            result = model.moveTo(id, dest.x, dest.y);
            
        } else if(action.equals(pf)){
        	result = model.pickupFlag(agName);
        } else if(action.equals(sf)){
        	result = model.scoreFlag(agName);
        } else if(action.equals(tk)){
        	String t = action.getTerm(0).toString();
        	result = model.takeFlag(agName, t);
        } else if(action.equals(p)){
        	//This is a temporary 'nudge' action, that moves the agent away from the boundaries of the map.
        }
        else logger.info("executing: "+action+", but not implemented!");
    	
    	//Slows the Simulation down, to make it possible to see what's going on.
    	try {
			Thread.sleep(42);
			view.update();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    //This is Utter Shit
    public Location getFreePos(Location dest) {
    	Random random = new Random();
    	int r = random.nextInt(8) + 1; Location l;
    	switch(r){
    	case 1:
    		l = new Location(dest.x - 1, dest.y + 1);
    		if(model.isFree(l)) return l;
    		else this.getFreePos(dest);
    	case 2:
    		l = new Location(dest.x, dest.y + 1);
    		if(model.isFree(l)) return l;
    		else this.getFreePos(dest);
    	case 3:
    		l = new Location(dest.x + 1, dest.y + 1);
    		if(model.isFree(l)) return l;
    		else this.getFreePos(dest);
    	case 4:
    		l = new Location(dest.x + 1, dest.y);
    		if(model.isFree(l)) return l;
    		else this.getFreePos(dest);
    	case 5:
    		l = new Location(dest.x + 1, dest.y - 1);
    		if(model.isFree(l)) return l;
    		else this.getFreePos(dest);
    	case 6:
    		l = new Location(dest.x, dest.y - 1);
    		if(model.isFree(l)) return l;
    		else this.getFreePos(dest);
    	case 7:
    		l =  new Location(dest.x - 1, dest.y - 1);
    		if(model.isFree(l)) return l;
    		else this.getFreePos(dest);
    	case 8:
    		l =  new Location(dest.x - 1, dest.y);
    		if(model.isFree(l)) return l;
    		else this.getFreePos(dest);
    	default:
    		System.out.println("getFreePos went wrong. Gave " + r);
    		return dest;
    	}
    }
    
    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
}
