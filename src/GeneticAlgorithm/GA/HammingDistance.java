package GeneticAlgorithm.GA;

import java.util.ArrayList;
import java.util.List;

public class HammingDistance {
	private HammingPath coveredPath;
	private HammingPath targetPath;
	
	public HammingDistance() {
		 
	}
	
	public HammingDistance(HammingPath coveredPath, HammingPath targetPath) {
		this.coveredPath = coveredPath;
		this.targetPath = targetPath;
	}

	public HammingPath getCoveredPath() {
		return coveredPath;
	}

	public void setCoveredPath(HammingPath coveredPath) {
		this.coveredPath = coveredPath;
	}

	public HammingPath getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(HammingPath targetPath) {
		this.targetPath = targetPath;
	}
	public int calueMin() {
		int lenght=Math.min(coveredPath.makeDistinctListOfNTuples(1).size(),
				targetPath.makeDistinctListOfNTuples(1).size());
		return lenght;
		
	}
	
	public float calculateSymmetricDifference(int nOrder) {
		int intersection=0;// so phan tu giong nhau giua coveredPath va targetPath 
		int union;// hop coveredPath cua targetPath
		int symmetricDifference;// D - so phan tu khac nhau coveredPath va targetPath
		
		float normalizedDistance;//- N
		float mSimarlity;//-M
		//int []size0fHammingPath=new int [100];// kich thuoc path S
		float[] simalirity=new float[100];//SIMALIRITY[nOrder]
		float[] weight=new float[100];// W 
		weight[1]=1;
		
			int s1=coveredPath.makeDistinctListOfNTuples(nOrder).size();
			int s2=targetPath.makeDistinctListOfNTuples(nOrder).size();
			for (int i = 0; i <s1 ; i++) {
	            for (int j = 0; j <s2; j++) {
	                if (coveredPath.makeDistinctListOfNTuples(nOrder).get(i).
	                		equals(targetPath.makeDistinctListOfNTuples(nOrder).get(j)) 
	                		) {
	                	intersection++;// so phan tu giong nhau giua coveredPath va targetPath
	                }
	                
	            }
	        }
	        symmetricDifference=s1+s2-2*intersection;// D so phan tu khac nhau coveredPath va targetPath
			union= intersection + symmetricDifference;// hop cua coveredPath va targetPath
			
			normalizedDistance=(float)(1.0*symmetricDifference/union);// N
			mSimarlity=1-normalizedDistance; // M
			
				for(int i=2;i<=nOrder;i++) {
					weight[i]=weight[i-1]*targetPath.makeDistinctListOfNTuples(i-1).size();//W					
				}
		simalirity[nOrder]=mSimarlity*weight[nOrder];
		return simalirity[nOrder];
		
	
	
	}
	
	
	
}
