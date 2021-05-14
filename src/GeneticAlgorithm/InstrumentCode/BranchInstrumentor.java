package GeneticAlgorithm.InstrumentCode;

import openjava.mop.*;
import openjava.ptree.*;

/**
 * Thực hiện phân tích code để tạo thành các path
 */
public class BranchInstrumentor extends openjava.mop.OJClass {

	public static int branchCounter = 0;

	java.lang.String className;

	java.io.PrintStream signatureFile;

	java.io.PrintStream targetFile;

	java.io.PrintStream pathFile;

	boolean isFirstTarget = true;

	static java.lang.String traceInterfaceType = "java.util.Set";

	static java.lang.String traceConcreteType = "java.util.HashSet";

	public java.lang.String getClassName() {
		return className;
	}

	/**
	 * Inserts import statements (java.util.*) vào đầu file .java
	 */
	public void insertImports() {
		try {
			openjava.ptree.ParseTreeObject pt = getSourceCode();
			while (!(pt instanceof openjava.ptree.CompilationUnit)) {
				pt = pt.getParent();
			}
			openjava.ptree.CompilationUnit cu = (openjava.ptree.CompilationUnit) pt;

			java.lang.String[] oldImports = cu.getDeclaredImports();
			java.lang.String[] newImports = new java.lang.String[oldImports.length + 2];
			System.arraycopy(oldImports, 0, newImports, 0, oldImports.length);
			newImports[oldImports.length] = "java.util.*;";
			newImports[oldImports.length + 1] = "GeneticAlgorithm.*;";
			cu.setDeclaredImports(newImports);
		} catch (openjava.mop.CannotAlterException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Adds field trace vào class đang được instrument
	 */
	public void insertTraceField() {
		try {
			openjava.mop.OJModifier mod = OJModifier.forModifier(OJModifier.STATIC);
			openjava.ptree.FieldDeclaration fd = new openjava.ptree.FieldDeclaration(
					new openjava.ptree.ModifierList(ModifierList.STATIC),
					TypeName.forOJClass(OJClass.forName(traceInterfaceType)), "trace",
					new openjava.ptree.AllocationExpression(OJClass.forName(traceConcreteType),
							new openjava.ptree.ExpressionList()));
			openjava.mop.OJField f = new openjava.mop.OJField(getEnvironment(), this, fd);
			addField(f);
		} catch (openjava.mop.OJClassNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (openjava.mop.CannotAlterException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Adds method getTrace vào class đang được instrument
	 */
	public void insertTraceAccessor() {
		try {
			openjava.ptree.StatementList body = makeStatementList("return trace;");
			openjava.mop.OJModifier mod = OJModifier.forModifier(OJModifier.PUBLIC);
			mod = mod.add(OJModifier.STATIC);
			openjava.mop.OJMethod m = new openjava.mop.OJMethod(this, mod, OJClass.forName(traceInterfaceType),
					"getTrace", new openjava.mop.OJClass[0], new openjava.mop.OJClass[0], body);
			addMethod(m);
		} catch (openjava.mop.OJClassNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (openjava.mop.CannotAlterException e) {
			System.err.println(e);
			System.exit(1);
		} catch (openjava.mop.MOPException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Adds method newTrace vào class đang được instrument
	 */
	public void insertTraceCreator() {
		try {
			openjava.ptree.StatementList body = makeStatementList("trace = new " + traceConcreteType + "();");
			openjava.mop.OJModifier mod = OJModifier.forModifier(OJModifier.PUBLIC);
			mod = mod.add(OJModifier.STATIC);
			openjava.mop.OJMethod m = new openjava.mop.OJMethod(this, mod, OJClass.forName("void"), "newTrace",
					new openjava.mop.OJClass[0], new openjava.mop.OJClass[0], body);
			addMethod(m);
		} catch (openjava.mop.OJClassNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (openjava.mop.CannotAlterException e) {
			System.err.println(e);
			System.exit(1);
		} catch (openjava.mop.MOPException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Tạo statement add branch id (Integer) vào trace.
	 */
	public openjava.ptree.Statement makeTraceStatement() {
		openjava.ptree.Statement traceBranch = null;
		try {
			branchCounter++;
			traceBranch = makeStatement("trace.add(new java.lang.Integer(" + branchCounter + "));");
			printTarget(branchCounter);
			printPath(branchCounter);
		} catch (openjava.mop.MOPException e) {
			System.err.println(e);
			System.exit(1);
		}
		return traceBranch;
	}

	/**
	 * Thăm cây phân tích của method
	 */
	public void insertBranchTraces(openjava.ptree.StatementList block) {
		try {
			block.accept(new BranchTraceVisitor(this));
		} catch (openjava.ptree.ParseTreeException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * In các path vào path file (.path)
	 */
	private void printPath(int tgt) {
		pathFile.print(tgt + ":");
		java.util.Iterator controlDep = BranchTraceVisitor.getControlDependences().iterator();
		while (controlDep.hasNext()) {
			java.lang.Integer br = (java.lang.Integer) controlDep.next();
			pathFile.print(" " + br);
		}
		pathFile.println();
	}

	/**
	 * In từng mục tiêu vào target file (.tgt)
	 */
	private void printTarget(int tgt) {
		if (isFirstTarget) {
			targetFile.print(": " + tgt);
			isFirstTarget = false;
		} else {
			targetFile.print(", " + tgt);
		}
	}

	/**
	 * In tất cả method name vào target file (.tgt)
	 */
	private void printTargetMethod(openjava.mop.OJMember mem) {
		isFirstTarget = true;
		if (mem.getModifiers().isPrivate() || mem.getModifiers().isProtected()) {
			return;
		}
		targetFile.print(getSignature(mem));
	}

	/**
	 * Dừng in các target cho các method
	 */
	private void printTargetEnd() {
		targetFile.println();
	}

	/**
	 * Return fullname của method hoặc constructor.
	 */
	private java.lang.String getSignature(openjava.mop.OJMember mem) {
		java.lang.String clName = mem.getDeclaringClass().toString();
		java.lang.String signature = clName;
		signature += "." + mem.signature().toString();
		signature = signature.replaceAll("\\$", "\\\\\\$");
		clName = clName.replaceAll("\\$", "\\\\\\$");
		signature = signature.replaceFirst("\\.constructor\\s+", "." + clName);
		signature = signature.replaceFirst("\\.method\\s+", ".");
		signature = signature.replaceAll("class\\s+", "");
		signature = signature.replaceAll("\\\\\\$", "\\$");

		return signature;
	}

	/**
	 * In tất cả method/constructor name vào signature file (.sign)
	 */
	private void printSignature(openjava.mop.OJMember mem) {
		if (mem.getModifiers().isPrivate() || mem.getModifiers().isProtected()) {
			return;
		}
		signatureFile.println(getSignature(mem));
	}

	/**
	 * Opens sigature, target và path files
	 */
	private void openOutputFiles() {
		try {
			signatureFile = new java.io.PrintStream(
					new java.io.FileOutputStream(InstrumentorConfiguration.relativePath + className + ".sign"));
			targetFile = new java.io.PrintStream(
					new java.io.FileOutputStream(InstrumentorConfiguration.relativePath + className + ".tgt"));
			pathFile = new java.io.PrintStream(new java.io.FileOutputStream(InstrumentorConfiguration.relativePath + className + ".path"));

		} catch (java.io.FileNotFoundException e) {
			System.err.println("File not found: " + e);
			System.exit(1);
		}
	}

	/**
	 * Ghi đè lên class của file java( add instrumentation)
	 */
	public void translateDefinition() throws openjava.mop.MOPException {
		if (className == null) {
			className = getSimpleName();
		}
		openOutputFiles();
		insertTraceField();
		openjava.mop.OJConstructor[] constructors = getDeclaredConstructors();
		for (int i = 0; i < constructors.length; ++i) {
			printSignature(constructors[i]);
			printTargetMethod(constructors[i]);
			insertBranchTraces(constructors[i].getBody());
			printTargetEnd();
		}
		openjava.mop.OJMethod[] methods = getDeclaredMethods();
		for (int i = 0; i < methods.length; ++i) {
			printSignature(methods[i]);
			printTargetMethod(methods[i]);
			insertBranchTraces(methods[i].getBody());
			printTargetEnd();
		}
		insertTraceCreator();
		insertTraceAccessor();
	}

	/**
	 * Generates a metaobject from source code
	 * 
	 * @param oj_param0
	 * @param oj_param1
	 * @param oj_param2
	 */
	public BranchInstrumentor(openjava.mop.Environment oj_param0, openjava.mop.OJClass oj_param1,
			openjava.ptree.ClassDeclaration oj_param2) {
		super(oj_param0, oj_param1, oj_param2);
	}

	/**
	 * Generates a metaobject from byte code
	 * 
	 * @param oj_param0
	 * @param oj_param1
	 */

	public BranchInstrumentor(java.lang.Class oj_param0, openjava.mop.MetaInfo oj_param1) {

		super(oj_param0, oj_param1);
	}




}
