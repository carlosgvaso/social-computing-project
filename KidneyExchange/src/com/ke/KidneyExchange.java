/**
 * 
 */
package com.ke;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import com.ke.KidneyExchange.CycleStruct;

/**
 * @author Jose Carlos Martinez Garcia-Vaso jcm3767 <carlosgvaso@gmail.com>
 * @author Trevor Anderson
 * @author Bryson Banks
 * @author Alexander Hoganson
 *
 */
public class KidneyExchange {
	// Class variables
	private static double inf = Double.MAX_VALUE;	// Infinity
	
	// Instance variables
	public int L; 								// Cycle cap
	public int V;								// Number of vertices
	public int E;								// Number of edges
	public Graph G;								// Reduced graph
	//public ArrayList<ArrayList<Integer>> cycles;// Negative cycles in the graph
	
	public KidneyExchange(int L, int V, int E) {
		this.L = L;
		this.V = V;
		this.E = E;
		this.G = new Graph(this.V, this.E);
	}
	
	/**
	 * A class to represent a weighted directed edge in graph.
	 * 
	 * Instance variables:
	 * 	src		Edge source vertex.
	 * 	dest	Edge destination vertex.
	 * 	weight	Edge weight.
	 */
	public class Edge {
		public int src, dest;
		public double weight, dual;
		
		// Create an empty directed edge (src=dest=weight=0).
		public Edge() {
			src = dest = 0; 
			dual = weight = 0;
		}
		
		// Create a directed edge.
		public Edge(int src, int dest, double dual, int weight) {
			this.src = src;
			this.dest = dest;
			this.dual = dual;
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
	public class Graph { 
		public int V, E;
		public Edge edge[];
		
		// Creates a graph with V vertices and E edges, where all the edges are empty.
		public Graph(int v, int e) {
			V = v;
			E = e;
			edge = new Edge[e];
			for (int i=0; i<e; ++i)
				edge[i] = new Edge();
		}
	}
	
	public class CycleStruct
	{
		public int weight;
		public ArrayList<ArrayList<Integer>> IncludedCycles;
		
		public CycleStruct () {
			weight = 0;
			IncludedCycles = new ArrayList<ArrayList<Integer>>();
		}
	}
	
	/**
	 * Find the negative weight cycles in graph G with a maximum length of L using a modified
	 * version of the Bellman-Ford algorithm.
	 * 
	 * @param graph		Reduced graph.
	 * @param cycle_len	Maximum cycle length.
	 */
	public ArrayList<ArrayList<Integer>> getNegativeCycles(Graph graph, int cycle_len) {
		// Empty the accumulator set for negative weight cycles.
		/*
		if (!this.cycles.isEmpty()) {
			this.cycles.clear();
		}
		*/

		 ArrayList<ArrayList<Integer>> cycles = new ArrayList<ArrayList<Integer>>();
		 HashSet<HashSet<Integer>> pathSet = new HashSet<HashSet<Integer>>();
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
			double dist[][] = new double[graph.V][cycle_len];
			
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
				//System.out.println("i = " + i);
				
				// Set initial values
				for (int dest=0; dest<graph.V; dest++) {
					if (dest != src) {
						dist[dest][i] = dist[dest][i-1];
						preds[dest][i] = preds[dest][i-1];
					}
				}
				
				// Iterate over all edges.
				for (int e=0; e<graph.E; e++) {
					//System.out.println("Edge: " + e + ", src: " + graph.edge[e].src + ", dest: " + graph.edge[e].dest + ", dual weight: " + graph.edge[e].dual);
					
					// If there is no loop in the path.
					if (!this.traversePreds(graph.edge[e].src, preds, (i-1)).contains(graph.edge[e].dest)) {
						//System.out.println("There is no loop in the path.");
						
						// If the step decreases the distance of the node.
						if (dist[graph.edge[e].src][i-1] != KidneyExchange.inf && dist[graph.edge[e].src][i-1] + graph.edge[e].dual < dist[graph.edge[e].dest][i]) {
							// Update to shorter distance
							dist[graph.edge[e].dest][i] = dist[graph.edge[e].src][i-1] + graph.edge[e].dual;
							//System.out.println("Update distance to: " + dist[graph.edge[e].dest][i]);
							
							// Store correct predecessor.
							preds[graph.edge[e].dest][i] = graph.edge[e].src;
							//System.out.println("Update predecessor to: " + preds[graph.edge[e].dest][i]);
						}
					}
				}
			}
			
			// Find negative weight cycles with s as the source. Iterate over all edges
			for (int e=0; e<graph.E; e++) {
				//System.out.println("Edge: " + e + ", src: " + graph.edge[e].src + ", dest: " + graph.edge[e].dest + ", dual weight: " + graph.edge[e].dual);
				
				// If the edge points from any vertex v (v!=src) to the src vertex (we have a cycle).
				if (graph.edge[e].src != src && graph.edge[e].dest == src) {
					if (dist[graph.edge[e].src][cycle_len-1] != KidneyExchange.inf && dist[graph.edge[e].src][cycle_len-1] + graph.edge[e].dual < 0) {
					//if (dist[graph.edge[e].src][cycle_len-1] + graph.edge[e].dual < 0) {
						ArrayList<Integer> newCycle = this.traversePreds(graph.edge[e].src, preds, cycle_len-1);
						HashSet<Integer> cycleSet = new HashSet<Integer>();
						for (int p : newCycle) {
							cycleSet.add(p);
						}
						if (pathSet.add(cycleSet)) {
							cycles.add(newCycle);
						}
					}
				}
			}
		}
		return cycles;
	}
	
