package com.ke.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.ke.KidneyExchange;

import java.util.ArrayList;

/**
 * Test class for KidneyExchange.
 *
 * @author	Jose Carlos Martinez Garcia-Vaso jcm3767 <carlosgvaso@gmail.com>
 * @author Trevor Anderson
 * @author Bryson Banks
 * @author Alexander Hoganson
 */
public class KidneyExchangeTest {
	
	@Test
	public void testTraversePreds() {
		// Assume L=3, |V|=3, L=3
		int V = 3;
		int E = 3;
		int L = 3;
		KidneyExchange k = new KidneyExchange(L, V, E);
		
		/*
		 * Set up preds as follows:
		 * 
		 *   | 0  1  2
		 * ------------
		 * 0 |-1 -1 -1
		 * 1 |-1  0 -1
		 * 2 |-1  0  1
		 */
		int preds[][] = new int[V][L];
		for (int i=0; i<L; i++) {
			for (int v=0; v<V; v++) {
				preds[v][i] = -1;
			}
		}
		preds[1][1] = 0;
		preds[1][2] = 0;
		preds[2][2] = 1;
		
		ArrayList<Integer> cycle = new ArrayList<Integer>(L);
		
		// Find the predecessors of v=2 at position i=L-1
		cycle.add(2);
		cycle.add(1);
		cycle.add(0);
		assertTrue(k.traversePreds(2, preds, L-1).equals(cycle));
	}
	
	@Test
	public void testGetNegativeCycles() {
		// Assume L=3, |V|=3, |E|=3
		KidneyExchange k = new KidneyExchange(3, 3, 3);
		
		// Set up graph
		k.G.edge[0].src = 0;
		k.G.edge[0].dest = 1;
		k.G.edge[0].weight = -1;
		
		k.G.edge[1].src = 1;
		k.G.edge[1].dest = 2;
		k.G.edge[1].weight = -2;
		
		k.G.edge[2].src = 2;
		k.G.edge[2].dest = 0;
		k.G.edge[2].weight = -2;
		
		// Expected cycles
		ArrayList<Integer> cycle_exp = new ArrayList<Integer>(k.L);
		ArrayList<ArrayList<Integer>> cycles_exp = new ArrayList<ArrayList<Integer>>();
		cycle_exp.add(2);
		cycle_exp.add(1);
		cycle_exp.add(0);
		cycles_exp.add(cycle_exp);
		
		k.getNegativeCycles(k.G, k.L);
		
		System.out.print("cycles: {");
		for (int i=0; i<k.cycles.size(); i++) {
			System.out.print("(");
			for (int j=0; j<k.cycles.get(i).size(); j++) {
				if (j != k.cycles.get(i).size()-1) {
					System.out.print(k.cycles.get(i).get(j) + ", ");
				} else {
					System.out.print(k.cycles.get(i).get(j));
				}
			}
			if (i != k.cycles.size()-1) {
				System.out.print("), ");
			} else {
				System.out.println(")}");
			}
		}
		
		assertTrue(k.cycles.equals(cycles_exp));
	}
}
