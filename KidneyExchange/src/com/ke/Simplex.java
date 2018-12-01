package com.ke;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.SolveParam;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.CompareType;
import edu.harvard.econcs.jopt.solver.mip.Constraint;
import edu.harvard.econcs.jopt.solver.mip.MIP;
import edu.harvard.econcs.jopt.solver.mip.VarType;
import edu.harvard.econcs.jopt.solver.mip.Variable;

public class Simplex {
    
    static double optimalVal;
    static double[] optimalValues;
    static double[][] a;
    static double[] b;
    static double[] c;
    static int V;

    public static void main(String[] args) {
        // verify one argument: inputFile
        if (args.length != 1) {
            System.out.println("Error. Java program expects 1 argument specifying an input file.");
            return;
        }
        String inputFile = args[0];
            
        // parse input file to create KidneyExchange instance
        KidneyExchange ke = parseInput(inputFile);
        a = ke.a;
        b = ke.b;
        c = ke.c;
        V = ke.V;
        createIMIP();
        printSolution();
    }
    
    public static void printSolution() {
        System.out.println("=================Solution=================");
        System.out.println("Optimal Objective Value: " + optimalVal);
        for (int x = 0; x < optimalValues.length; x++) {
            if (optimalValues[x] > 0) {
                ArrayList<Integer> nodes = new ArrayList<>();
                for (int i = 0; i < V; i++) {
                    if (a[i][x] > 0) {
                        nodes.add(i);
                    }
                }
                printCycle(nodes);
            }
        }
        System.out.println("==========================================");
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
    
    public static void printCycle(ArrayList<Integer> nodes) {
        System.out.print("( ");
        for (int i = 0; i < nodes.size(); i++) {
            System.out.print(nodes.get(i));
            if (i < nodes.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(" )");
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
        
        KidneyExchange ke = new KidneyExchange(Main.L, numVertices, weights, true);
        return ke;
    }
    
    public static IMIPResult solve(IMIP mip, SolverClient client) {
        //mip.setSolveParam(SolveParam.CALC_DUALS, true);
        IMIPResult result = client.solve(mip);
        return result;
    }
    
    public static void createIMIP() {
               IMIP mip;
               ArrayList<Constraint> constraints;
               
               mip = new MIP();
               
               Variable[] vars = new Variable[c.length];
               for (int i = 0; i < c.length; i++) {
                   vars[i] = new Variable("x" + i, VarType.INT, 0, 1);
                   mip.add(vars[i]);
               }
               
               mip.setObjectiveMax(true);
               for (int i = 0; i < c.length; i++) {
                   mip.addObjectiveTerm(c[i], vars[i]);
               }
               
               Constraint[] cons = new Constraint[a.length];
               for (int i = 0; i < a.length; i++) {
                   Constraint con = new Constraint(CompareType.LEQ, 1);
                   for (int j = 0; j < a[i].length; j++) {
                       con.addTerm(a[i][j], vars[j]);
                   }
                   mip.add(con);
                   cons[i] = con;
               }
               
               IMIPResult result = solve(mip, new SolverClient());
               optimalVal = result.getObjectiveValue();
               optimalValues = new double[vars.length];
               for (int i = 0; i < vars.length; i++) {
                   optimalValues[i] = result.getValue("x" + i);
               }
//               System.out.println(result);
//               for (int i = 0; i < vars.length; i++) {
//                   System.out.println("x" + i + ": " + result.getValue("x" + i));
//               }
//               for (int i = 0; i < cons.length/2; i++) {
//                   System.out.println("Dual of n" + i + ": " + result.getDual(cons[i]));
//               }
        
                 
             }

}
