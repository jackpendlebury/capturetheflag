import java.util.ArrayList;

import jason.asSyntax.Literal;
import jason.environment.grid.Location;

public interface InterfacePercept {
	public ArrayList<Literal> 	atLocation(String agName);
	public ArrayList<Literal> 	lookAround(String agName);
	public Location getTeamBase(int id);
	public String 	getTeam(int id);
}
