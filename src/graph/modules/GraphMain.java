package graph.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
/* 
 * Implementation class for "The Trains" Problem.
 * Input is read from the file given by the user.
 */
public class GraphMain {
	
	public static String ReadFileContents(String fileName) throws IOException{
		
		BufferedReader buf = new BufferedReader(new FileReader(fileName));
		String content; 
		//Read file contents line by line and add it to StringBuilder
		try{
			StringBuilder sb = new StringBuilder();
			String line = buf.readLine();
			while(line != null){
				sb.append(line);
				sb.append(System.lineSeparator());
				line = buf.readLine();
			}
			content = sb.toString();
			if(content != null)
				return content;
			else{
					System.out.println("Content not present in file");
					return null;
			}
		}catch(IOException e){
			System.out.println("Exception while reading the file : ");
			e.printStackTrace();
		}finally{
			buf.close();
		}
		return null;
	}
	
		//Create route table (Adjacency List) from the given graph content.
		public static Hashtable<Vertex, Edge> CreateRouteTable(String fileContent){
			String[] eachEdge = fileContent.trim().split(",");
			Set<String> vertices = new HashSet<String>();
			Hashtable<Vertex, Edge> adjacencyList = new Hashtable<Vertex, Edge>();
			
			for(int i = 0; i<eachEdge.length; i++){
				char[] charArray = eachEdge[i].trim().toCharArray();
				for(int j = 0; j<charArray.length-1; j++){
					vertices.add(String.valueOf(charArray[j]));
				}				
			}
			
			Vertex[] ver = new Vertex[30];
			int inc = 0;
			for(String s : vertices){
				ver[inc++] = new Vertex(s);
			}
			Edge[] edgeArray = new Edge[30];
			String start, end;
			int weight = 0, i = 0;
			start = end = null;
			for(String e : eachEdge){
				char[] edgeSplit = e.trim().toCharArray();
				
				start = String.valueOf(edgeSplit[0]);
				end = String.valueOf(edgeSplit[1]);
				weight = Integer.parseInt(String.valueOf(edgeSplit[2]));
				edgeArray[i++] = new Edge(new Vertex(start),new Vertex(end),weight);	
			}
			Edge presentEdge = new Edge(null,null,0);
			for(Edge e : edgeArray){
				if(e!=null){
							if(!(adjacencyList.containsKey(e.origin))){ // When edge is placed for the first time in the hash table
								adjacencyList.put(e.origin, e);
							}
							else{ // When the Edge is placed after the key is created in hash table
									presentEdge = adjacencyList.get(e.origin); // get the value of the key (#first edge)
									while(presentEdge.next != null){
										presentEdge = presentEdge.next; // Check the edge list and get the last edge							
									}
									presentEdge.next = e; // Assign new edge to next of the last edge
							}			
					}
				else
					break;
			}
			return adjacencyList;
		}		
		
		public static int DistanceBetween(Graph g, ArrayList<Vertex> places) throws Exception{
			int distance = g.distanceBetween(places);
			return distance;
		}
		
		public static int OriginToDestWithinStops(Graph g, Vertex start, Vertex end, int stops) throws Exception{
			int numOfStops = g.numStops(start, end, stops);
			return numOfStops;
		}
		
		public static int ShortRouteFromOriginToDest(Graph g, Vertex start, Vertex end) throws Exception{
			int shortRoute = g.shortestRoute(start, end);
			return shortRoute;
		}
		
		public static int NumRoutesWithin(Graph g, Vertex start, Vertex end, int maxWeight) throws Exception{
			int NumRoutes = g.numRoutesWithin(start, end, maxWeight);
			return NumRoutes;
		}
		
		public static boolean CheckFile(String fileName){
			File f = new File(fileName);
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
			if(!f.exists()){
				System.out.println("File does not exist.");
				return false;
			}
			if(!f.isFile()){
				System.out.println(fileName + " is not a valid File.");
				return false;
			}
			if(!ext.equals("txt")){
				System.out.println(fileName + " extention is not proper.");
				return false;
			}
			if(!f.canRead()){
				System.out.println("Cannot read the file contents of "+fileName);
				return false;
			}
			return true;
		}
		
		public static boolean CheckData(String fileContent){
			String[] splitData = fileContent.trim().split(",");
			if(splitData.length == 0){
				System.out.println("Graph data not in correct format.");
				return false;
			}
			for(String s : splitData){
				if(!Pattern.matches("[a-zA-Z0-9]{3}", s.trim())){
					System.out.println("Graph data not in right format. Please make sure that each edge is represented like 'AB5'.");
					System.out.println("And each edge is separated by comma.");
					return false;
				}				
			}
			return true;
		}
		
