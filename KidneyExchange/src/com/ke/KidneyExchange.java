/**
 * 
 */
package com.ke;

/**
 * @author Jose Carlos Martinez Garcia-Vaso jcm3767 <carlosgvaso@gmail.com>
 * @author Trevor Anderson
 * @author Bryson Banks
 * @author Alexander Hoganson
 *
 */
public class KidneyExchange {
	// Class variables
	private static int inf = 1000000000;// Infinity
	
	// Instance variables
	private int L; 						// Cycle cap
	private int[] w;					// Reduced edge weight vector
	private Graph G;					// Reduced graph
	
	/**
	 * A class to represent a weighted directed edge in graph.
	 * 
	 * Instance variables:
	 * 	src		Edge source vertex.
	 * 	dest	Edge destination vertex.
	 * 	weight	Edge weight.
	 */
	private class Edge {
		public int src, dest, weight;
		
		// Create an empty directed edge (src=dest=weight=0).
		Edge() {
			src = dest = weight = 0; 
		}
		
		// Create a directed edge.
		Edge(int src, int dest, int weight) {
			this.src = src;
			this.dest = dest;
			this.weight = weight; 
		}
	}
	
	/**
	 * A class to represent a connected, directed and weighted graph.
	 * 
	 * Instance variables:
	 * 	V		Number or vertices.
	 * 	E		Number of edges.
	 * 	edge	Array with all the edges (Edge objects) in the graph.
	 * 
	 * To modify edge 0 in graph to point from vertex 2 to vertex 3 with a weight of 1:
	 * 	graph.edge[0].src = 2; 
	 * 	graph.edge[0].dest = 3; 
	 * 	graph.edge[0].weight = 1; 
	 */
	private class Graph { 
		public int V, E;
		public Edge edge[];
		
		// Creates a graph with V vertices and E edges, where all the edges are empty.
		Graph(int v, int e) {
			V = v;
			E = e;
			edge = new Edge[e];
			for (int i=0; i<e; ++i)
				edge[i] = new Edge();
		}
	}
	
	/**
	 * Find the negative weight cycles in graph G with a maximum length of L using a modified
	 * version of the Bellman-Ford algorithm.
	 * 
	 * @param G	Reduced graph.
	 * @param L	Maximum cycle length.
	 * @param w	Reduced edge weight vector.
	 */
	private void getNegativeCycles(Graph G, int L, int[] w) {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
