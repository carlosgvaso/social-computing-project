/**
 * 
 */
package com.ke;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Jose Carlos Martinez Garcia-Vaso jcm3767 <carlosgvaso@gmail.com>
 * @author Trevor Anderson
 * @author Bryson Banks
 * @author Alexander Hoganson
 *
 */
public class KidneyExchange {
	// Class variables
	private static int inf = Integer.MAX_VALUE;	// Infinity
	
	// Instance variables
	private int L; 								// Cycle cap
	private int[] w;							// Reduced edge weight vector
	private Graph G;							// Reduced graph
	
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
	 * @param graph		Reduced graph.
	 * @param cycleLen	Maximum cycle length.
	 * @param reduced_w	Reduced edge weight vector.
	 */
	private void getNegativeCycles(Graph graph, int cycleLen, int[] reduced_w) {
		ArrayList<Integer> cycles = new ArrayList<Integer>();
		
		for (int src=0; src<graph.V; src++) {
			/*
			 * Create array to track previous vertices already in the cycle. The array is of size
			 * cycleLen, and it is initialized to -1 (no vertices in cycle).
			 * */
			int preds[] = new int[cycleLen];
			Arrays.fill(preds, -1);
			
			/*
			 * Create array to track the distance from the src (source) vertex to all the other
			 * vertices. Distance is defined as the sum of the edge reduced weights in the computed
			 * path. Then, initialize all the distances as infinite, except for the distance of the
			 * source vertex to itself which is set to 0.
			 */
			int dist[] = new int[graph.V];
			Arrays.fill(dist, KidneyExchange.inf);
			dist[src] = 0;
			
			for (int i=0; i<(cycleLen-1); i++) {
				for (int k=0; k<graph.E; k++) {
					if (graph.edge[k].dest == this.traversePreds(graph.edge[k].src, preds, (i-1))) {
						if () {
							
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param source
	 * @param predecesor
	 * @param position
	 * @return
	 */
	private int traversePreds(int source, int predecesor[], int position) {
		/*
		 *  TODO:
		 *  - implement.
		 */
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
