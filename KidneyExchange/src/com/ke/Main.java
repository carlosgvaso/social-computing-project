package com.ke;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    
    public static final int L = 4;  // cycle cap

    public static void main(String[] args) {
        // verify one argument: inputFile
        if (args.length != 1) {
            System.out.println("Error. Java program expects 1 argument specifying an input file.");
            return;
        }
        String inputFile = args[0];
            
        // parse input file to create KidneyExchange instance
        KidneyExchange ke = parseInput(inputFile);
        
        //printDebug(ke);
        
        //printGraphInput(ke.G);
    

		ArrayList<ArrayList<Integer>> cycles = ke.getNegativeCycles(ke.G, ke.L);
		OutputCycles(cycles);
		
		ArrayList<CycleStruct> feasiblecycles = ke.DetermineFeasibleWeightedCycles(cycles);
		OutputFeasibleCycles(feasiblecycles);
		
		CycleStruct bestCycleSet = ke.DetermineHighestWeightedCycle(feasiblecycles);
		OutputFinalCycles(bestCycleSet);
		
		
		//ArrayList<ArrayList<Integer>> failurecycles = k.GetDiscountedPositivePriceCycles(k.G, k.L, 1000);
		//OutputCycles(failurecycles);
		

		//ArrayList<ArrayList<Integer>> failurecycles2 = k.GetDiscountedPositivePriceCycles(k.G, k.L, .001);
		//k.OutputCycles(failurecycles2);
	}

	
	private static void OutputFinalCycles(CycleStruct bestCycleSet) {
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

	private static void OutputFeasibleCycles(ArrayList<CycleStruct> feasiblecycles) {
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

	private static void OutputCycles(ArrayList<ArrayList<Integer>> cycles) {
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
    
    public static void printDebug(KidneyExchange ke) {
        // print graph edges for debug
        for (Edge edge : ke.G.edges) {
              System.out.println("Reduced Edge: " + (edge.src.number+1) + " --> " + (edge.dest.number+1) + " , weight: " + edge.weight);
        }
    }
    
    public static void printGraphInput(Graph G) {
        double[][] weights = new double[G.V][G.V];
        for (int i = 0; i < G.edges.size(); i++) {
            int src = G.edges.get(i).src.number;
            int dest = G.edges.get(i).dest.number;
            weights[src][dest] = G.edges.get(i).weight;
        }
        

        System.out.println(G.V);
        for (int i = 0; i < G.V; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < G.V; j++) {
                sb.append(weights[i][j] + " ");
            }
            System.out.println(sb);
        }
    }
    
    /**
     * Parse input file.
     * @return KidneyExchange instance
     */
    public static KidneyExchange parseInput(String inputFile) {
        
        Scanner scanner;
        int numVertices = 0;
        
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
        double[][] weights = new double[numVertices][numVertices];
        for (int rowIndex = 0; rowIndex < numVertices; rowIndex++) {
            for (int colIndex = 0; colIndex < numVertices; colIndex++) {
                if (scanner.hasNextDouble()) {
                    double edgeWeight = scanner.nextDouble();
                    weights[rowIndex][colIndex] = edgeWeight;
                } else {
                    System.out.println("Error. Failed to parse weight matrix. Input file is incorrectly formatted.");
                    scanner.close();
                    return null;
                }
             }
        }
            
        // done parsing file, success
        scanner.close();
        
        KidneyExchange ke = new KidneyExchange(Main.L, numVertices, weights);
        return ke;
    }
 

}
