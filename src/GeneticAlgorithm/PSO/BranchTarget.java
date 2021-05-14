package GeneticAlgorithm.PSO;

import java.util.Iterator;
import java.util.Set;

/**
 * Định nghĩa branch
 */
class BranchTarget extends Target implements Comparable<BranchTarget> {
	/**
	 * Branch sẽ được cover
	 */
	int branch;

	/**
	 * Khởi tạo constructor có tham số
	 */
	public BranchTarget(int br) {
		branch = br;
	}

	/**
	 * Khởi tạo constructor không tham số
	 */
	public BranchTarget() {
		super();
	}

	/**
	 * Used in Map's.
	 */
	public int hashCode() {
		return branch;
	}

	/**
	 * Used in Map's.
	 */
	public boolean equals(Object obj) {
		BranchTarget tgt = (BranchTarget) obj;
		return branch == tgt.branch;
	}

	/**
	 * toString 
	 */
	public String toString() {
		return Integer.toString(branch);
	}

	/**
	 * toString
	 * @param set
	 * @return
	 */
	public String toString(Set set) {
		String s = "";
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			s += "[" + it.next() + "]\n";
		}
		return s;

	}

	@Override
	public int compareTo(BranchTarget branchTarget) {

		return this.branch - branchTarget.branch;
	}

}
