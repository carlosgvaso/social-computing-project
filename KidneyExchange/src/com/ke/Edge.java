package com.ke;

import com.ke.Vector.TYPE;

/**
 * A class to represent a weighted directed edge in graph.
 * 
 * Instance variables:
 *  src             Edge source vertex.
 *  dest            Edge destination vertex.
 *  weight          Edge weight.
 *  reducedWeight   Edge weight in reduced graph.
 */
public class Edge {
    
    public Vector src, dest;
    public double weight, reducedWeight;
    
    public Edge() {
        this.src = null;
        this.dest = null;
        this.weight = 0;
        this.reducedWeight = 0;
    }
    
    public Edge(Vector src, Vector dest) {
        this.src = src;
        this.dest = dest;
        this.weight = calculateEdgeWeight();
        this.reducedWeight = 0;
    }
    
    public Edge(Vector src, Vector dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.reducedWeight = 0;
    }
    
    public double calculateEdgeWeight() {
//        System.out.println(src.type);
//        System.out.println(dest.type);
        if (src.donorType == TYPE.BLACK || dest.patientType == TYPE.BLACK) {
            return 0;
        } else if ((src.donorType == TYPE.BLUE && dest.patientType == TYPE.BLUE) || (src.donorType == TYPE.ORANGE && dest.patientType == TYPE.ORANGE)) {
            return 1;
        } else if ((src.donorType == TYPE.GREEN && dest.patientType == TYPE.GREEN) || (src.donorType == TYPE.RED && dest.patientType == TYPE.RED)) {
            return 1;
        } else if ((src.donorType == TYPE.BLUE && dest.patientType == TYPE.ORANGE) || (src.donorType == TYPE.ORANGE && dest.patientType == TYPE.BLUE)) {
            return 0.5;
        } else if ((src.donorType == TYPE.BLUE && dest.patientType == TYPE.GREEN) || (src.donorType == TYPE.GREEN && dest.patientType == TYPE.BLUE)) {
            return 0.2;
        } else if ((src.donorType == TYPE.RED && dest.patientType == TYPE.ORANGE) || (src.donorType == TYPE.ORANGE && dest.patientType == TYPE.RED)) {
            return 0.7;
        } else if ((src.donorType == TYPE.RED && dest.patientType == TYPE.GREEN) || (src.donorType == TYPE.GREEN && dest.patientType == TYPE.RED)) {
            return 0.6;
        } else {
            return 0;
        }
    }
    
    @Override
    public int hashCode() {
        return src.number + dest.number;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge edge = (Edge) obj;
            if (edge.src == this.src && edge.dest == this.dest) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}