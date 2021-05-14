package GeneticAlgorithm.GA;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class HammingPath {
	
	private List<Integer> nodes;
	
	public HammingPath () {
		this.nodes = new ArrayList<Integer> (); 
	}
	
	public HammingPath (List<Integer> sampleNodes) {
		this.nodes = sampleNodes;
	}

	public List<Integer> getNodes() {
		return nodes;
	}

	public void setNodes(List<Integer> nodes) {
		this.nodes = nodes;
	}
	
	public List<List> makeListofNTuples (int nOrder) {
		List<List> result = new ArrayList<List> ();
		for (int i = 0; i < nodes.size(); i++) {
			List<Integer> nTuple = new ArrayList<Integer>();
			int elementIndexInNTuple = i;
			for (int j = 0; j < nOrder; j++) {
				nTuple.add(nodes.get(elementIndexInNTuple));
				elementIndexInNTuple++;
			}
			result.add(nTuple);
			if (elementIndexInNTuple >= nodes.size())
				break;
		}
		return result;
	}
	
	public List<List> makeDistinctListOfNTuples (int nOrder) {
		List<List> result = new ArrayList<List>();
		List<List> listOfNTuples = this.makeListofNTuples(nOrder);
		for (int i = 0; i < listOfNTuples.size(); i++) {
			if (!result.contains(listOfNTuples.get(i))) 
				result.add(listOfNTuples.get(i));
		}
		return result;
	}

	
}
