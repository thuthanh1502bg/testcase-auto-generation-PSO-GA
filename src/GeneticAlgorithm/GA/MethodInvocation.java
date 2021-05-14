package GeneticAlgorithm.GA;

import java.util.*;

/**
 * Định nghĩa method
 * 
 */
public class MethodInvocation extends Statement {

	/**
	 * Khởi tạo MethodInvocation action.
	 *
	 * @param objVar
	 *            Target of method invocation ($xN in $xN.m());
	 * @param methodName
	 *            Method name (m in $xN.m()).
	 * @param formalParams
	 *            Parameter types.
	 * @param vals
	 *            Input values (e.g., "$x0", "23")
	 */
	MethodInvocation(String objVariable, String methodName, List parameterTypes, List parameterValues) {
		super();
		this.setTargetObject(objVariable);
		this.setName(methodName);
		this.setParameterTypes(parameterTypes);
		this.setParameterValues(parameterValues);
	}

	/**
	 * Used when cloning chromosome.
	 */
	public Object clone() {
		return new MethodInvocation(this.getTargetObject(), this.getName(), this.getParameterTypes(), this.getParameterValues());
	}


	/**
	 * Tiền tố của method có tham số
	 *
	 * Example: "$x0.m", where the method invocation is $x0.m(int)
	 */
	String actionPrefix() {
		return this.getTargetObject() + "." + this.getName();
	}

	/**
	 * Java code khi gọi method (tạo testcase )
	 *
	 * Example: "x0.m(4);", where the chromosome action is $x0.m(int)@4
	 */
	String toCode() {
		String s = "    ";
		if (this.getExpectResult() != null) {
			s += "assertEquals(\"" + this.getExpectResult() + "\",String.valueOf(";
			s += this.getTargetObject().substring(1) + "." + this.getName();
			s += "(";
			Iterator i = this.getParameterTypes().iterator();
			Iterator j = this.getParameterValues().iterator();
			while (i.hasNext() && j.hasNext()) {
				String param = (String) j.next();
				if (param.startsWith("$"))
					param = param.substring(1);
				if (param.contains(" ")) {
					String type = i.next().toString();
					int index = type.indexOf("[") + 1;
					if (type.substring(0, index - 1).equals("float")) {
						String init = "\t" + type.substring(0, index) + "] t = {(float)"
								+ param.replaceAll(" ", ",(float)") + "};\n";
						if (s.endsWith("("))
							s += "t";
						else
							s += ", " + "t";
						s = init + s;

					} else {
						String init = "\t" + type.substring(0, index) + "] t = {" + param.replaceAll(" ", ",") + "};\n";
						if (s.endsWith("("))
							s += "t";
						else
							s += ", " + "t";
						s = init + s;
					}
				} else {
					if (s.endsWith("("))
						s += param;
					else
						s += ", " + param;
				}
			}
			s += ")));";
			return s;

		} else {
			s += this.getTargetObject().substring(1) + "." + this.getName();
			s += "(";
			Iterator i = this.getParameterTypes().iterator();
			Iterator j = this.getParameterValues().iterator();
			while (i.hasNext() && j.hasNext()) {
				String param = (String) j.next();
				if (param.startsWith("$"))
					param = param.substring(1);
				if (param.contains(" ")) {
					String type = i.next().toString();
					int index = type.indexOf("[") + 1;
					if (type.substring(0, index - 1).equals("float")) {
						String init = "\t" + type.substring(0, index) + "] t = {(float)"
								+ param.replaceAll(" ", ",(float)") + "};\n";
						if (s.endsWith("("))
							s += "t";
						else
							s += ", " + "t";
						s = init + s;

					} else {
						String init = "\t" + type.substring(0, index) + "] t = {" + param.replaceAll(" ", ",") + "};\n";
						if (s.endsWith("("))
							s += "t";
						else
							s += ", " + "t";
						s = init + s;
					}
				} else {
					if (s.endsWith("("))
						s += param;
					else
						s += ", " + param;
				}
			}
			s += ");";
			s += "\n    System.out.println(\"OK\");";
			return s;
		}

	}

	/**
	 * Tập các biến sử dụng cho action (e.g., {$x1, $x2} for $x1.A($x2)).
	 */
	Set getUsedVariablesInMethodInvocation() {
		Set use = new HashSet();
		use.add(getTargetObject());
		Iterator i = getParameterObjects().iterator();
		while (i.hasNext())
			use.add(i.next());
		return use;
	}

}
