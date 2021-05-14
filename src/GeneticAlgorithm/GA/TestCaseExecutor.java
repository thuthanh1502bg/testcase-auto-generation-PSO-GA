package GeneticAlgorithm.GA;

import GeneticAlgorithm.convert.ConvertFactory;
import GeneticAlgorithm.convert.GetArray;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCaseExecutor {
    /**
     * biểu diễn của test case.
     *
     * @see ChromosomeFormer
     */
    private String chromosome = "";
    /**
     *
     */
    String expectResult;

    /**
     * Đếm số lần thực hiện
     */
    public static int testCaseExecutions = 0;

    /**
     * Mảng các đối tượng để tạo test case execution.
     * <p>
     * Mỗi đối tượng $xN trong chromosome được liên kết với objects[N].
     */
    private Object[] objects;

    /**
     * Mảng các class để tạo test case execution. Mỗi đối tượng $xN trong chromosome
     * được liên kết với classes[N].
     */
    private Class[] classes;

    /**
     * Trả về object thứ n khi tạo test case execution.
     *
     * @param n
     * @return objects[n]
     */
    public Object objectAt(int n) {
        return objects[n];
    }

    /**
     * Wraps primitive types and maps object vars into object types. Kết hợp các
     * primitive type và ánh xạ các biến thành object types
     *
     * @param type Kiểu primitive type or 1 object var.
     * @return Wrapper type/class.
     */
    public Class mapTypeToClass(String type) {
        try {
            if (ChromosomeFormer.isPrimitiveArrayType(type)) {
                if (type.startsWith("java.lang.String"))
                    return java.lang.String[].class;
                else if (type.startsWith("boolean"))
                    return boolean[].class;
                else if (type.startsWith("double"))
                    return double[].class;
                else if (type.startsWith("float"))
                    return float[].class;
                else if (type.startsWith("long"))
                    return long[].class;
                else
                    return int[].class;
            }  else if (ChromosomeFormer.isObjectArrayType(type)) {
                String singleType = type.substring(0, type.indexOf("["));
                Class requested = null;
                for (Class cl : classes
                ) {
                    if (cl.getName().indexOf(singleType) > -1) {
                        requested = cl;
                        break;
                    }
                }
                if (requested != null) {
                    return Array.newInstance(requested, 1).getClass();
                }

            } else {
                if (type.indexOf("[") != -1)
                    type = type.substring(0, type.indexOf("["));
                if (ChromosomeFormer.isPrimitiveType(type)) {
                    if (type.equals("java.lang.String"))
                        return Class.forName("java.lang.String");
                    else if (type.equals("boolean"))
                        return Boolean.TYPE;
                    else if (type.equals("double"))
                        return Double.TYPE;
                    else if (type.equals("float"))
                        return Float.TYPE;
                    else if (type.equals("long"))
                        return Long.TYPE;
                    else
                        return Integer.TYPE;
                }
            }

            int k = Integer.parseInt(type.substring(2));
            return classes[k];
        }
        catch (ClassNotFoundException e) {
            System.err.println("Class not found. " + e);
            e.printStackTrace();
//            System.exit(1);
        } catch (NumberFormatException e){
            System.err.println("Number Format Exception "+e);
            e.printStackTrace();
//            System.exit(1);
        } catch (StringIndexOutOfBoundsException ex){
            System.err.println("String Index Out Of Bound: "+ex);
            ex.printStackTrace();
//            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException ex){
            System.err.println("Array Index Out Of Bound: "+ex);
            ex.printStackTrace();
//            System.exit(1);
        }
        return null;
    }

    /**
     * Kết hợp value với obj và trả về previously allocated objects
     *
     * @param val either $xN or an integer value
     * @return Either objects[N] for $xN, or new Integer(val).
     */
    public Object[] mapArrayValueToObject(String val) {
        if (val.equals("null"))
            return null;

        String[] valArray = val.split(" ");
        Object[] obj = new Object[valArray.length];
        Pattern p = Pattern.compile("\\$x(\\d+)");

        for (int i = 0; i < valArray.length; i++) {
            Object temp;
            Matcher m = p.matcher(valArray[i]);
            if (m.find())
                obj[i] = objects[Integer.parseInt(m.group(1))];
            else if (valArray[i].startsWith("\"") && valArray[i].endsWith("\""))
                obj[i] = valArray[i].substring(1, val.length() - 1);
            else if (valArray[i].equals("true") || valArray[i].equals("false"))
                obj[i] = new Boolean(valArray[i]);
            else if (valArray[i].indexOf(".") != -1)
                obj[i] = new Double(Double.parseDouble(valArray[i]));
            else {

                obj[i] = Integer.parseInt(valArray[i]);
            }
        }
        return obj;
    }

    /**
     * @param val
     * @return
     */
    public Object mapValueToObject(String val) {
        if (val.equals("null"))
            return null;
        Object obj = null;
        Pattern p = Pattern.compile("\\$x(\\d+)");
        Matcher m = p.matcher(val);
        if (m.find())
            obj = objects[Integer.parseInt(m.group(1))];
        else if (val.startsWith("\"") && val.endsWith("\""))
            obj = val.substring(1, val.length() - 1);
        else if (val.equals("true") || val.equals("false"))
            obj = new Boolean(val);
        else if (val.indexOf(".") != -1)
            obj = new Double(Double.parseDouble(val));
        else
            obj = new Integer(Integer.parseInt(val));
        return obj;
    }

    /**
     * Executes 1 action đã passed như 1 parameter. Action để thực thi có thể là xây
     * dựng constructor 1 đối tượng hoặc gọi 1 method
     *
     * @param action action sẽ execute.
     * @param values tham số thực tế
     */
    private void execute(String action, String[] values) {

        if (action.indexOf("=") != -1) {
            executeObjectConstruction(action, values);
        } else {
            executeMethodInvocation(action, values);
        }

    }

    /**
     * Xác định Constuctor tương thích với parameter
     *
     * @param cl     Lớp chứa phương thức.
     * @param params Các kiểu tham số thực tế.
     * @return Constructor tương thích (null nếu không tồn tại).
     */
    private Constructor getConstructor(Class cl, Class[] params) {
        Constructor constr = null;
        Constructor[] classConstructors = cl.getConstructors();
        for (int i = 0; i < classConstructors.length; i++) {
            constr = classConstructors[i];
            Class[] formalParams = constr.getParameterTypes();
            if (formalParams.length != params.length)
                continue;
            boolean paramsAreCompatible = true;
            for (int j = 0; j < formalParams.length; j++)
                if (params[j] == null || !formalParams[j].isAssignableFrom(params[j]))
                    paramsAreCompatible = false;
            if (paramsAreCompatible)
                return constr;
        }
        return null;
    }

    /**
     * Xây dựng 1 đối tượng mà method trả về
     *
     * @param action constructor sẽ thực hiện
     * @param values tham số của constructor
     */
    private void executeObjectConstruction(String action, String[] values) {
        String className = "";
        try {
            String lhs = action.substring(action.indexOf("$x") + 2, action.indexOf("="));
            int i = Integer.parseInt(lhs);
            if (action.indexOf("#") != -1) {
                className = action.substring(action.indexOf("=") + 1, action.indexOf("#"));
                objects[i] = null;
                classes[i] = Class.forName(className);
                return;
            }
            className = action.substring(action.indexOf("=") + 1, action.indexOf("("));
            String[] paramNames = action.substring(action.indexOf("(") + 1, action.indexOf(")")).split(",");
            if (paramNames.length == 1 && paramNames[0].equals(""))
                paramNames = new String[0];
            Class[] params = new Class[paramNames.length];
            for (int j = 0; j < paramNames.length; j++)
                params[j] = mapTypeToClass(paramNames[j]);
            Class cl = Class.forName(className);
            Constructor constr = getConstructor(cl, params);
            Object[] actualParams = new Object[params.length];

            for (int j = 0; j < actualParams.length; j++)
                actualParams[j] = mapValueToObject(values[j]);
            if (constr != null) {
                objects[i] = constr.newInstance(actualParams);
                classes[i] = objects[i].getClass();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found. " + e);
            System.exit(1);
        } catch (SecurityException e) {
            System.err.println("Class security violation: " + className + ".");
            System.exit(1);
        } catch (InstantiationException e) {
            System.err.println("Instantiation error: " + className + ".");
            System.exit(1);
        } catch (IllegalAccessException e) {
            System.err.println("Illegal access error: " + className + ".");
            System.exit(1);
        } catch (InvocationTargetException e) {
            return;
        }
    }

    /**
     * HuanNt add method
     * **/


    /**
     * Xác định method tương thích với parameter
     *
     * @param cl         class chứa method
     * @param methodName tên method
     * @param params     kiểu tham số thực tế
     * @return Method tương thích (null nếu không tồn tại).
     */
    private Method getMethod(Class cl, String methodName, Class[] params) {

        Method method = null;
        Method[] classMethods = cl.getMethods();
        for (int i = 0; i < classMethods.length; i++) {
            method = classMethods[i];
            if (!method.getName().equals(methodName))
                continue;
            Class[] formalParams = method.getParameterTypes();
            if (formalParams.length != params.length)
                continue;
            boolean paramsAreCompatible = true;
            for (int j = 0; j < formalParams.length; j++) {

                if (params[j] == null || !formalParams[j].isAssignableFrom(params[j]))
                    paramsAreCompatible = false;

            }
            if (paramsAreCompatible)
                return method;
        }
        return null;

    }

    /**
     * Invoke method theo yêu cầu của action
     *
     * @param action The method invocation action được thực hiện
     * @param values tham số thực tế cho invocation.
     */
    private void executeMethodInvocation(String action, String[] values) {
        try {
            String targetName = action.substring(action.indexOf("$x") + 2, action.indexOf("."));
            String methodName = action.substring(action.indexOf(".") + 1, action.indexOf("("));
            String[] paramNames = action.substring(action.indexOf("(") + 1, action.indexOf(")")).split(",");
            if (paramNames.length == 1 && paramNames[0].equals(""))
                paramNames = new String[0];
            Class[] params = new Class[paramNames.length];

            Object obj = objects[Integer.parseInt(targetName)];
            if (obj == null)
                return;
            Class cl = obj.getClass();
            for (int i = 0; i < paramNames.length; i++) {
                params[i] = mapTypeToClass(paramNames[i]);
            }
            Method method = getMethod(cl, methodName, params);

            Object[] actualParams = new Object[params.length];
            Object[] actualOneArrayParams = null;
            int indexOfArray = 1;
            for (int j = 0; j < params.length; j++) {
                // set params for type is an object, an primitive type
                if (!values[j].contains(" ")) {
                    indexOfArray++;
                    actualParams[j] = mapValueToObject(values[j]);
                }
                // set params for type is an array defined objects
                else if (values[j].contains("[]{")) {
                    String variableArray = values[j].substring(values[j].indexOf("{$"));
                    variableArray = variableArray.substring(1, variableArray.length() - 1);
                    String[] variables = variableArray.split(" ");
                    GetArray getArray = new GetArray();
                    actualOneArrayParams = getArray.newArray(this.classes[indexOfArray], variables.length);
                    for (int i = 0; i < actualOneArrayParams.length; i++) {
                        actualOneArrayParams[i] = this.objects[indexOfArray];
                        indexOfArray++;
                    }
                    actualParams[j] = actualOneArrayParams;
                }
                // set params for type is a primitive type
                else {
                    String valuesTypeArray[] = values[j].split(" ");
                    actualOneArrayParams = new Object[valuesTypeArray.length];
                    for(int x=0;x<valuesTypeArray.length;x++){
                        actualOneArrayParams[x]=mapValueToObject(valuesTypeArray[x]);
                    }
                    actualParams[j] = actualOneArrayParams;

                    ConvertFactory convertFactory = ConvertFactory.getInstance();
                    if (paramNames[j].equals("boolean[]")) {
                        actualParams[j] = convertFactory.convertToBooleanArray(actualOneArrayParams);
                    } else if (paramNames[j].equals("char[]")) {
                        actualParams[j] = convertFactory.convertToCharArray(actualOneArrayParams);
                    } else if (paramNames[j].equals("byte[]")) {
                        actualParams[j] = convertFactory.convertToByteArray(actualOneArrayParams);
                    } else if (paramNames[j].equals("short[]")) {
                        actualParams[j] = convertFactory.convertToShortArray(actualOneArrayParams);
                    } else if (paramNames[j].equals("int[]")) {
                        actualParams[j] = convertFactory.convertToIntArray(actualOneArrayParams);
                    } else if (paramNames[j].equals("long[]")) {
                        actualParams[j] = convertFactory.convertToLongArray(actualOneArrayParams);
                    } else if (paramNames[j].equals("float[]")) {
                        actualParams[j] = convertFactory.convertToFloatArray(actualOneArrayParams);
                    } else if (paramNames[j].equals("double[]")) {
                        actualParams[j] = convertFactory.convertToDoubleArray(actualOneArrayParams);
                    }

                    //objects[j] = actualOneArrayParams;
                }
            }
            if (method != null) {
                method.invoke(obj, actualParams);
            }
        } catch (SecurityException e) {
            System.err.println("Class security violation.");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * reset lại chỉ số biến Example: "$x21=A():$x22.m($x21)" thành
     * "$x0=A():$x1.m($x0)".
     */
    private String renameChromsomeVariables(String chrom) {
        String inputDescription = chrom.substring(0, chrom.indexOf("@"));
        String[] actions = inputDescription.split(":");
        int n = 0;
        Map mapIndex = new HashMap();
        for (int i = 0; i < actions.length; i++)
            if (actions[i].indexOf("=") != -1) {
                String targetObj = actions[i].substring(2, actions[i].indexOf("="));
                int k = Integer.parseInt(targetObj);
                mapIndex.put(new Integer(k), new Integer(n++));
            }
        Iterator i = mapIndex.keySet().iterator();
        while (i.hasNext()) {
            Integer x = (Integer) i.next();
            int k = x.intValue();
            Integer y = (Integer) mapIndex.get(x);
            int j = y.intValue();
            if (k == j)
                continue;
            Pattern p = Pattern.compile("(.*)\\$x" + k + "([\\.=,\\,\\ ,\\ })].*)");
            Matcher m = p.matcher(chrom);
            while (m.find()) {
                chrom = m.group(1) + "$y" + j + m.group(2);
                m = p.matcher(chrom);
            }
        }
        chrom = chrom.replaceAll("\\$y", "\\$x");
//        System.out.println(chrom);
        return chrom;
    }

    /**
     * Thực hiện testcase được mã hóa bởi chromosome Cá thể được chia thành 2 phần
     * description and value. Mỗi action được mô tả đầu vào, sau đó thực hiện
     *
     * @param classUnderTest Class đang được thực tiện test
     * @param chrom          chromosome sẽ test
     */
    public void execute(String classUnderTest, String chrom) {

        Method setUpExec = null;
        Method tearDownExec = null;
        try {
            Class cl = Class.forName(classUnderTest);
            setUpExec = cl.getDeclaredMethod("setUpExec", new Class[0]);
            if (setUpExec != null)
                setUpExec.invoke(null, new Object[0]);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        testCaseExecutions++;
        chromosome = renameChromsomeVariables(chrom);
//        chromosome = chrom;
        String inputDescription = chromosome.substring(0, chromosome.indexOf("@"));
        String inputValues = chromosome.substring(chromosome.indexOf("@") + 1);
        String[] actions = inputDescription.split(":");
        String[] values = inputValues.split(",");
        int n = -1;
        for (int i = 0; i < actions.length; i++)
            if (actions[i].indexOf("=") != -1) {
                String targetObj = actions[i].substring(0, actions[i].indexOf("="));
                int k = Integer.parseInt(targetObj.substring(2));
                if (k > n)
                    n = k;
            }
        objects = new Object[n + 1];
        classes = new Class[n + 1];
        resetExecutionTrace(classUnderTest);
        int k = 0;
        for (int i = 0; i < actions.length; i++) {
            String action = actions[i];
            String[] params = new String[0];
            if (action.indexOf("(") != -1)
                params = action.substring(action.indexOf("(") + 1, action.indexOf(")")).split(",");
            if (params.length == 1 && params[0].equals(""))
                params = new String[0];
            String[] actionValues = new String[params.length];
            for (int j = 0; j < params.length; j++) {
                if (ChromosomeFormer.isPrimitiveType(params[j]))
                    actionValues[j] = values[k++];
                else
                    actionValues[j] = params[j];
            }
            execute(action, actionValues);
        }
        try {
            Class cl = Class.forName(classUnderTest);
            tearDownExec = cl.getDeclaredMethod("tearDownExec", new Class[0]);
            if (tearDownExec != null)
                tearDownExec.invoke(null, new Object[0]);
        } catch (Exception e) {
//            e.printStackTrace();
        }

    }

    /**
     * Yêu cầu theo dõi trace cho lớp đang test
     *
     * @param classUnderTest class đang được test
     * @return trace: Set<Integer>
     * @throws IOException
     */
    public Collection getExecutionTrace(String classUnderTest) throws IOException {
        try {
            // getTrace của classUnderTest (class chính)
            Class cl = Class.forName(classUnderTest);
            Method getTrace = cl.getDeclaredMethod("getTrace", new Class[0]);
            Collection trace = (Collection) getTrace.invoke(null, new Object[0]);
            System.out.println(trace);

            List extendTarget = new LinkedList<>();

            // getTrace của các class còn lại
            for (String s : MainGA.inputs) {
                if (s.equals(classUnderTest) == false) {

                    Class subCl = Class.forName(s);
                    Method subGetTrace = subCl.getDeclaredMethod("getTrace", new Class[0]);
                    Collection subTrace = (Collection) subGetTrace.invoke(null, new Object[0]);
                    Iterator x = subTrace.iterator();
                    while (x.hasNext()) {
                        trace.add(x.next());
                    }
                    if (subTrace.size() != 0) {
                        if (Population.getExtendTarget().size() == 0) {
                            List<String> extendTargetString = MainGA.generateExtendTarget(s, subTrace);

                            Set curTarget;
                            if (Population.preTarget == null)
                                curTarget = Population.curTarget;
                            else
                                curTarget = Population.preTarget;
                            for (int i = 0; i < extendTargetString.size(); i++) {
                                Set newTarget = new HashSet();
                                newTarget.addAll(curTarget);
                                if (extendTargetString.get(i) != null) {
                                    String temp = extendTargetString.get(i).replace("[", "").replace("]", "");
                                    if (temp.contains(",")) {

                                        String[] listNode = temp.split(", ");
                                        for (String node : listNode) {
                                            BranchTarget branch = new BranchTarget(Integer.parseInt(node));
                                            newTarget.add(branch);
                                        }
                                        extendTarget.add(newTarget);

                                    } else {
                                        BranchTarget branch = new BranchTarget(Integer.parseInt(temp));
                                        newTarget.add(branch);
                                        extendTarget.add(newTarget);
                                    }
                                }
                            }
                            Population.setExtendTarget(extendTarget);

                        }

                    }
                }

            }
            // add coveredBranches
            Collection coveredBranches;
            coveredBranches = new HashSet();
            Iterator j = trace.iterator();
            while (j.hasNext()) {
                BranchTarget branch = new BranchTarget(((Integer) j.next()).intValue());
                coveredBranches.add(branch);
            }

            return coveredBranches;
        } catch (NoSuchMethodException e) {
            System.err.println("Method not found. " + e);
            System.exit(1);
        } catch (IllegalAccessException e) {
            System.err.println("Illegal access error.");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found. " + e);
            System.exit(1);
        } catch (InvocationTargetException e) {
            System.err.println("Invocation target error: " + classUnderTest + ".");
            System.exit(1);
        }
        return null;
    }


    /**
     * Resets laị trace thực hiện của lớp đang test
     *
     * @param classUnderTest class đang được test
     */
    public void resetExecutionTrace(String classUnderTest) {
        try {
            Class cl = Class.forName(classUnderTest);
            Method newTrace = cl.getDeclaredMethod("newTrace", new Class[0]);
            newTrace.invoke(null, new Object[0]);

            for (String s : MainGA.inputs) {
                if (s.equals(classUnderTest) == false) {
                    Class subCl = Class.forName(s);
                    Method subNewTrace = subCl.getDeclaredMethod("newTrace", new Class[0]);
                    subNewTrace.invoke(null, new Object[0]);
                }
            }
        } catch (NoSuchMethodException e) {
            System.err.println("Method not found. " + e);
            System.exit(1);
        } catch (IllegalAccessException e) {
            System.err.println("Illegal access error.");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found. " + e);
            System.exit(1);
        } catch (InvocationTargetException e) {
            System.err.println("Invocation target error: " + classUnderTest + ".");
            System.exit(1);
        }
    }

//	/**
//	 * Tính fitness function
//	 * 
//	 * @param side1
//	 * @param side2
//	 * @param side3
//	 */
//	public double calcBD(Set branchTarget, double side1, double side2, double side3) {
//		double distanceBranch = 0;
//		int branchTargetNo = 0;
//		if (branchTarget.toString().equals("[2, 3, 5, 7]"))
//			branchTargetNo = 1;
//		else if (branchTarget.toString().equals("[2, 3, 5, 6]"))
//			branchTargetNo = 2;
//		else if (branchTarget.toString().equals("[2, 3, 4]"))
//			branchTargetNo = 3;
//		else if (branchTarget.toString().equals("[2, 8]"))
//			branchTargetNo = 4;
//		switch (branchTargetNo) {
//		case 1: // branch #1: (A==B)&&(B==C)
//			double[] term3 = new double[2];
//			term3[0] = distanceKorel("==", side1, side2);
//			term3[1] = distanceKorel("==", side2, side3);
//
//			distanceBranch = distanceKorel("&&", term3[0], term3[1]);
//			break;
//		case 2: // branch #2: ((A == B) && (B != C)) || ((B == C) && (A != C)) || ((C == A) &&
//				// (B != A))
//			double[] subTerm = new double[6];
//			subTerm[0] = distanceKorel("==", side1, side2);
//			subTerm[1] = distanceKorel("!=", side2, side3);
//			subTerm[2] = distanceKorel("==", side3, side2);
//			subTerm[3] = distanceKorel("!=", side1, side3);
//			subTerm[4] = distanceKorel("==", side1, side3);
//			subTerm[5] = distanceKorel("!=", side1, side2);
//
//			double[] term = new double[3];
//			term[0] = distanceKorel("&&", subTerm[0], subTerm[1]);
//			term[1] = distanceKorel("&&", subTerm[2], subTerm[3]);
//			term[2] = distanceKorel("&&", subTerm[4], subTerm[5]);
//
//			distanceBranch = Math.min(term[0], Math.min(term[1], term[2]));
//			break;
//		case 3: // branch #3: (A!=B)&&(A!=C)&&(B!=C)
//			double[] term1 = new double[3];
//			term1[0] = distanceKorel("!=", side1, side2);
//			term1[1] = distanceKorel("!=", side2, side3);
//			term1[2] = distanceKorel("!=", side1, side3);
//			distanceBranch = term1[0] + term1[1] + term1[2];
//			break;
//		case 4: // branch #4: (A+B>C)&&(B+C>A)&&(A+C>B)
//			double[] term2 = new double[3];
//			term2[0] = distanceKorel("<=", side1 + side2, side3);
//			term2[1] = distanceKorel("<=", side2 + side3, side1);
//			term2[2] = distanceKorel("<=", side1 + side3, side2);
//
//			distanceBranch = Math.min(term2[0], Math.min(term2[1], term2[2]));
//			break;
//		}
//		// if (distanceBranch == 0)
//		// return 1;
//		// else
//		return (1 - 1 / distanceBranch);
//	}
//
//	/**
//	 * Bonus: Tính Branch Distance theo CT Korel
//	 * 
//	 * @param operator:
//	 *            biểu thức toán học
//	 * @param operator1
//	 * @param operator2
//	 * @return
//	 */
//	private double distanceKorel(String operator, double operand1, double operand2) {
//		double k = 1;
//		double distance = 0;
//		switch (operator) {
//		case "==":
//			if (Math.abs(operand1 - operand2) == 0)
//				distance = 0;
//			else
//				distance = Math.abs(operand1 - operand2) + k;
//			break;
//		case "!=":
//			if (Math.abs(operand1 - operand2) != 0)
//				distance = 0;
//			else
//				distance = k;
//			break;
//		case "<":
//			if (operand1 - operand2 < 0)
//				distance = 0;
//			else
//				distance = Math.abs(operand1 - operand2) + k;
//			break;
//		case "<=":
//			if (operand1 - operand2 <= 0)
//				distance = 0;
//			else
//				distance = Math.abs(operand1 - operand2) + k;
//			break;
//		case ">":
//			if (operand2 - operand1 > 0)
//				distance = 0;
//			else
//				distance = Math.abs(operand2 - operand1) + k;
//			break;
//		case ">=":
//			if (operand1 - operand2 >= 0)
//				distance = 0;
//			else
//				distance = Math.abs(operand2 - operand1) + k;
//			break;
//		case "||":
//			distance = Math.min(operand1, operand2);
//			break;
//		case "&&":
//			distance = operand1 + operand2;
//			break;
//		}
//
//		return distance;
//	}


}
