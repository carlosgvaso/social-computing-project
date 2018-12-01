package com.ke;

import java.util.ArrayList;

/**
 * A class to represent a connected, directed and weighted graph.
 * 
 * Instance variables:
 *  V           Number or vertices.
 *  E           Number of edges.
 *  edges       Array with all the edges (Edge objects) in the graph.
 * 
 * To modify edge 0 in graph to point from vertex 2 to vertex 3 with a weight of 1:
 *  graph.edge[0].src = 2; 
 *  graph.edge[0].dest = 3; 
 *  graph.edge[0].weight = 1; 
 */
public class Graph { 
    
    public int V, E;
    public ArrayList<Vector> vectors;
    public ArrayList<Edge> edges;
    
    public Graph(int v) {
        this.V = v;
        this.vectors = new ArrayList<>();
        this.edges = new ArrayList<>();
        for (int i = 0; i < v; i++) {
            Vector vect = new Vector(i);
            vectors.add(vect);
        };
        for (int i = 0; i < v; i++) {
            for (int j = 0; j < v; j++) {
                   Edge edge = new Edge(vectors.get(i), vectors.get(j));
                   if (edge.weight > 0) {
                       edges.add(edge);
                   }
            }
        }
        this.E = edges.size();
    }
    
    public Graph(int v, int e) {
        this.V = v;
        this.vectors = new ArrayList<>();
        this.edges = new ArrayList<>();
        for (int i = 0; i < v; i++) {
            Vector vect = new Vector(i);
            vectors.add(vect);
        };
        for (int i=0; i<e; i++) {
            this.edges.add(new Edge());
        }
        this.E = edges.size();
    }
    
    public Graph(int v, int e, ArrayList<Vector> vectors, ArrayList<Edge> edges) {
        this.V = v;
        this.E = e;
        this.vectors = vectors;;
        this.edges = edges;
    }
    
    public Graph(int v, double[][] weights) {
        this.V = v;
        this.vectors = new ArrayList<>();
        this.edges = new ArrayList<>();
        for (int i = 0; i < v; i++) {
            Vector vect = new Vector(i);
            vectors.add(vect);
        };
        for (int i = 0; i < v; i++) {
            for (int j = 0; j < v; j++) {
                Edge edge = new Edge(vectors.get(i), vectors.get(j), weights[i][j]);
                if (edge.weight > 0) {
                    edges.add(edge);
                }
            }
        }
        this.E = edges.size();
    }
    
    public Graph(Graph G, double[][] weights) {
        this.V = G.V;
        this.E = G.E;
        this.vectors = new ArrayList<Vector>(G.vectors);
        this.edges = new ArrayList<Edge>(G.edges);
        for (int i = 0; i < G.E; i++) {
           int src = edges.get(i).src.number;
           int dest = edges.get(i).dest.number;
           edges.get(i).reducedWeight = weights[src][dest];
        }
    }
}