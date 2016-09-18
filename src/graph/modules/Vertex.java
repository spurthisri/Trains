package graph.modules;

public class Vertex {

	public String name;
	public boolean visited;
 
	public Vertex(String name) {
		this.name = name;
		this.visited = false;
	}
 
	@Override
	public boolean equals(Object v) {
		if (v == null || v.getClass() != getClass()) {
	        return false;
	    }
		Vertex vx = (Vertex)v;
		return this.name.equals(vx.name);
	}
 
	@Override
	public String toString(){
		return this.name;
	}
	
	@Override
	public int hashCode() {
		if(this.name == null) return 0;
		return this.name.hashCode();
	}
	
}
