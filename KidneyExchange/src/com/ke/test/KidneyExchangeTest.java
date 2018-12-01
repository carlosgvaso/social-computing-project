package com.ke.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.ke.KidneyExchange;

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
	
//	@Test
//	public void testTraversePreds() {
//		// Assume L=3, |V|=3, L=3
//		int V = 3;
//		int E = 3;
//		int L = 3;
//		KidneyExchange k = new KidneyExchange(L, V, E);
//		
//		/*
//		 * Set up preds as follows:
//		 * 
//		 *   | 0  1  2
//		 * ------------
//		 * 0 |-1 -1 -1
//		 * 1 |-1  0 -1
//		 * 2 |-1  0  1
//		 */
//		int preds[][] = new int[V][L];
//		for (int i=0; i<L; i++) {
//			for (int v=0; v<V; v++) {
//				preds[v][i] = -1;
//			}
//		}
//		preds[1][1] = 0;
//		preds[1][2] = 0;
//		preds[2][2] = 1;
//		
//		ArrayList<Integer> cycle = new ArrayList<Integer>(L);
//		
//		// Find the predecessors of v=2 at position i=L-1
//		cycle.add(2);
//		cycle.add(1);
//		cycle.add(0);
//		assertTrue(k.traversePreds(2, preds, L-1).equals(cycle));
//	}
//	
//	@Test
//	public void testGetNegativeCycles() {
//		// Assume L=3, |V|=3, |E|=3
//		KidneyExchange k = new KidneyExchange(3, 5, 9);
//		
////		/cles.equals(cycles_exp));
//	}
}
