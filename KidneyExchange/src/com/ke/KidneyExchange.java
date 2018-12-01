/**
 * 
 */
package com.ke;

import java.util.ArrayList;
import java.util.HashSet;

import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.SolveParam;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.CompareType;
import edu.harvard.econcs.jopt.solver.mip.Constraint;
import edu.harvard.econcs.jopt.solver.mip.MIP;
import edu.harvard.econcs.jopt.solver.mip.VarType;
import edu.harvard.econcs.jopt.solver.mip.Variable;

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
	public int L; 								// Cycle cap
	public int V;								// Number of vertices
	public int E;								// Number of edges
	public Graph G;						        // Reduced graph
    public Graph origG;                         // Original graph
    double weights[][];
    double reducedWeights[][];
	//public ArrayList<ArrayList<Integer>> cycles;// Negative cycles in the graph
    
    // linear programming representation (cycle formulation)
    double[][] a;
    double[] b;
    double[] c;
    double optimal;
    double[] dualValues;
    
    public KidneyExchange(int L, int V, double[][] weights) {
        this.L = L;
        this.V = V;
        this.E = V;
        this.weights = weights;
        this.reducedWeights = new double[V][V];
        this.origG = new Graph(V, weights);
        formulateLP();  // sets a, b, and c
        this.optimal = getLPOptimalVal(); // sets dual values
        
        //System.out.println(optimal);
        //this.G = createReducedGraph(this.origG);
        
        setReducedEdgeWeights();
        this.G = new Graph(origG, reducedWeights);
    }
    
    public IMIPResult solve(IMIP mip, SolverClient client) {
        mip.setSolveParam(SolveParam.CALC_DUALS, true);
        IMIPResult result = client.solve(mip);
        return result;
    }
    
    public void setReducedEdgeWeights() {
        reducedWeights = new double[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (weights[i][j] > 0) {
                    reducedWeights[i][j] = - dualValues[j] - weights[i][j];
                }
            }
        }
    }
    
    public double getLPOptimalVal() {
        LinearProgramming lp;
        try {
            lp = new LinearProgramming(a, b, c);
        }
        catch (ArithmeticException e) {
            System.out.println(e);
            return -1.0;
        }
        double[] x = lp.primal();
        double[] y = lp.dual();
        dualValues = y;
//        for (int i = 0; i < lp.primal().length; i++) {
//            System.out.println("x[" + i + "] = " + x[i]);
//        }
        for (int i = 0; i < lp.dual().length; i++) {
            System.out.println("y[" + i + "] = " + y[i]);
        }
        return lp.value();
    }
    
    public double getDualPriceOfNodeConstraint(int node) {
        b[node] = 2;
        double newOptimal = getLPOptimalVal();
        b[node] = 1;
        return optimal - newOptimal;
    }
    
    public void formulateLP() {
        ArrayList<HashSet<Edge>> allCycles = getAllCycles(L);
        int cycleCount = allCycles.size();
        System.out.println("Total cycle count: " + cycleCount);
        System.out.flush();

        a = new double[V+cycleCount][cycleCount];
        b = new double[V+cycleCount];
        c = new double[cycleCount];
        for (int i = 0; i < V; i++) {
            double[] subA = new double[cycleCount];
            HashSet<HashSet<Edge>> nodeCycles = getCyclesFromNode(i, V, weights, L);
            for (HashSet<Edge> cycle : nodeCycles) {
                for (int j = 0; j < cycleCount; j++) {
                    if (allCycles.get(j).equals(cycle)) {
                        subA[j] = 1;
                        break;
                    }
                }
            }
            a[i] = subA;
            b[i] = 1;
        }
        for (int i = V; i < V + cycleCount; i++) {
            double[] subA = new double[cycleCount];
            subA[i-V] = 1;
            a[i] = subA;
            b[i] = 1;
        }
        for (int i = 0; i < cycleCount; i++) {
            c[i] = allCycles.get(i).size();
        }
    }
    
    public HashSet<Edge> getAllEdges(int nodes, double[][] weights) {
        HashSet<Edge> edges = new HashSet<>();
        edges.addAll(origG.edges);
        return edges;
    }
    
    public HashSet<Edge> getForwardEdges(int node, HashSet<Edge> edges) {
        HashSet<Edge> forwardEdges = new HashSet<>();
        for (Edge edge : edges) {
            if (edge.src.number == node) {
                forwardEdges.add(edge);
            }
        }
        return forwardEdges;
    }
    
    public ArrayList<ArrayList<Edge>> getPathsFromNode(int node, HashSet<Edge> edges, HashSet<Edge> usedEdges, int k) {
        ArrayList<ArrayList<Edge>> paths = new ArrayList<ArrayList<Edge>>();
        // reached cycle length limit?
        if (k == 0) {
            return paths;
        }
        // any edges left?
        if (edges.isEmpty()) {
            return paths;
        }
        //System.out.println("Edges left: " + edges.size());
        // any forward edges from node?
        HashSet<Edge> forwardEdges = getForwardEdges(node, edges);
        if (forwardEdges.isEmpty()) {
            return paths;
        }
        // look for paths along edge forward edge
        for (Edge edge : forwardEdges) {
            //System.out.println("Edge: " + edge.src + " --> " + edge.dest);
            // does forward edge create a cycle?
            boolean isCycle = false;
            for (Edge usedEdge: usedEdges) {
                if (edge.dest == usedEdge.src) {
                    //System.out.println(usedEdge.src + " --> " + usedEdge.dest);
                    isCycle = true;
                    break;
                }
            }
            if (!isCycle) {
                // if not a cycle, continue on
                HashSet<Edge> restrictedEdges = new HashSet<>();
                restrictedEdges.addAll(edges);
                restrictedEdges.remove(edge);
                HashSet<Edge> newUsedEdges = new HashSet<>();
                newUsedEdges.addAll(usedEdges);
                newUsedEdges.add(edge);
                ArrayList<ArrayList<Edge>> forwardPaths = getPathsFromNode(edge.dest.number, restrictedEdges, newUsedEdges, k-1);
                if (!forwardPaths.isEmpty()) {
                    for (ArrayList<Edge> path : forwardPaths) {
                        path.add(0, edge);
                    }
                    }
                paths.addAll(forwardPaths);
            } else {
                // forward edges creates a cycle
                //System.out.println("Edge creates a cycle");
                ArrayList<Edge> path = new ArrayList<>();
                path.add(edge);
                paths.add(path);
            }
        }
        return paths;
    }
    
    public HashSet<HashSet<Edge>> getCyclesFromPaths(ArrayList<ArrayList<Edge>> paths) {
        HashSet<HashSet<Edge>> cycles = new HashSet<HashSet<Edge>>();
        for (ArrayList<Edge> path : paths) {
            if (path.get(0).src == path.get(path.size() - 1).dest) {
                cycles.add(new HashSet<Edge>(path));
            }
        }
        return cycles;
    }
    
    public HashSet<HashSet<Edge>> getCyclesFromNode(int node, int nodes, double[][] weights, int k) {
        HashSet<Edge> edges = new HashSet<>(this.origG.edges);
        HashSet<Edge> usedEdges = new HashSet<>();
        ArrayList<ArrayList<Edge>> paths = getPathsFromNode(node, edges, usedEdges, k);
        //System.out.println("Paths from Node: " + paths.size());
        HashSet<HashSet<Edge>> cycles = getCyclesFromPaths(paths);
        //System.out.println("Cycles from Node: " + cycles.size());
        return cycles;
    }
    
    public ArrayList<HashSet<Edge>> getAllCycles(int k) {
        ArrayList<HashSet<Edge>> allCycles = new ArrayList<HashSet<Edge>>();
        for (int i = 0; i < V; i++) {
            HashSet<HashSet<Edge>> cycles;
            cycles = getCyclesFromNode(i, V, weights, k);
            //System.out.println("Node Cycles: " + cycles.size());
            allCycles = addCycles(cycles, allCycles);
            //System.out.println("All Cycles: " + allCycles.size());
        }
        //System.out.println("All Cycles: " + allCycles.size());
        return allCycles;
    }
    
    public ArrayList<HashSet<Edge>> addCycles(HashSet<HashSet<Edge>> newCycles, ArrayList<HashSet<Edge>> allCycles) {
        for (HashSet<Edge> cycle : newCycles) {
            boolean duplicate = false;
            for (HashSet<Edge> cycle2 : allCycles) {
                if (cycle.equals(cycle2)) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {
//              cycle.forEach((edge) -> {
//                  System.out.println(edge.src + " --> " + edge.dest);
//              });
//              System.out.println("---");
                allCycles.add(cycle);
            }
        }
        return allCycles;
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
				System.out.println("i = " + i);
				
				// Set initial values
				for (int dest=0; dest<graph.V; dest++) {
					if (dest != src) {
						dist[dest][i] = dist[dest][i-1];
						preds[dest][i] = preds[dest][i-1];
					}
				}
				
				// Iterate over all edges.
				for (int e=0; e<graph.E; e++) {
					System.out.println("Edge: " + e + ", src: " + graph.edges.get(e).src.number + ", dest: " + graph.edges.get(e).dest.number + ", dual weight: " + graph.edges.get(e).reducedWeight);
					
					// If there is no loop in the path.
					if (!this.traversePreds(graph.edges.get(e).src.number, preds, (i-1)).contains(graph.edges.get(e).dest.number)) {
						System.out.println("There is no loop in the path.");
						
						// If the step decreases the distance of the node.
						if (dist[graph.edges.get(e).src.number][i-1] != KidneyExchange.inf && dist[graph.edges.get(e).src.number][i-1] + graph.edges.get(e).reducedWeight < dist[graph.edges.get(e).dest.number][i]) {
							// Update to shorter distance
							dist[graph.edges.get(e).dest.number][i] = dist[graph.edges.get(e).src.number][i-1] + graph.edges.get(e).reducedWeight;
							System.out.println("Update distance to: " + dist[graph.edges.get(e).dest.number][i]);
							
							// Store correct predecessor.
							preds[graph.edges.get(e).dest.number][i] = graph.edges.get(e).src.number;
							System.out.println("Update predecessor to: " + preds[graph.edges.get(e).dest.number][i]);
						}
					}
				}
			}
			
			// Find negative weight cycles with s as the source. Iterate over all edges
			for (int e=0; e<graph.E; e++) {
				System.out.println("Edge: " + e + ", src: " + graph.edges.get(e).src.number + ", dest: " + graph.edges.get(e).dest.number + ", dual weight: " + graph.edges.get(e).reducedWeight);
				
				// If the edge points from any vertex v (v!=src) to the src vertex (we have a cycle).
				if (graph.edges.get(e).src.number != src && graph.edges.get(e).dest.number == src) {
					if (dist[graph.edges.get(e).src.number][cycle_len-1] != KidneyExchange.inf && dist[graph.edges.get(e).src.number][cycle_len-1] + graph.edges.get(e).reducedWeight < 0) {
					//if (dist[graph.edge[e].src][cycle_len-1] + graph.edge[e].dual < 0) {
						ArrayList<Integer> newCycle = this.traversePreds(graph.edges.get(e).src.number, preds, cycle_len-1);
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
		
		System.out.print("predecessors traversed: ");
		for (int i=0; i<cycle.size(); i++) {
			System.out.print(cycle.get(i));
			if (i != cycle.size()-1) {
				System.out.print(", ");
			} else {
				System.out.println();
			}
		}
		
		return cycle;
	}

	public ArrayList<ArrayList<Integer>> GetDiscountedPositivePriceCycles(Graph G, int L, double probability) {
		ArrayList<ArrayList<Integer>> cycle = new ArrayList<ArrayList<Integer>>();
		for (int l = 2; l <= L; l++) {
			Graph altG = new Graph(G.V, G.E);
			for (int i = 0; i < G.E; i++) {
				int src = G.edges.get(i).src.number;
				int dest = G.edges.get(i).dest.number;
				double dual = (G.edges.get(i).reducedWeight - (G.edges.get(i).weight * Math.pow(probability, l)));
				//altG.edge[i] = new Edge(src,dest,dual,0);
				altG.edges.get(i).src.number = src;
                altG.edges.get(i).dest.number = dest;
                altG.edges.get(i).reducedWeight = dual;
				
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
								cs.weight += this.G.edges.get(cI).weight;
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
	
	
//	public Graph createReducedGraph(Graph graph) {
//        
//        
//	      IMIP mip;
//	      ArrayList<Constraint> constraints;
//	      
//	      mip = new MIP();
//	      
//	      Variable[] vars = new Variable[c.length];
//	      for (int i = 0; i < c.length; i++) {
//	          vars[i] = new Variable("x" + i, VarType.DOUBLE, 0, 1);
//	          mip.add(vars[i]);
//	      }
//	      
//	      mip.setObjectiveMax(true);
//	      for (int i = 0; i < c.length; i++) {
//	          mip.addObjectiveTerm(c[i], vars[i]);
//	      }
//	      
//	      Constraint[] cons = new Constraint[a.length];
//	      for (int i = 0; i < a.length; i++) {
//	          Constraint con = new Constraint(CompareType.LEQ, 1);
//	          for (int j = 0; j < a[i].length; j++) {
//	              con.addTerm(a[i][j], vars[j]);
//	          }
//	          mip.add(con);
//	          cons[i] = con;
//	      }
//
//	      System.out.println(mip);
//	      
//	      IMIPResult result = solve(mip, new SolverClient());
//	      System.out.println(result);
//	      for (int i = 0; i < vars.length; i++) {
//	          System.out.println("x" + i + ": " + result.getValue("x" + i));
//	      }
//	      for (int i = 0; i < cons.length/2; i++) {
//	          System.out.println("Dual of n" + i + ": " + result.getDual(cons[i]));
//	      }
//
//	        
//	      return null;
//	    }

}
