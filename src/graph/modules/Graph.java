package graph.modules;

import java.util.ArrayList;
import java.util.Hashtable;
/*
 * Graph class to represent Graph Routes and Graph Operations.
 * Properties: routeTable or AdjacencyList
 */
public class Graph {
	public Hashtable<Vertex, Edge> routeTable;
 
	public Graph() {
		this.routeTable = new Hashtable<Vertex, Edge>();
	}
	
	public Graph(Hashtable<Vertex, Edge> createdRouteTable) {
		this.routeTable = createdRouteTable;
	}
 
	// To calculate the distance between cities
	public int distanceBetween(ArrayList<Vertex> cities) throws Exception {
		// If 0 or 1 cities are given path does not exist
		if(cities.size() < 2)
			return 0;
		int distance, depth, i;
		distance = depth = i = 0;
 
		// For each city in the list, check if entry exists in our route table.
		while(i < cities.size() - 1) {
			if(this.routeTable.containsKey(cities.get(i))) {
				Edge route = this.routeTable.get(cities.get(i));
				/* If key exists, we check if route from key to next
				 * city exists. We add the distance, and maintain a
				 * depth count
				 */
				while(route != null) {
					if(route.destination.equals(cities.get(i + 1))) {
						distance += route.weight;
						depth++;
						break;
					}
					route = route.next;
				}
			}
			else
				throw new Exception("NO SUCH ROUTE");
			i++;
		}
		/*If edge depth is not equal to vertex - 1,
		 * then it is safe to assume that one ore more
		 * routes do not exist
		 */
		if(depth != cities.size() - 1)
			throw new Exception("NO SUCH ROUTE");
 
		return distance;
	}
 
	/*
	 * Number of stops;
	 * Wrapper for recursive function
	 */
	public int numStops(Vertex start, Vertex end, int maxStops) throws Exception{
		//Wrapper to maintain depth of traversal
		return findRoutes(start, end, 0, maxStops);
	}
 
	/*
	 * Finds number of stops from start to end,
	 * with a maximum of maxStops and the depth
	 * limit.
	 */
	private int findRoutes(Vertex start, Vertex end, int depth, int maxStops) throws Exception{
		int routes = 0;
		//Check if start and end vertices exists in route table
		if(this.routeTable.containsKey(start) && this.routeTable.containsKey(end)) {
			/*
			 * If start Vertex exists then traverse all possible
			 * routes and for each, check if it is destination
			 * If destination, and number of stops within 
			 * allowed limits, count it as possible route.
			 */
			depth++;
			if(depth > maxStops)		//Check if depth level is within limits
				return 0;
			start.visited = true;		//Mark start Vertex as visited
			Edge edge = this.routeTable.get(start);
			while(edge != null) {
				/* If destination matches, we increment route
				 * count, then continue to next Vertex at same depth
				 */
				if(edge.destination.equals(end)) {
					routes++;
					edge = edge.next;
					continue;
				}
				/* If destination does not match, and
				 * destination Vertex has not yet been visited,
				 * we recursively traverse destination Vertex
				 */
				else if(!edge.destination.visited) {
					routes += findRoutes(edge.destination, end, depth, maxStops);
					depth--;
				}
				edge = edge.next;
			}
		}
		else
			throw new Exception("NO SUCH ROUTE");
 
		/*
		 * Before exiting this recursive stack level,
		 * we mark the start Vertex as visited.
		 */
		start.visited = false;
		return routes;
	}
 
	/*
	 * Shortest route;
	 * Wrapper for recursive function
	 */
	public int shortestRoute(Vertex start, Vertex end) throws Exception {
		//Wrapper to maintain weight
		return findShortestRoute(start, end, 0, 0);
 
	}
 
	/*
	 * Finds the shortest route between two Vertexs
	 */
	private int findShortestRoute(Vertex start, Vertex end, int weight, int shortestRoute) throws Exception{
		//Check if start and end Vertexs exists in route table
		if(this.routeTable.containsKey(start) && this.routeTable.containsKey(end)) {
			/*
			 * If start Vertex exists then traverse all possible
			 * routes and for each, check if it is destination
			 */
			start.visited = true;		//Mark start Vertex as visited
			Edge edge = this.routeTable.get(start);
			while(edge != null) {
				//If Vertex not already visited, or is the destination, increment weight
				if(edge.destination == end || !edge.destination.visited)
					weight += edge.weight;
 
				/* If destination matches, we compare
				 * weight of this route to shortest route
				 * so far, and make appropriate switch
				 */
				if(edge.destination.equals(end)) {
					if(shortestRoute == 0 || weight < shortestRoute)
						shortestRoute = weight;
					start.visited = false;
					return shortestRoute; 			// return shortest route
				}
				/* If destination does not match, and
				 * destination Vertex has not yet been visited,
				 * we recursively traverse destination Vertex
				 */
				else if(!edge.destination.visited) {
					shortestRoute = findShortestRoute(edge.destination, end, weight, shortestRoute);
					//Decrement weight as we backtrack
					weight -= edge.weight;
				}
				edge = edge.next;
			}
		}
		else
			throw new Exception("NO SUCH ROUTE");
 
		/*
		 * Before exiting this recursive stack level,
		 * we mark the start Vertex as visited.
		 */
		start.visited = false;
		return shortestRoute;
 
	}
 
	/*
	 * Shortest route;
	 * Wrapper for recursive function
	 */
	public int numRoutesWithin(Vertex start, Vertex end, int maxDistance) throws Exception {
		//Wrapper to maintain weight
		return findnumRoutesWithin(start, end, 0, maxDistance);
	}
 
	/*
	 * Finds the shortest route between two vertices
	 */
	private int findnumRoutesWithin(Vertex start, Vertex end, int weight, int maxDistance) throws Exception{
		int routes = 0;
		//Check if start and end vertices exists in route table
		if(this.routeTable.containsKey(start) && this.routeTable.containsKey(end)) {
			/*
			 * If start Vertex exists then traverse all possible
			 * routes and for each, check if it is destination
			 */
			Edge edge = this.routeTable.get(start);
			while(edge != null) {
				weight += edge.weight; 
				/* If distance is under max, keep traversing
				 * even if match is found until distance is > max
				 */
				if(weight <= maxDistance) {
					if(edge.destination.equals(end)) {
						routes++;
						routes += findnumRoutesWithin(edge.destination, end, weight, maxDistance);
						edge = edge.next;
						continue;
					}
					else {
						routes += findnumRoutesWithin(edge.destination, end, weight, maxDistance);
						weight -= edge.weight;	//Decrement weight as we backtrack
					}
				}
				else 
					weight -= edge.weight;
 
				edge = edge.next;
			}
		}
		else
			throw new Exception("NO SUCH ROUTE");
 
		return routes;
 
	}	
}