	/**
	 * Traverse all predecessors to the vertex and provide a list of predecessors.
	 * 
	 * @param vertex		Vertex.
	 * @param predecessors	Predecessors array matrix.
	 * @param position		Position in the cycle.
	 * @return	List of predecessors to the vertex.
	 */
	public ArrayList<Integer> traversePreds(int vertex, int predecessors[][], int position) {
		//System.out.println("traversePreds: destination=" + vertex + ", position=" + position);
		
		// Create cycle and current lists and add source to current.
		ArrayList<Integer> cycle = new ArrayList<Integer>(this.L);	// Represents a cycle.
		int current = vertex;
		
		// Iterate until current is empty which means we reached the source node.
		while (current != -1) {
			// Add predecessor to path
			cycle.add(current);
			//System.out.println("Current added to cycle: " + current + ", position: " + position);
			
			// Get predecessor of current predecessor
			current = predecessors[current][position];
			//System.out.println("Predecessor of current: " + current + ", position: " + position);
			position--;
		}
		
		//System.out.print("predecessors traversed: ");
		for (int i=0; i<cycle.size(); i++) {
			//System.out.print(cycle.get(i));
			if (i != cycle.size()-1) {
				//System.out.print(", ");
			} else {
				//System.out.println();
			}
		}
		
		return cycle;
	}

	public ArrayList<ArrayList<Integer>> GetDiscountedPositivePriceCycles(Graph G, int L, double probability) {
		ArrayList<ArrayList<Integer>> cycle = new ArrayList<ArrayList<Integer>>();
		for (int l = 2; l <= L; l++) {
			Graph altG = new Graph(G.V, G.E);
			for (int i = 0; i < G.E; i++) {
				int src = G.edge[i].src;
				int dest = G.edge[i].dest;
				double dual = (G.edge[i].dual - (G.edge[i].weight));// * Math.pow(probability, l)));
				altG.edge[i] = new Edge(src,dest,dual,0);
			}
			cycle.addAll(getNegativeCycles(altG, l));
		}
		return cycle;
	}
	
