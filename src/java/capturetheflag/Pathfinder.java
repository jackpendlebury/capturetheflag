package capturetheflag;

import jason.environment.grid.Location;

import java.util.ArrayList;

public class Pathfinder {
		private ArrayList<Location> closed = new ArrayList<Location>();
		private ArrayList<Location> open   = new ArrayList<Location>();
		private MapModel model = MapEnv.model;
		private Node nodes[][];
		
		//Add the rest of the code HERE
		
		public float getCost(MapModel model, int agent, int x, int y, int tx, int ty){
			float dx = tx - x;
			float dy = ty - y;
			
			float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));
			
			return result;
		}
}

class Node implements Comparable<Node>{
	int x;
	int y;
	
	private float cost;
	private Node parent;
	private float heuristic;
	private int depth;
	
	public Node(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int setParent(Node parent){
		depth = parent.depth + 1;
		this.parent = parent;
		
		return depth;
	}

	public int compareTo(Node n) {
		float f = heuristic + cost;
		float nf = n.heuristic + n.cost;
		
		if(f < nf){
			return -1;
		} else if(f > nf){
			return 1;
		} else {
			return 0;
		}
	}
}

class Path{
	private ArrayList<Node> steps = new ArrayList<Node>();
	
	public Path(){
		
	}
	
	public int getLength(){
		return steps.size();
	}
	
	public Node getStep(int i){
		return steps.get(i);
	}
	
	public int getX(int i){
		return steps.get(i).x;
	}
	
	public int getY(int i){
		return steps.get(i).y;
	}
	
	//Adds a step to the end of the Path
	public void appendStep(int x, int y){
		steps.add(new Node(x,y));
	}
	
	//Adds a Step to the start of the Path
	public void prependStep(int x, int y){
		steps.add(0, new Node(x,y));
	}
	
	public boolean contains(int x, int y){
		return steps.contains(new Node(x,y));
	}
	
}
