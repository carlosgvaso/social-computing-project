package com.ke;

public class TestCaseGenerator {
    
    public static final int V = 5;  // number of vertices
    
    public static void main(String[] args) {
        
        Graph G = new Graph(V);
        printGraphInput(G);
        
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

}