	public ArrayList<CycleStruct> DetermineFeasibleWeightedCycles(ArrayList<ArrayList<Integer>> cycles) {
		/*
		 * Step 1, find cycle sets with all vertices, then pick the max weight
		 */
		ArrayList<CycleStruct> WeightedCycles = new ArrayList<CycleStruct>();
		HashSet<HashSet<Integer>> pathIndexSet = new HashSet<HashSet<Integer>>();
		
		//Iterate through all cycles as starting point
		for (int k = 0; k < cycles.size(); k++) {
			ArrayList<Integer> cycleA = cycles.get(k);
			//ArrayList<Integer> cycleList = new ArrayList<Integer>();
			for (int i = 0; i < cycles.size(); i++) {
				ArrayList<Integer> cycleList = new ArrayList<Integer>();
				HashSet<Integer> vertexSet = new HashSet<Integer>();
				vertexSet.addAll(cycleA);
				for (int j = 0; j < cycles.size(); j++) {
					ArrayList<Integer> cycleB = cycles.get((i + j) % cycles.size());
					boolean containsAny = false;
					for (int b : cycleB) {
						if (vertexSet.contains(b)) {
							containsAny = true;
						}
					}
					if (!containsAny) {
						vertexSet.addAll(cycleB);
						cycleList.add((i + j) % cycles.size());
					}
				}
				if (vertexSet.size() == this.V) {
					cycleList.add(k);
					HashSet<Integer> t = new HashSet<Integer>();
					t.addAll(cycleList);
					if (pathIndexSet.add(t)) {
						CycleStruct cs = new CycleStruct();
						for(int c : cycleList) {
							ArrayList<Integer> cycle = cycles.get(c);
							for (int cI : cycle) {
								cs.weight += this.G.edge[cI].weight;
							}
							cs.IncludedCycles.add(cycle);
						}
						WeightedCycles.add(cs);
						
					}
				}
			}
		}
		return WeightedCycles;
	}
	
	public CycleStruct DetermineHighestWeightedCycle(ArrayList<CycleStruct> Cycles) {
		int maxWeight = Integer.MIN_VALUE;
		int bestCycleSet = -1;
		for (CycleStruct cycle : Cycles) {
			if (cycle.weight > maxWeight) {
				maxWeight = cycle.weight;
				bestCycleSet = Cycles.indexOf(cycle);
			}
		}
		return Cycles.get(bestCycleSet);
	}
	
	/**
 	* Parse input file.
 	* @return KidneyExchange instance
 	*/
	public static KidneyExchange parseInput(String inputFile) {
	Scanner scanner;
	int numVertices = 0;
	int numEdges = 0;
	ArrayList<Edge> edges = new ArrayList<>();
	// create scanner object to parse file
	try {
	    scanner = new Scanner(new File(inputFile));
	} catch (FileNotFoundException e) {
	    System.out.println("Error. File not found: " + inputFile);
	    return null;
	}
	// parse the matrix size from first line
	if (scanner.hasNextInt()) {
	    numVertices = scanner.nextInt();
	} else {
	    System.out.println("Error. Failed to parse matrix size. Input file is incorrectly formatted.");
	    scanner.close();
	    return null;
	}
	// parse the square matrix values and calculate non-zero weight edges
	int[][] weights = new int[numVertices][numVertices];
	for (int rowIndex = 0; rowIndex < numVertices; rowIndex++) {
	    for (int colIndex = 0; colIndex < numVertices; colIndex++) {
		if (scanner.hasNextInt()) {
		    int edgeWeight = scanner.nextInt();
		    weights[rowIndex][colIndex] = edgeWeight;
		    if (edgeWeight != 0) {
			numEdges++;
		    }
		} else {
		    System.out.println("Error. Failed to parse weight matrix. Input file is incorrectly formatted.");
		    scanner.close();
		    return null;
		}
	    }
	}
	// done parsing file, success
	scanner.close();
	// create KidneyExchange instance
	KidneyExchange ke = new KidneyExchange(3, numVertices, numEdges);
	// update edge values
	int edgeIndex = 0;
	for (int rowIndex = 0; rowIndex < numVertices; rowIndex++) {
	    for (int colIndex = 0; colIndex < numVertices; colIndex++) {
		int edgeWeight = weights[rowIndex][colIndex];
		if (edgeWeight != 0) {
		    ke.G.edge[edgeIndex].src = rowIndex;
		    ke.G.edge[edgeIndex].dest = colIndex;
		    ke.G.edge[edgeIndex].weight = edgeWeight;
		    edgeIndex++;
		}
	    }
	}
	// return created KidneyExchange instance
	return ke;
}


/**
 * @param args
 */
public static void main(String[] args) {
	
	// verify one argument: inputFile
    	if (args.length != 1) {
    	    System.out.println("Error. Java program KidneyExchange expects 1 argument specifying an input file.");
    	    return;
    	}
    	String inputFile = args[0];
            
    	// parse input file to create KidneyExchange instance
    	KidneyExchange k = parseInput(inputFile);

		k.makeGraph();

		ArrayList<ArrayList<Integer>> cycles = k.getNegativeCycles(k.G, k.L);
		k.OutputCycles(cycles);
		
		ArrayList<CycleStruct> feasiblecycles = k.DetermineFeasibleWeightedCycles(cycles);
		k.OutputFeasibleCycles(feasiblecycles);
		
		CycleStruct bestCycleSet = k.DetermineHighestWeightedCycle(feasiblecycles);
		k.OutputFinalCycles(bestCycleSet);
		
		
		ArrayList<ArrayList<Integer>> failurecycles = k.GetDiscountedPositivePriceCycles(k.G, k.L, 1000);
		k.OutputCycles(failurecycles);
		

		ArrayList<ArrayList<Integer>> failurecycles2 = k.GetDiscountedPositivePriceCycles(k.G, k.L, .001);
		k.OutputCycles(failurecycles2);
	}

