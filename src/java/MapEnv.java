import jason.asSemantics.Agent;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.*;

import java.util.Random;
import java.util.logging.*;

public class MapEnv extends Environment {
	
	public static final Literal pf = Literal.parseLiteral("pickup(flag)");
	public static final Literal tk = Literal.parseLiteral("tackle(player)");
	public static final Literal sf = Literal.parseLiteral("score(flag)");
	public static final Literal iam = Literal.parseLiteral("greeting");

	public static final Literal af = Literal.parseLiteral("at(player,flag)");
	public static final Literal ab = Literal.parseLiteral("at(player,base)");
	
	public static final Literal nf = Literal.parseLiteral("near(flag)");
	public static final Literal nb = Literal.parseLiteral("near(base)");
	public static final Literal npf = Literal.parseLiteral("near(player, friendly)");
	public static final Literal npn = Literal.parseLiteral("near(player, nasty)");

    private Logger logger = Logger.getLogger("capturetheflag."+MapEnv.class.getName());
    
    MapModel model;
    
    @Override
    public void init(String[] args) {
    	model = new MapModel();
    	if (args.length == 1 && args[0].equals("gui")) { 
            MapView view  = new MapView(model);
            model.setView(view);
        }
        super.init(args);
        updatePercepts();
    }
    
    public void updatePercepts(){
    	for(int i = 1; i <= model.TotAgt; i++){
    		clearPercepts("player" + i);
    		Location lplayer= model.getAgPos(i-1);
            if (lplayer.equals(model.flag.getFlagLoc())) {
                 addPercept("player" + i, af);
            }
            if (lplayer.equals(model.rBase)){
            	addPercept("player" + i, ab);
            }    
    	}
    }

	//This could be edited to include a 'sight length' parameter
	//i.e. how far a player can see.
	
	//TODO: Make sure this to see's other players
	public boolean look(int id, int sightLength){
		Location p = model.getAgPos(id); Location l = p; int nearAgent;
		for(int y = -sightLength; y <= sightLength; y++){
			for(int x = -sightLength; x <= sightLength; x++){
				l.x = p.x += x;
				l.y = p.y += y;
				if(model.getAgAtPos(p) != -1){
					nearAgent = model.getAgAtPos(p);
					addPercept("near(player#"+ nearAgent +")");
					return true;
				} if(p.equals("FLAG")){
					addPercept("near(flag)");
					return true;
				} if(p.equals("RED_BASE") || p.equals("BLU_BASE")){
					addPercept("near(base)");
					return true;
				}
			}
		}
		for(int y = -(sightLength+1); y <= (sightLength+1); y+=((sightLength+1) * 2)){
			for(int x = -(sightLength+1); x <= (sightLength+1); x+=((sightLength+1) * 2)){
				l.x = p.x += x;
				l.y = p.y += y;
				if(model.getAgAtPos(p) != -1){
					nearAgent = model.getAgAtPos(l);
					addPercept("near(player#"+ nearAgent +")");
					return true;
				} if(p.equals("FLAG")){
					addPercept("near(flag)");
					return true;
				} if(p.equals("RED_BASE") || p.equals("BLU_BASE")){
					addPercept("near(base)");
					return true;
				}
			}
		}
		return false;
	}

    @Override
    public boolean executeAction(String agName, Structure action) {
    	@SuppressWarnings("unused")
    	boolean result = false;
    	if (action.getFunctor().equals("move_towards")) {
            updatePercepts();
            String l = action.getTerm(0).toString();
            Location dest = null;
            
            if (l.equals("base")) {
                dest = model.rBase;
            }
            if (l.equals("flag")){
            	dest = model.flag.getFlagLoc();
            }
                        
            try {
            	if(dest == null || !model.isFree(dest)){
            			//TODO: Change the source to un-protect this. There's no reason its protected!
//                		dest = model.getFreePos();
            	}
                result = model.moveTowards(dest, agName); //0 is 1st Agent ID
//            	System.out.println(agName + " is moving to " + dest.toString());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else if(action.equals(pf)){
        	System.out.println("Picking up Flag");
        	result = model.pickupFlag(agName);
        } else if(action.equals(sf)){
        	result = model.scoreFlag(agName);
        } else if(action.equals(tk)){
        	result = model.takeFlag(agName);
        }
        else logger.info("executing: "+action+", but not implemented!");
        return true;
    }
    
    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
}
