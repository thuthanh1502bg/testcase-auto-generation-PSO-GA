/*
 * Copyright(C) 2019 NgocAnh
 *
 * Chromosome.java, Sep 26, 2019 NgocAnh
 */
package GeneticAlgorithm.GA;

import java.util.*;

/**
 * Biểu diễn chromosome. Chromosome mã hóa thông tin của testcase
 * <p>
 * Examples:
 * <p>
 * $x0=A(int[0;1]):$x1=B#null:$x0.m(int,$x1)@1,88
 * $x0=A():$x1=B(int[-2;2]):$x1.g1():$x0.m(int,$x1)@-1,42
 */
public class Chromosome implements Comparable, Cloneable {
    /**
     * Testcase : List hàm hoặc constructor
     */
    private List<Statement> statements = new LinkedList();
    
    private List<String> actualValues = new ArrayList<String>();

    /**
     * Branch targets được bao phủ
     */
    private Collection coveredPath;

    /**
     * Đường dẫn được bao phủ bởi TestCaseExecutor.
     */
    public void setCoveredPath(Set pathPoints) {
    	this.coveredPath = pathPoints;
    }
    
    public Collection getCoveredPath() {
    	return this.coveredPath;
    }

    /**
     * Branch targets được bao phủ ( chỉ xét trong method hiện tại)
     */
    private Collection coveredTarget;

    /**
     * Target được thỏa mãn bởi TestCaseExecutor.
     */
    public void setCoveredTarget(Set pathPoints) {
        this.coveredTarget = pathPoints;
    }
    
    public Collection getCoveredTarget() {
    	return this.coveredTarget;
    }
    
    public void setStatements(List<Statement> statements) {
    	this.statements = statements;
    }
    
    public List<Statement> getStatements() {
    	return this.statements;
    }

    /**
     * Số branch được bao phủ đến hiện tại bởi chromosome
     */
    private int fitness = 0;
    
    public void setFitness(int fitness) {
    	this.fitness = fitness;
    }
    
    public int getFitness() {
    	return this.fitness;
    }

    /**
     * kết quả trả về của method
     */
    private String expectResult;
    
    public void setExpectResult(String expectResult) {
    	this.expectResult = expectResult;
    }
    
    public String getExpectResult() {
    	return this.expectResult;
    }

    /**
     * Implements chromosome duplication.
     */
    public Object clone() {
        List acts = new LinkedList();
        Iterator it = this.statements.iterator();
        while (it.hasNext()) {
            Statement act = (Statement) it.next();
            acts.add(act.clone());
        }
        return new Chromosome(acts);
    }

    /**
     * Sắp xếp chromosome dựa trên độ giảm giá trị fitness
     */
    public int compareTo(Object o) {
        Chromosome other = (Chromosome) o;
        if (other.getFitness() == this.fitness)
        	return 0;
        else if (other.getFitness() > this.fitness)
        	return 1;
        else 
        	return -1;
        //return other.fitness - fitness;
    }

    /**
     * Equality of chromosomes is based on fitness.
     */
    public boolean equals(Object o) {
        Chromosome ind = (Chromosome) o;
        //should update in the case the fitness is not integer anymore
        return fitness == ind.getFitness();
    }

    /**
     * số lượng action
     */
    public int size() {
        return statements.size();
    }

    /**
     * Gets ConstructorInvocation
     *
     * @param objId object Target của constructor.
     * @return ConstructorInvocation object của objId .
     */
    private Statement getConstructor(String objId) {
        Iterator i = statements.iterator();
        while (i.hasNext()) {
            Statement act = (Statement) i.next();
            if (objId.equals(act.getTargetObject()))
                return act;
        }
        return null;
    }

    /**
     * xậy dựng chromosome từ các action
     */
    public Chromosome(List statements) {
        this.statements = statements;
        //after constructing a chromosome from a list of statements, initializing the list of actual values (after @) for this chromosome
        this.initializeActualValuesList();
    }

    /**
     * Builds chromosome.
     */
    public Chromosome() {
    	
    }

    /**
     * biểu diễn chromosome
     * <p>
     * Example:
     *
     * <pre>
     * $x0=A():$x1=B(int):$x1.c():$x0.m(int, $x1) @ 1, 4
     * each statement is separated by a :
     * each actual value is separated by a ,
     * </pre>
     */
    public String toString() {
        String s = "";
        try {
            Iterator i = statements.iterator();
            while (i.hasNext()) {
                Statement act = (Statement) i.next();
                if (s.equals("")) {
                    s = act.actionDescription();
                } else {
                    s += ":" + act.actionDescription();
                }
            }
            s += "@";
            i = statements.iterator();
            while (i.hasNext()) {
                Statement act = (Statement) i.next();
                String actVals = act.actualValuesToString();
                if (!actVals.equals("")) {
                    if (s.endsWith("@")) {
                        s += actVals;
                    } else {
                        s += "," + actVals;
                    }
                }
            }
        } catch (Exception ex) {

        }
        return s;
    }

