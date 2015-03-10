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
	
	@Override
    public void draw(Graphics g, int x, int y, int object) {
	    super.drawAgent(g, x, y, Color.white, -1);
        switch (object) {
	        case MapModel.BLU_BASE: 
	            if (viewModel.isFree(viewModel.bBase)) {
	            	super.drawAgent(g, x, y, Color.blue, -1);
	            	g.setColor(Color.black);
	            	super.drawString(g, x, y, defaultFont, "Blue Base");
	            }
	            break;
            case MapModel.RED_BASE: 
                if (viewModel.isFree(viewModel.rBase)) {
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
		
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        if (viewModel.flag.flagCarried && id == viewModel.flag.agentCarrying){
        	c = Color.magenta;
	    	super.drawAgent(g, x, y, c, id);
	    	g.setColor(Color.black);
	        super.drawString(g, x, y, defaultFont, "Flagholder");
        } else {
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
