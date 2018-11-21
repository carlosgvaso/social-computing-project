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
	 * @param cycle_len	Maximum cycle length.
	 * @param reduced_w	Reduced edge weight vector.
	 */
	private void getNegativeCycles(Graph graph, int cycle_len, int[] reduced_w) {
		ArrayList<Integer> cycles = new ArrayList<Integer>(this.L);
		
		for (int src=0; src<graph.V; src++) {
			/*
			 * Create array matrix to track previous vertices already in the cycle. The array is of
			 * size num of vertices by max cycle length, and it is initialized to -1 (no vertices in
			 * cycle).
			 * */
			int preds[][] = new int[graph.V][cycle_len];
			
			/*
			 * Create array matrix to track the distance from the src (source) vertex to all the
			 * other vertices. The matrix is of size num of vertices by max cycle length. Distance
			 * is defined as the sum of the edge reduced weights in the computed path. Then,
			 * initialize all the initial distances as infinite, except for the distance of the
			 * source vertex to itself which is set to 0.
			 */
			int dist[][] = new int[graph.V][cycle_len];
			
			// Initialize matrices as described above.
			for (int i=0; i<graph.V; i++) {
				for (int j=0; j<cycle_len; j++) {
					preds[i][j] = -1;
					dist[i][j] = KidneyExchange.inf;
				}
			}
			dist[src][0] = 0;
			
			// Iterate over the max cycle length.
			for (int i=1; i<cycle_len; i++) {
				for (int dest=0; dest<graph.V; dest++) {
					if (dest != src) {
						dist[dest][i] = dist[dest][i-1];
						preds[dest][i] = preds[dest][i-1];
					}
				}
				
				// Iterate over all edges.
				for (int e=0; e<graph.E; e++) {
					// If there is no loop in the path.
					if (!this.traversePreds(graph.edge[e].src, preds, (i-1)).contains(graph.edge[e].dest)) {
						// If the step decreases the distance of the node.
						if (dist[graph.edge[e].src][i-1] + w[e] < dist[graph.edge[e].dest][i]) {
							// Update to shorter distance
							dist[graph.edge[e].dest][i] = dist[graph.edge[e].src][i-1] + w[e];
							
							// Store correct predecessor.
							preds[graph.edge[e].dest][i] = graph.edge[e].src;
						}
					}
				}
			}
			
			// Find negative weight cycles with s as the source.
			for (int v=0; v<graph.V; v++) {
				if (v != src) {
					
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
	private ArrayList<Integer> traversePreds(int source, int predecesor[][], int position) {
		// Create cycle and current lists and add source to current.
		ArrayList<Integer> cycle = new ArrayList<Integer>(this.L);	// Represents a cycle.
		ArrayList<Integer> current = new ArrayList<Integer>(this.L);
		current.add(source);
		
		// Iterate until current is empty which means we reached the source node.
		while (!current.isEmpty()) {
			// Add predecessor to path
			cycle.addAll(current);
			
			// Get predecessor of current predecessor
			
		}
		
		return cycle;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
