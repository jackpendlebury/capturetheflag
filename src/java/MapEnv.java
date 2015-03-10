import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.*;

import java.util.Random;
import java.util.logging.*;

public class MapEnv extends Environment {
	
	public static final Literal pf  = Literal.parseLiteral("pickup(flag)");
	public static final Literal tk  = Literal.parseLiteral("tackle(player)");
	public static final Literal sf  = Literal.parseLiteral("score(flag)");
	public static final Literal iam = Literal.parseLiteral("greeting");

    private Logger logger = Logger.getLogger("capturetheflag."+MapEnv.class.getName());
    MapModel model; Perception percept;
    
    @Override
    public void init(String[] args) {
    	model = new MapModel(); percept = new Perception();
    	if (args.length == 1 && args[0].equals("gui")) { 
            MapView view  = new MapView(model);
            model.setView(view);
        }
        super.init(args);
    }

	//This could be edited to include a 'sight length' parameter
	//i.e. how far a player can see.
	
	//TODO: Make sure this to see's other players

    @Override
    public boolean executeAction(String agName, Structure action) {
    	@SuppressWarnings("unused")
    	boolean result = false;
    	percept.updatePercepts(agName);
    	if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
            
            if (l.equals("base")) {
                dest = model.rBase;
            }
            if (l.equals("flag")){
            	dest = model.flag.getFlagLoc();
            }
                        
        	if(dest == null || !model.isFree(dest)){
            		dest = getFreePos(dest);
        	}
            result = model.moveTowards(dest, agName);
            
        } else if(action.equals(pf)){
//        	System.out.println("Picking up Flag");
        	result = model.pickupFlag(agName);
        } else if(action.equals(sf)){
        	result = model.scoreFlag(agName);
        } else if(action.equals(tk)){
        	result = model.takeFlag(agName);
        }
        else logger.info("executing: "+action+", but not implemented!");
    	
    	//Slows the Simulation down, to make it possible to see what's going on.
    	try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
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
