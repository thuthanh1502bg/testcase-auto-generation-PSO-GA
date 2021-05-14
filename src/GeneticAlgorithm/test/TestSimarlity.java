package GeneticAlgorithm.test;

import java.util.ArrayList;
import java.util.List;

import GeneticAlgorithm.GA.HammingDistance;
import GeneticAlgorithm.GA.HammingPath;

public class TestSimarlity {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> coveredPath = new ArrayList<Integer>();
		for (int i = 1; i <= 5; i++) {
			coveredPath.add(Integer.valueOf(i));
		}
		List<Integer> targetPath = new ArrayList<Integer>();
		for (int i = 1; i <= 6; i++) {
			targetPath.add(Integer.valueOf(i));
		}
		targetPath.add(3, Integer.valueOf(3));
		targetPath.add(3, Integer.valueOf(3));
		
//		System.out.println(coveredPath);
//		System.out.println(targetPath);
//		System.out.println("===================================");
		HammingPath coverPath1 = new HammingPath(coveredPath);
		HammingPath targetPath1 = new HammingPath(targetPath);
	
		System.out.println(coverPath1.makeDistinctListOfNTuples(1));
		System.out.println(targetPath1.makeDistinctListOfNTuples(1));
		System.out.println("===================================");
		System.out.println(coverPath1.makeDistinctListOfNTuples(2));
		System.out.println(targetPath1.makeDistinctListOfNTuples(2));
		System.out.println("===================================");
		System.out.println(coverPath1.makeDistinctListOfNTuples(3));
		System.out.println(targetPath1.makeDistinctListOfNTuples(3));
		System.out.println("===================================");
		System.out.println(coverPath1.makeDistinctListOfNTuples(4));
		System.out.println(targetPath1.makeDistinctListOfNTuples(4));
		System.out.println("===================================");
		System.out.println(coverPath1.makeDistinctListOfNTuples(5));
		System.out.println(targetPath1.makeDistinctListOfNTuples(5));
		System.out.println("===================================");
		float [] s=new float[100];
		
		float SIMARLIRITY=0;
	
		HammingDistance hammingDistance = new HammingDistance(coverPath1,targetPath1);
		for(int i=1;i<=hammingDistance.calueMin();i++) {
			s[i]=hammingDistance.calculateSymmetricDifference(i);
			SIMARLIRITY+=s[i];
		}
		for(int i=1;i<hammingDistance.calueMin();i++) {
			System.out.println("SIMARLIRITY "+i+"_ order  " + ": " +s[i]);
		}

		

		System.out.println("SIMARLIRITY sử dụng HammingDistance :  "+ SIMARLIRITY);
		
	
	}


}
