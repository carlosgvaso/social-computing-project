package com.ke.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.ke.KidneyExchange;
import com.ke.KidneyExchange.CycleStruct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
		KidneyExchange k = new KidneyExchange(3, 5, 9);
		
		// Set up graph
		k.G.edge[0].src = 0;
		k.G.edge[0].dest = 1;
		k.G.edge[0].weight = 1;
		k.G.edge[0].dual = -1;
		
		k.G.edge[1].src = 1;
		k.G.edge[1].dest = 0;
		k.G.edge[1].weight = 1;
		k.G.edge[1].dual = -1;
		
		k.G.edge[2].src = 1;
		k.G.edge[2].dest = 2;
		k.G.edge[2].weight = 1;
		k.G.edge[2].dual = -2;
		
		k.G.edge[3].src = 2;
		k.G.edge[3].dest = 1;
		k.G.edge[3].weight = 1;
		k.G.edge[3].dual = -1;
		
		k.G.edge[4].src = 2;
		k.G.edge[4].dest = 3;
		k.G.edge[4].weight = 1;
		k.G.edge[4].dual = -1;
		
		k.G.edge[5].src = 3;
		k.G.edge[5].dest = 2;
		k.G.edge[5].weight = 1;
		k.G.edge[5].dual = -2;
		
		k.G.edge[6].src = 2;
		k.G.edge[6].dest = 4;
		k.G.edge[6].weight = 1;
		k.G.edge[6].dual = -1;
		
		k.G.edge[7].src = 4;
		k.G.edge[7].dest = 0;
		k.G.edge[7].weight = 1;
		k.G.edge[7].dual = -1;
		
		k.G.edge[8].src = 4;
		k.G.edge[8].dest = 3;
		k.G.edge[8].weight = 1;
		k.G.edge[8].dual = -1;
		
		
		// Expected cycles
		ArrayList<Integer> cycle_exp = new ArrayList<Integer>(k.L);
		ArrayList<ArrayList<Integer>> cycles_exp = new ArrayList<ArrayList<Integer>>();
		
		cycles_exp.clear();
		cycle_exp.clear();
		cycle_exp.add(2);
		cycle_exp.add(1);
		cycle_exp.add(0);
		cycles_exp.add(new ArrayList<Integer>(cycle_exp));
		cycle_exp.clear();
		
		cycle_exp.add(0);
		cycle_exp.add(2);
		cycle_exp.add(1);
		cycles_exp.add(new ArrayList<Integer>(cycle_exp));
		cycle_exp.clear();
		
		cycle_exp.add(1);
		cycle_exp.add(0);
		cycle_exp.add(2);
		cycles_exp.add(new ArrayList<Integer>(cycle_exp));
		cycle_exp.clear();
		

		//ArrayList<ArrayList<Integer>> cycles = k.getNegativeCycles(k.G, k.L);

		ArrayList<ArrayList<Integer>> cycles = k.GetDiscountedPositivePriceCycles(k.G, k.L, 8);
		ArrayList<CycleStruct> feasiblecycles = k.DetermineFeasibleWeightedCycles(cycles);
		CycleStruct bestCycleSet = k.DetermineHighestWeightedCycle(feasiblecycles);
		//ArrayList<ArrayList<Integer>> cycles2 = k.GetDiscountedPositivePriceCycles(k.G, k.L, .8);
		
		//HashSet<HashSet<Integer>> cycleSet = new HashSet<HashSet<Integer>>();
		
		System.out.print("cycles: {");
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
		
		/*System.out.print("cycles2: {");
		if(cycles2.size() > 0) {
			for (int i=0; i<cycles2.size(); i++) {
				//HashSet cSet = new HashSet<Integer>();
				
				System.out.print("(");
				for (int j=0; j<cycles2.get(i).size(); j++) {
					if (j != cycles2.get(i).size()-1) {
						System.out.print(cycles2.get(i).get(j) + ", ");
					} else {
						System.out.print(cycles2.get(i).get(j));
					}
				}
				if (i != cycles2.size()-1) {
					System.out.print("), ");
				} else {
					System.out.println(")}");
				}
			}
		}
		else
			System.out.println("}");*/
		
		System.out.println("Feasible Cycles");
		for (int m = 0; m < feasiblecycles.size(); m++) {
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
				System.out.print(firstFinal + "), ");
			}

			System.out.println("}");
		}
		
		System.out.println("Maximum Weight Cycle");
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
		assertTrue(cycles.equals(cycles_exp));
	}
}
