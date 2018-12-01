package com.ke;

import java.io.File;
import java.io.FileNotFoundException;
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
        
        printDebug(ke);
        
        printGraphInput(ke.G);
    
        //TODO - further algorithm implementation
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
