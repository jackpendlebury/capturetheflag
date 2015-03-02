import jason.environment.grid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class MapView extends GridWorldView {
	
	MapModel viewModel;
	
	public MapView(MapModel model){
		super(model, "Capture the Flag", 500);
		viewModel = model;
		defaultFont = new Font("Arial", Font.BOLD, 10);
		setVisible(true);
		repaint();
	}
	
	//TODO: [MAJOR] Implement Swing.
	@Override
    public void draw(Graphics g, int x, int y, int object) {
        Location lplayer = model.getAgPos(0);
        super.drawAgent(g, x, y, Color.white, -1);
        switch (object) {
	        case MapModel.BLU_BASE: 
	            if (!lplayer.equals(viewModel.bBase)) {
	            	super.drawAgent(g, x, y, Color.blue, -1);
	            	g.setColor(Color.black);
	            	super.drawString(g, x, y, defaultFont, "Blue Base");
	            }
	            break;
            case MapModel.RED_BASE: 
                if (!lplayer.equals(viewModel.rBase)) {
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
        	c = Color.red;
        	super.drawAgent(g, x, y, c, id);
        	g.setColor(Color.black);
        }
    }
	
}
