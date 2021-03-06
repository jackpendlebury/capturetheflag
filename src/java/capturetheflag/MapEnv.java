package capturetheflag;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.*;

import java.util.ArrayList;
import java.util.logging.*;

public class MapEnv extends Environment {

	public static MapModel model;
	public static MapView view;
	public static InterfacePercept percept;
	
	/** Agent Command String Literals */
	
	public static final Literal sc = Literal.parseLiteral("score");
	public static final Literal pu = Literal.parseLiteral("pickup");
	
	/** Jason Console Error Logger */

    private Logger logger = Logger.getLogger("capturetheflag."+MapEnv.class.getName());
    
    /** Initial System Initialisation Function 
     * 
     * Parameter:
     * String[] args - Argument from Initial Call from Jason Project File
     * 
     * */
    
    @Override
    public void init(String[] args) {
    	//Creates Model Class Instance
    	model = new MapModel();
    	//Jason Project Call passes "gui" to initialise the GUI
    	if (args.length == 1 && args[0].equals("gui")) { 
    		//Create GUI Class instance
            view  = new MapView(model);
            model.setView(view);
        }
    	//Creates Perception Class Instance
    	percept = new Perception();
        super.init(args);
    }
    
    /** Perception Update Function 
     * 
     * Parameter:
     * String agName - Active Agent's Full Name
     * 
     * */
    
    public void updatePercepts(String agName){
    	clearPercepts(agName);
    	
    	//Applies the Perceptions added to ArrayList within the Perception Class to the Active Agent
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
    
    /** Execute Agent Action Function 
     * 
     * Parameter:
     * String agName - Active Agent's Full Name
     * Structure action - Data Structure containing Action calls
     * 
     * Returns:
     * boolean - Boolean Success Indicator Flag
     * 
     * */
    
    @SuppressWarnings("unused")
    public boolean executeAction(String agName, Structure action) {
    	int id = model.getAgentID(agName);
    	Location lplayer = model.getAgPos(id);
   		boolean result = false;
   		//Call function to update Agent perceptions
    	updatePercepts(agName);
    	if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
            
            if (l.equals("base")) {
            	dest = percept.getTeamBase(model.getAgentID(agName));
            }
            if(l.equals("flag")){
            	if (!model.flag.flagCarried){
                	dest = model.flag.getFlagLoc();
                } else {
                	dest = model.getAgPos(model.flag.agentCarrying);
                }
            }
            
            if(dest != null){
                result = model.moveTo(id, dest.x, dest.y);
            } else {
            	logger.info("movement error: destination was null");
            }
            
        } else if(action.equals(pu)){
        	result = model.pickupFlag(agName);
        } else if(action.equals(sc)){
        	result = model.scoreFlag(agName);
        } else if(action.getFunctor().equals("tackle")){
        	String t = action.getTerm(0).toString();
        	result = model.takeFlag(agName, t);
        } 
        else logger.info("executing: "+action+", but not implemented!");
    	
    	//Slows the Simulation down, making is possible to take accurate results.
    	try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
}