	private void makeGraph() {
		// Set up graph
		G.edge[0].src = 0;
		G.edge[0].dest = 1;
		G.edge[0].weight = 1;
		G.edge[0].dual = -1;
		
		G.edge[1].src = 1;
		G.edge[1].dest = 0;
		G.edge[1].weight = 1;
		G.edge[1].dual = -1;
		
		G.edge[2].src = 1;
		G.edge[2].dest = 2;
		G.edge[2].weight = 1;
		G.edge[2].dual = -2;

		G.edge[3].src = 2;
		G.edge[3].dest = 1;
		G.edge[3].weight = 1;
		G.edge[3].dual = -1;
		
		G.edge[4].src = 2;
		G.edge[4].dest = 3;
		G.edge[4].weight = 1;
		G.edge[4].dual = -1;
		
		G.edge[5].src = 3;
		G.edge[5].dest = 2;
		G.edge[5].weight = 1;
		G.edge[5].dual = -2;
		
		G.edge[6].src = 2;
		G.edge[6].dest = 4;
		G.edge[6].weight = 1;
		G.edge[6].dual = -1;
		
		G.edge[7].src = 4;
		G.edge[7].dest = 0;
		G.edge[7].weight = 1;
		G.edge[7].dual = -1;
		
		G.edge[8].src = 4;
		G.edge[8].dest = 3;
		G.edge[8].weight = 1;
		G.edge[8].dual = -1;
	}
	
	private void OutputFinalCycles(CycleStruct bestCycleSet) {
		System.out.println("======================MAX WEIGHT Cycle======================");
		System.out.print("{ ");
		for(int i = 0; i < bestCycleSet.IncludedCycles.size(); i++) {
			ArrayList<Integer> path = bestCycleSet.IncludedCycles.get(i);
			System.out.print("(");
			int firstFinal = -1;
			for (int j = 0; j < path.size(); j++) {
				if (j == 0)
					firstFinal = path.get(j);
				System.out.print(path.get(j) + " -> ");
			}
			System.out.print(firstFinal + ") ");
		}
		System.out.println("}");
	}

	private void OutputFeasibleCycles(ArrayList<CycleStruct> feasiblecycles) {
		System.out.println("======================FEASIBLE Cycles======================");
		for (int m = 0; m < feasiblecycles.size(); m++) {
			System.out.print("{ ");
			CycleStruct feasiblecycle = feasiblecycles.get(m);
			for(int i = 0; i < feasiblecycle.IncludedCycles.size(); i++) {
				ArrayList<Integer> path = feasiblecycle.IncludedCycles.get(i);
				System.out.print("(");
				int firstFinal = -1;
				for (int j = 0; j < path.size(); j++) {
					if (j == 0)
						firstFinal = path.get(j);
					System.out.print(path.get(j) + " -> ");
				}
				System.out.print(firstFinal + "), ");
			}

			System.out.println("}");
		}
	}

	private void OutputCycles(ArrayList<ArrayList<Integer>> cycles) {
		System.out.println("==========================ALL Cycles=====================");
		System.out.print("{");
		
		if(cycles.size() > 0) {
			for (int i=0; i<cycles.size(); i++) {
				//HashSet cSet = new HashSet<Integer>();
				
				System.out.print("(");
				for (int j=0; j<cycles.get(i).size(); j++) {
					if (j != cycles.get(i).size()-1) {
						System.out.print(cycles.get(i).get(j) + ", ");
					} else {
						System.out.print(cycles.get(i).get(j));
					}
				}
				if (i != cycles.size()-1) {
					System.out.print("), ");
				} else {
					System.out.println(")}");
				}
			}
		}
		else
			System.out.println("}");
	}

}
