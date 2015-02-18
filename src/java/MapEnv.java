import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;

import java.util.Random;
import java.util.logging.*;

public class MapEnv extends Environment {
	
	public static final Literal pf = Literal.parseLiteral("pickup(flag)");
	public static final Literal sf = Literal.parseLiteral("score(flag)");
	public static final Literal iam = Literal.parseLiteral("greeting");

	public static final Literal af = Literal.parseLiteral("at(player,flag)");
	public static final Literal ab = Literal.parseLiteral("at(player,base)");
	
	public static final Literal nrf = Literal.parseLiteral("near(flag)");
	public static final Literal nrb = Literal.parseLiteral("near(base)");

	
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
    
    void updatePercepts(){
    	// clear the percepts of the agent
//    	for(int i = 0; i <= MapModel.TotAgt; i++){
    		clearPercepts("player");
    		Location lplayer= model.getAgPos(0);
            if (lplayer == model.flag) {
            	 System.out.println("At Flag");
                 addPercept("player", af);
            }
            if (lplayer == model.rBase){
            	addPercept("player", ab);
            }
//            if(look(i, MapModel.FLAG)){
//            	addPercept("player", nrf);
//            }
//    	}
                
    }
	
	public Location randomMove(Location p){
		Random rand = new Random();
		int randomNum = rand.nextInt(5);
		System.out.println("Random Number is: " + randomNum);
		switch(randomNum){
			case 1:
				p.y++;
				break;	
			case 2:
				p.x++;
				break;
			case 3:
				p.y--;
				break;
			case 4:
				p.x--;
				break;
			default:
				System.out.println("Random gave something else");
				break;
		}
		return p;
	}

	//This could be edited to include a 'sight length' parameter
	//i.e. how far a player can see.
	public boolean look(int id, Object o){
		Location p = model.getAgPos(id);
		for(int y = -2; y <= 2; y++){
			for(int x = -2; x <= 2; x++){
				p.x += x;
				p.y += y;
				if(p.equals(o)){
					return true;
				}
			}
		}
		for(int y = -3; y <= 3; y+=6){
			for(int x = -3; x <= 3; x+=6){
				if(p.equals(o)){
					return true;
				}
			}
		}
		return false;
	}

    @Override
    public boolean executeAction(String agName, Structure action) {
    	boolean result = false;
    	if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
            
            if (l.equals("base")) {
//            	dest = search(0, model.rBase);
                dest = model.rBase;
            }
            if (l.equals("flag")){
//            	dest = search(0, model.flag);
            	dest = model.flag;
            }
            
            try {
                result = model.moveTowards(dest, 0); //0 is Agent ID
//            	System.out.println(agName + " is moving to " + dest.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else if(action.equals(pf)){
        	System.out.println("Picking up Flag");
        	result = model.pickupFlag();
        } else if(action.equals(sf)){
        	result = model.scoreFlag();
        } else if(action.equals(iam)){
        	result = model.setCurrentAgent(agName);
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
