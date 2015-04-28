import java.util.ArrayList;

public class Pathfinder implements InterfacePath {
		private ArrayList<Node> closed = new ArrayList<Node>();
		private ArrayList<Node> open   = new ArrayList<Node>();
		private MapModel model = MapEnv.model;
		private Node nodes[][];
		int maxSearch;
		
		public Pathfinder(int maxSearch){
			this.maxSearch = maxSearch;
			//Initialise the 2D Node Array.
			nodes = new Node[MapModel.GSize][MapModel.GSize];
			for(int x = 0; x < MapModel.GSize; x++){
				for(int y = 0; y < MapModel.GSize; y++){
					nodes[x][y] = new Node(x,y);
				}
			}
		}
		
		public Node findPath(int agentID, int sx, int sy, int tx, int ty){
			//If the target location is blocked, return 'null'
			if(!model.isFree(tx, ty)){
//				System.out.println("Target Location not Free");
				return null;
			}
			
			nodes[sx][sy].cost = 0;
			nodes[sx][sy].depth = 0;
			closed.clear();
			open.clear();
			addToOpen(nodes[sx][sy]);
			nodes[tx][ty].parent = null;
			
			int maxDepth = 0;
			while((maxDepth < maxSearch) && (open.size() != 0)){
				Node current = getFirstinOpen();
				if(current == nodes[tx][ty]){
					break;
				}
				removeFromOpen(current);
				addToClosed(current);
				
				//Cycle through all the tiles neighbours
				for(int x = -1; x < 2; x++){
					for(int y = -1; y < 2; y++){
						//Not a neighbour, but the current tile.
						if ((x == 0) && (y == 0)) {
							continue;
						}
						//determine the location of the tile
						int xp = x + current.x;
						int yp = y + current.y;
						
						if(isValidLocation(sx, sy, xp, yp) && !nodes[xp][yp].visited){
							Node neighbour = nodes[xp][yp];
							neighbour.visited = true;
							float nextStepCost = current.cost + getCost(sx, sy, xp, yp);
//							System.out.println("Current Cost = ("+ nodes[xp][yp].cost +")");
							/** 
							 *  If the new cost we found is lower than previously thought then
							 *  the neighbour must be re-evaluated.
							 */
							if(nextStepCost > neighbour.cost){
								if(isInOpen(neighbour)){
									removeFromOpen(neighbour);
								}
								if(isInClosed(neighbour)){
									removeFromClosed(neighbour);
								}
							}
							
							if(!isInOpen(neighbour) && !isInClosed(neighbour)){
								neighbour.cost = nextStepCost;
								neighbour.heuristic = getCost(neighbour.x,neighbour.y,tx,ty);
								maxDepth = Math.max(maxDepth, neighbour.setParent(current));
								addToOpen(neighbour);
							}
						}
					}
				}
				
			}
//			if(!open.isEmpty()){
//				Node best = closed.get(0);
//				for (int i = 1; i < closed.size(); i++) {
//					if(best.compareTo(closed.get(i)) < 0){
//						best = closed.get(i);
//					}
//				}
//				return best;
//			}
//			return null;
			
			
			if(nodes[tx][ty].parent == null){
//				System.out.println("No Path");
				return null;
			}
			
			Path path = new Path();
			Node target = nodes[tx][ty];
			while(target != nodes[sx][sy]){
				path.prependStep(target.x, target.y);
				target = target.parent;
			}
			path.prependStep(sx, sy);
			System.out.println("Found Path");
			return path.getNextStep();
		}
		
		
		protected boolean isValidLocation(int sx, int sy, int tx, int ty){
			boolean invalid = (tx < 0) || (ty < 0) || (tx >= MapModel.GSize) || (ty >= MapModel.GSize);
			
			if((!invalid) && (sx != tx) && (sy != ty)){
				invalid = model.isFree(tx, ty);
			}
			
			return !invalid;
		}
		
		protected Node getFirstinOpen(){
			return open.get(0);
		}
		
		protected void addToOpen(Node n){
			open.add(n);
		}
		
		protected boolean isInOpen(Node n){
			return open.contains(n);
		}
		
		protected void removeFromOpen(Node n){
			open.remove(n);
		}
		
		protected Node getFirstinClosed(){
			return closed.get(0);
		}
		
		protected void addToClosed(Node n){
			closed.add(n);
		}
		
		protected boolean isInClosed(Node n){
			return closed.contains(n);
		}
		
		protected void removeFromClosed(Node n){
			closed.remove(n);
		}
		
		public float getCost(int x, int y, int tx, int ty){
			float dx = tx - x;
			float dy = ty - y;
			
			float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));
			
			return result;
		}
}

class Node implements Comparable<Node>{
	int x;
	int y;
	
	float cost;
	Node  parent;
	float heuristic;
	int   depth;
	boolean visited = false;
	
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
	
	public Node getNextStep(){
		if(getLength() != 0){
			
			return steps.get(0);
		} else {
			return null;
		}
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