		public static void main(String[] args) throws Exception{
			System.out.println("Give the .txt file name where the graph is defined : ");
			Scanner sc = new Scanner(System.in);
			String fileName = null;
			String fileContent = null;
			Hashtable<Vertex, Edge> routeTable = new Hashtable<Vertex,Edge>();			
			fileName = sc.nextLine();
			if(!CheckFile(fileName)){
				System.out.println("Problem reading the file contents.");
				sc.close();
				return;
			}
			else{
				fileContent = ReadFileContents(fileName); //1) Read File contents
				if(CheckData(fileContent) == false){
					System.out.println("File content not in expected format.");
					sc.close();
					return;
				}
				System.out.println(fileContent);
				routeTable = CreateRouteTable(fileContent); //2) Create Route Table
				Graph g = new Graph(routeTable); //3) Represent Graph using Route Table
				System.out.println(routeTable.toString());
				System.out.println("*************FIND DISTANCE START*************");
				//Enter the routes to check the weights in the format Ex: A-B-C
				//Press enter once all the routes checking is finished.
				System.out.println("Give the routes separated by '-' (Ex:A-B-C): (Press Enter after all scenarios are finished)");
				boolean cond = true;
				while(cond){
					String s = sc.nextLine();
					String[] sArray = s.split("-");
					if(s.isEmpty()){
						cond = false;
						break;
					}
					ArrayList<Vertex> aList = new ArrayList<Vertex>();
					for(String st : sArray){
						aList.add(new Vertex(st));
					}
					int distance = DistanceBetween(g, aList);
					System.out.println("Distance : " + distance);
					
				}
				System.out.println("*************FIND DISTANCE END*************");
				System.out.println("*************FIND ORIGIN TO DESTINATION WITH IN GIVEN NO OF STOPS START*************");
				System.out.println("Give the Origin, Destination and MaxStops separated by '-' (Ex:C-C-3): (Press Enter after all scenarios are finished)");
				cond = true;
				while(cond){
					String s = sc.nextLine();
					String[] sArray = s.split("-");
					if(s.isEmpty()){
						cond = false;
						break;
					}
					Vertex start,end;
					start = new Vertex(sArray[0]);
					end = new Vertex(sArray[1]);
					int maxStops = Integer.parseInt(sArray[sArray.length-1]);
					int withInStops = OriginToDestWithinStops(g, start, end, maxStops);
					System.out.println("No.Of routes : " +withInStops);
				}
				System.out.println("*************FIND ORIGIN TO DESTINATION WITH IN GIVEN NO OF STOPS END*************");
				System.out.println("*************LENGTH OF SHORTEST ROUTE START*************");
				System.out.println("Give the Origin and Destination seperated by '-' (Ex:A-C): (Press Enter after all scenarios are finished)");
				cond = true;
				while(cond){
					String s = sc.nextLine();
					String[] sArray = s.split("-");
					if(s.isEmpty()){
						cond = false;
						break;
					}
					Vertex start, end;
					start = new Vertex(sArray[0]);
					end = new Vertex(sArray[1]);
					int shortRoute = ShortRouteFromOriginToDest(g, start, end);
					System.out.println("Shortest Route between " +start +" and "+end+ " is "+shortRoute);
				}
				System.out.println("*************LENGTH OF SHORTEST ROUTE END*************");
				System.out.println("*************NUMBER OF DIFFERENT ROUTES START*************");
				System.out.println("Give the Origin, Destination and distance seperated by '-' (Ex:A-C-30): (Press Enter after all scenarios are finished)");
				cond = true;
				while(cond){
					String s = sc.nextLine();
					String[] sArray = s.split("-");
					if(s.isEmpty()){
						cond = false;
						break;
					}
					Vertex start,end;
					int maxDistance;
					start = new Vertex(sArray[0]);
					end = new Vertex(sArray[1]);
					maxDistance = Integer.parseInt(sArray[2]);
					int diffRoutes = NumRoutesWithin(g,start,end,maxDistance);
					System.out.println("Number of different routes between "+start+" and "+end+" with distance less than "+maxDistance+ " is "+diffRoutes);
				}
				System.out.println("*************NUMBER OF DIFFERENT ROUTES END*************");
			}
			sc.close();
		}
}
