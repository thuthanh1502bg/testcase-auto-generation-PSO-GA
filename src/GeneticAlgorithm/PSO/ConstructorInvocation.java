package GeneticAlgorithm.PSO;


import java.util.*;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * Định nghĩa Constructor
 * 
 * @author AnhBTN
 */

public class ConstructorInvocation extends Statement {
	/**
	 * Khởi tạo ConstructorInvocation action.
	 *
	 * @param objVariable
	 *            Bên trái của constructor invocation $xN=A();
	 * @param constructorName
	 *            Constructor name.
	 * @param parameterTypes
	 *            Parameter types.
	 * @param parameterValues
	 *            Input values (e.g., "$x0", "23")
	 */
	ConstructorInvocation(String objVariable, String constructorName, List parameterTypes, List parameterValues) {
		super();
		this.setTargetObject(objVariable);
		this.setName(constructorName);
		this.setParameterTypes(parameterTypes);
		this.setParameterValues(parameterValues);
	}

	/**
	 * Used when cloning chromosomes.
	 */
	public Object clone() {
		return new ConstructorInvocation(this.getTargetObject(), this.getName(), this.getParameterTypes(), this.getParameterValues());
	}

	/**
	 * Tiền tố của constructor có tham số
	 *
	 * Example: "$x0=A", where the constructor invocation is $x0=A(int)
	 */
	String actionPrefix() {
		return this.getTargetObject() + "=" + this.getName();
	}

	/**
	 * Generate Java code when constructor is invoked while create unit test
	 *
	 * Example: "A x0 = new A(4);", chromosome action will be $x0=A(int)@4
	 */
	String toCode() {
		String s = "    ";
		s += this.getName() + " " + this.getTargetObject().substring(1) + " = new " + this.getName();
		s += "(";
		Iterator i = this.getParameterTypes().iterator();
		Iterator j = this.getParameterValues().iterator();
		while (i.hasNext() && j.hasNext()) {
			String param = (String) j.next();
			if (param == null)
				param = "null";
			if (param.startsWith("$x"))
				param = param.substring(1);
			if (s.endsWith("("))
				s += param;
			else
				s += ", " + param;
		}
		s += ");";
		return s;
	}
	
}
