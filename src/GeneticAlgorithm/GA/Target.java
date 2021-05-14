package GeneticAlgorithm.GA;

import java.util.LinkedList;
import java.util.List;

abstract class Target {

	public List getSubTargets() {
		List subTargets = new LinkedList();
		subTargets.add(this);
		return subTargets;
	}

	
}
