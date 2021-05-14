package GeneticAlgorithm.test;

import java.util.ArrayList;
import java.util.List;

import GeneticAlgorithm.GA.HammingPath;

public class TestDistinctHammingPath {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> nodes = new ArrayList<Integer>();
		for (int i = 1; i <= 5; i++) {
			nodes.add(Integer.valueOf(i));
		}
		nodes.add(3, Integer.valueOf(3));
		nodes.add(3, Integer.valueOf(3));
		
		System.out.println(nodes);
		
		HammingPath hammingPath = new HammingPath(nodes);
		System.out.println(hammingPath.makeDistinctListOfNTuples(1));
		System.out.println(hammingPath.makeDistinctListOfNTuples(2));
		System.out.println(hammingPath.makeDistinctListOfNTuples(3));
		System.out.println(hammingPath.makeDistinctListOfNTuples(4));
		System.out.println(hammingPath.makeDistinctListOfNTuples(5));
	}

}
