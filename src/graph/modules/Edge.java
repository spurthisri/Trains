package graph.modules;

public class Edge {
	
		//Starting town
		public Vertex origin;
		//Destination town
		public Vertex destination;
		//Weight of the route to destination
		public int weight;
		//Next Edge
		public Edge next;
		//Initializing class variables in constructor
		public Edge(Vertex origin, Vertex destination, int weight) {
			this.origin 		= origin;
			this.destination	= destination;
			this.weight 		= weight;
			this.next 		= null;
		}
	 
		public Edge next(Edge edge) {
			this.next = edge;
			return this;
		}

}
