package GeneticAlgorithm.PSO;

import java.util.*;

/**
 * Định nghĩa Action
 *
 */
public class Statement implements Cloneable {
	/**
	 * Lưu target object.
	 */
	private String targetObject;

	/**
	 * Action name.
	 */
	private String name;

	/**
	 * Lưu các loại param
	 *
	 * Example: ("int", "B", "A", "int").
	 */
	private List parameterTypes = new LinkedList();

	/**
	 * Giá trị tham số
	 *
	 * Example: ("12", "$x2", "$x0", "23")
	 */
	private List parameterValues = new LinkedList();
	/**
	 * 
	 */
	private String expectResult;
	
	public Statement() {
		
	}
	
	public Object clone() {
		Statement act = new Statement();
		act.setTargetObject(this.targetObject);
		act.setName(this.name);
		act.setParameterTypes(this.parameterTypes);
		act.setParameterValues(this.parameterValues);
		act.setExpectResult(this.expectResult);
		return act;
	}

	/**
	 * get ParameterValues
	 */
	public List getParameterValues() {
		return parameterValues;
	}
	
	public void setParameterValues(List parameterValues) {
		this.parameterValues = parameterValues;
	}
	
	public List getParameterTypes() {
		return this.parameterTypes;
	}
	
	public void setParameterTypes(List parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	/**
	 * return a list of parameters which are object instances
	 *
	 * Example: ("$x2", "$x0") trong tập ("12", "$x2", "$x0", "23")
	 */
	public List getParameterObjects() {
		List paramObjects = new LinkedList();
		if (parameterTypes == null || parameterValues == null)
			return paramObjects;
		Iterator i = parameterTypes.iterator();
		Iterator j = parameterValues.iterator();
		while (i.hasNext() && j.hasNext()) {
			String paramType = (String) i.next();
			String param = (String) j.next();
			if (!ChromosomeFormer.isPrimitiveType(paramType) && !ChromosomeFormer.isPrimitiveArrayType(paramType))
				paramObjects.add(param);
		}
		return paramObjects;
	}

	/**
	 * Get targetObject
	 */
	public String getTargetObject() {
		return targetObject;
	}
	
	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	/**
	 * get name of action
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getExpectResult() {
		return this.expectResult;
	}
	
	public void setExpectResult(String expectResult) {
		this.expectResult = expectResult;
	}

	/**
	 * Code biểu diễn cho action.
	 */
	String toCode() {
		return "";
	}

	/**
	 * Chuỗi mô tả action
	 */
	String actionDescription() {
		return actionPrefix() + parameterDescription();
	}

	/**
	 * tiền tố action
	 */
	String actionPrefix() {
		return "";
	}

	/**
	 * String biểu diễn kiểu param
	 */
	String parameterDescription() {
		try {
			if (parameterTypes == null || parameterValues == null) {
				return "";
			}
			String s = "(";
			Iterator i = parameterTypes.iterator();
			Iterator j = parameterValues.iterator();
			while (i.hasNext() && j.hasNext()) {
				String param = (String) i.next();
//				String paramId = (String) j.next();

				Object paramId1= (Object) j.next();
				String paramId=paramId1.toString();
				if (!ChromosomeFormer.isPrimitiveType(param) && !ChromosomeFormer.isPrimitiveArrayType(param)) {
					param = paramId;
				}
				if (s.equals("(")) {
					s += param;
				}
				else {
					s += "," + param;
				}
			}
			s += ")";
			return s;
		}catch (Exception e){
			System.out.println("DESCRIPTION: "+e.getMessage());
			return null;
		}
	}

	/**
	 * String biểu diễn giá trị param
	 */
	String actualValuesToString() {
//		if (parameterValues == null || parameterTypes == null)
//			return "";
//		String s = "";
//		Iterator i = parameterValues.iterator();
//		Iterator j = parameterTypes.iterator();
//		while (i.hasNext() && j.hasNext()) {
//			String paramVal = (String) i.next();
//			String paramType = (String) j.next();
//			if (ChromosomeFormer.isPrimitiveType(paramType) || ChromosomeFormer.isPrimitiveArrayType(paramType)) {
//				if (s.equals(""))
//					s += paramVal;
//				else
//					s += "," + paramVal;
//			}
//		}
//		return s;
		String resultString = "";
		List<String> actualValues = this.getActualValues();
		if (actualValues == null) return resultString;
		Iterator valIterator = actualValues.iterator();
		while (valIterator.hasNext()) {
			String valString = (String) valIterator.next();
			if (resultString.equals(""))
				resultString += valString;
			else {
				resultString += "," + valString;
			}
		}
		return resultString;
	}
	
	List<String> getActualValues() {
		List<String> actualValues = new ArrayList<String> ();
		if (parameterValues == null || parameterTypes == null)
			return null;
		Iterator i = parameterValues.iterator();
		Iterator j = parameterTypes.iterator();
		while (i.hasNext() && j.hasNext()) {
			String paramVal = (String) i.next();
			String paramType = (String) j.next();
			if (ChromosomeFormer.isPrimitiveType(paramType) || ChromosomeFormer.isPrimitiveArrayType(paramType)) {
				actualValues.add(paramVal);
			}
		}
		return actualValues;
	}
 
	/**
	 * Thay đổi ngẫu nhiên một giá trị của action.
	 *
	 * @param valIndex
	 *            chỉ số của giá trị primitive type thay đổi
	 */
	public void changeInputValue(int valIndex) {
		if (parameterValues == null || parameterTypes == null)
			return;
		List newParamVals = new LinkedList();
		int k = 0;
		Iterator i = parameterValues.iterator();
		Iterator j = parameterTypes.iterator();
		while (i.hasNext() && j.hasNext()) {
			String paramVal = (String) i.next();
			String paramType = (String) j.next();
			if (ChromosomeFormer.isPrimitiveArrayType(paramType) && k == valIndex) {
				int length = ChromosomeFormer.getLengthArray();
				String newVal = ChromosomeFormer.buildArrayValue(paramType, length);
				newParamVals.add(newVal);
			} else if (ChromosomeFormer.isPrimitiveType(paramType) && k == valIndex) {
				String newVal = ChromosomeFormer.buildValue(paramType);
				newParamVals.add(newVal);
			} else {
				newParamVals.add(paramVal);
			}
			if (ChromosomeFormer.isPrimitiveType(paramType) || ChromosomeFormer.isPrimitiveArrayType(paramType))
				k++;
		}
		parameterValues = newParamVals;
	}

	/**
	 * Số lượng primitive type ( int, float..)
	 */
	public int countPrimitiveTypes() {
		int n = 0;
		if (parameterValues == null || parameterTypes == null)
			return n;
		Iterator i = parameterTypes.iterator();
		while (i.hasNext()) {
			String paramType = (String) i.next();
			if (ChromosomeFormer.isPrimitiveType(paramType) || ChromosomeFormer.isPrimitiveArrayType(paramType)) {
				n++;
			}
		}
		return n;
	}

}
