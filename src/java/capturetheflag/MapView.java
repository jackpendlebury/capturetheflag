package capturetheflag;
import jason.environment.grid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class MapView extends GridWorldView {
	
	MapModel viewModel;
	Perception viewPercept;
	
	public MapView(MapModel model){
		super(model, "Capture the Flag", 500);
		viewModel = model;
		defaultFont = new Font("Arial", Font.BOLD, 10);
		setVisible(true);
		repaint();
	}
	
	/** Function to Draw Map Environment 
     * 
     * Parameter:
     * Graphics g - Java Graphics Object
     * int x - X Co-Ordinate of Location to be Drawn.
     * int y - Y Co-Ordinate of Location to be Drawn.
     * int object - Object present within Location (-1 if non-present)
     * 
     * */
	
	@Override
    public void draw(Graphics g, int x, int y, int object) {
	    super.drawAgent(g, x, y, Color.white, -1);
        switch (object) {
	        case MapModel.BLU_BASE: 
	            if (viewModel.isFree(MapModel.bBase)) {
	            	super.drawAgent(g, x, y, Color.blue, -1);
	            	g.setColor(Color.black);
	            	super.drawString(g, x, y, defaultFont, "Blue Base");
	            }
	            break;
            case MapModel.RED_BASE: 
                if (viewModel.isFree(MapModel.rBase)) {
                	super.drawAgent(g, x, y, Color.red, -1);
                	g.setColor(Color.black);
                	super.drawString(g, x, y, defaultFont, "Red Base");
                }
                break;
            case MapModel.FLAG:
            	if(!viewModel.flag.flagCarried){
            		super.drawAgent(g, x, y, Color.green, -1);
            		g.setColor(Color.black);
                	super.drawString(g, x, y, defaultFont, "Flag");
            	}
            	break;
        }
    }
	
	/** Function to Draw Map Environment 
     * 
     * Parameter:
     * Graphics g - Java Graphics Object
     * int x - X Co-Ordinate of Location to be Drawn.
     * Color c - Object Colour
     * int id - Present Agent ID (-1 if non-present)
     * 
     * */
		
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		//If Flagcarrier present...
        if (viewModel.flag.flagCarried && id == viewModel.flag.agentCarrying){
        	c = Color.magenta;
	    	super.drawAgent(g, x, y, c, id);
	    	g.setColor(Color.black);
	        super.drawString(g, x, y, defaultFont, "Flagholder");
        } else {
        	//If Agent Present...
        	if(id != -1){
        		if(Perception.teamList.get(id) == "red"){
            		c = Color.red;
                	super.drawAgent(g, x, y, c, id);
        		} else if(Perception.teamList.get(id) == "blue"){
            		c = Color.blue;
                	super.drawAgent(g, x, y, c, id);
        		}
        	}
    	}
    }
}