    /**
     * Lấy ra list các value của chromosome
     *
     * @return actualParams List các value
     */
    public void initializeActualValuesList() {
        actualValues = new ArrayList<String>();
        Iterator i = statements.iterator();
        while (i.hasNext()) {
//            Statement stm = (Statement) i.next();
//            String actVals = stm.actualValuesToString();
//            if (!actVals.equals("")) {
//            	String[] actValueStringArray = actVals.split(",");
//                for (String element : actValueStringArray) {
//                	actualValues.add(element);
//                }
//            }
        	Statement stm = (Statement) i.next();
        	List<String> actualValueOfStatement = stm.getActualValues();
        	if (actualValueOfStatement != null) 
        		actualValues.addAll(actualValueOfStatement);
        }
    }

    /**
     * Lấy ra list các value của chromosome
     *
     * @return actualParams List các value
     */
    public String[] getArrayOfActualValues() {
        //make sure that actualValues of the chromosome has been invoked before
        return actualValues.toArray(new String[0]);
    } 

    /**
     * Set giá trị value cho action
     *
     * @param newValue list giá trị mới
     */
    public void setInputValue(List newValue) {
        int index = 0;
        Iterator i = statements.iterator();
        while (i.hasNext()) {
//			int count=0;
            Statement act = (Statement) i.next();
            List actualParamValues = act.getParameterValues();
            List parameterValues = new ArrayList();
            for (int j = 0; j < actualParamValues.size(); j++) {
                Object element = actualParamValues.get(j);
                if (!element.toString().contains("$")) {
                	parameterValues.add(newValue.get(index));
                    index++;
                } else {
                	parameterValues.add(element);
                }
            }
            if (parameterValues.size() > 0) {
                act.setParameterValues(parameterValues);
            }
//			act.setParameterValuesMethod(newValue);
        }
    }

    /**
     * java code representation of Chromosome.
     * <p>
     * Example:
     *
     * <pre>
     * $x0=A():$x1=B(int):$x1.c():$x0.m(int, $x1) @ 1, 4
     * </pre>
     * <p>
     * becomes:
     *
     * <pre>
     * A x0 = new A();
     * B x1 = B(1);
     * x1.c();
     * x0.m(4, x1) @ 1, 4
     * </pre>
     */
    public String toCode() {
        String s = "";
        Iterator i = statements.iterator();
        while (i.hasNext()) {
            Statement act = (Statement) i.next();
            act.setExpectResult(this.expectResult);
            s += act.toCode() + "\n";
        }
        return s;
    }

    /**
     * Xác định biến $xN được gán cho obj của 1 class từ class đã biết
     * <p>
     * Scan chromosome cho tới khi gặp đối tượng của 1 class. left hand side
     * variable được trả về
     *
     * @param className class của đối tượng tìm kiếm
     * @return String đại diện của biến đối tượng tìm kiếm (hoặc null)
     */
    public String getObjectId(String className) {
        if (className.indexOf("[") != -1)
            className = className.substring(0, className.indexOf("["));
        Iterator i = statements.iterator();
        while (i.hasNext()) {
            Statement a = (Statement) i.next();
            if (className.equals(a.getName()))
                return a.getTargetObject();
        }
        return null;
    }

    /**
     * Xác định biến $xN được gán cho obj của 1 class từ list class đã biết
     *
     * @param classes Danh sách các lớp đối tượng có thể thuộc về
     * @return String đại diện của biến đối tượng tìm kiếm (hoặc null)
     */
    public String getObjectId(List classes) {
        Iterator i = classes.iterator();
        while (i.hasNext()) {
            String cl = (String) i.next();
            String objId = getObjectId(cl);
            if (objId != null)
                return objId;
        }
        return null;
    }

    /**
     * Adds action để mô tả input
     *
     * @param act Action được add
     */
    public void addStatement(Statement act) {
        statements.add(act);
        List<String> actualValuesOfAStatement = act.getActualValues();
        if (actualValuesOfAStatement != null)
        	actualValues.addAll(actualValuesOfAStatement);
    }

    /**
     * Ghép 2 chrom với nhau
     * <p>
     * Example: $x0=A(int)@10 $x1.m($x0,int)@21
     * <p>
     * $x0=A(int):$x1.m($x0,int)@10,21
     *
     * @param chrom Chromosome sau khi được ghép
     */
    public void appendChromosome(Chromosome chrom) {
        statements.addAll(chrom.getStatements());
        Iterator i = chrom.getStatements().iterator();
        while (i.hasNext()) {
        	Statement stm = (Statement) i.next();
        	List<String> actualValueOfStatement = stm.getActualValues();
        	if (actualValueOfStatement != null) 
        		actualValues.addAll(actualValueOfStatement);
        }
    }

    /**
     * Mutation operator: thay đổi ngẫu nhiên 1 trong các value
     */
    public void mutation() {
        int valNum = 0;
        Iterator i = statements.iterator();
        while (i.hasNext()) {
            Statement act = (Statement) i.next();
            valNum += act.countPrimitiveTypes();
        }
        if (valNum == 0)
            return;
        int inputIndex = ChromosomeFormer.randomGenerator.nextInt(valNum);
        int k = 0;
        i = statements.iterator();
        while (i.hasNext()) {
            Statement act = (Statement) i.next();
            int actValNum = act.countPrimitiveTypes();
            if (k <= inputIndex && k + actValNum > inputIndex) {
                act.changeInputValue(inputIndex - k);
                break;
            }

            k += actValNum;
        }


    }

}