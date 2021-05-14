package GeneticAlgorithm.GA;

import java.util.*;

/**
 * Quản lý chữ kí method/constructor
 */
public class MethodSignature {
	/**
	 * Tên method
	 */
	private String name;

	/**
	 * List parameter
	 */
	private List parameters;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the parameters
	 */
	public List getParameters() {
		return parameters;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(List parameters) {
		this.parameters = parameters;
	}

	/**
	 * @param name
	 * @param parameters
	 */
	public MethodSignature(String name, List parameters) {
		super();
		this.name = name;
		this.parameters = parameters;
	}

	public MethodSignature() {
	}
}
