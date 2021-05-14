package GeneticAlgorithm.InstrumentCode;

import openjava.ptree.*;
import openjava.ptree.util.*;

/**
 * Thêm các trace vào các nhánh của chương trình Mỗi trace được thêm vào mỗi
 * nhánh của if, while do, switch, các câu lệnh bằng cách sử dụng công cụ phân
 * tích khối lệnh
 */
public class BranchTraceVisitor extends ParseTreeVisitor {
	static int branchCounter = 0;
	static java.util.List controlDependences;
	BranchInstrumentor instrumentor;

	/**
	 * Tìm kiếm và in các node cha
	 */
	public BranchTraceVisitor(BranchInstrumentor instr) {
		instrumentor = instr;
		controlDependences = new java.util.LinkedList();
	}

	/**
	 * Returns the control dependences.
	 */
	public static java.util.List getControlDependences() {
		return controlDependences;
	}

	public void visit(AllocationExpression p) {
	}

	public void visit(ArrayAccess p) {
	}

	public void visit(ArrayAllocationExpression p) {
	}

	public void visit(ArrayInitializer p) {
	}

	public void visit(AssignmentExpression p) {
	}

	public void visit(BinaryExpression p) {
	}

	/**
	 * Resorts to visit(StatementList) in order to visit a Block.
	 */
	public void visit(Block p) throws ParseTreeException {
		StatementList stmts = p.getStatements();
		stmts.accept(this);
	}

	public void visit(BreakStatement p) {
	}

	/**
	 * Resorts to visit(StatementList) add trace statement to each case.
	 */
	public void visit(CaseGroup p) throws ParseTreeException {
		java.util.List initCtrlDep = new java.util.LinkedList(controlDependences);
		StatementList stmts = p.getStatements();
		stmts.accept(this);
		controlDependences = initCtrlDep;
	}

	/**
	 * Iterates over cases in switch statements.
	 */
	public void visit(CaseGroupList p) throws ParseTreeException {
		java.util.Enumeration it = p.elements();
		while (it.hasMoreElements()) {
			ParseTree elem = (ParseTree) it.nextElement();
			elem.accept(this);
		}
	}

	public void visit(CaseLabel p) {
	}

	public void visit(CaseLabelList p) {
	}

	public void visit(CastExpression p) {
	}

	/**
	 * Resorts to visit(StatementList) to add trace statement to each catch block.
	 */
	public void visit(CatchBlock p) throws ParseTreeException {
		java.util.List initCtrlDep = new java.util.LinkedList(controlDependences);
		StatementList stmts = p.getBody();
		stmts.accept(this);
		controlDependences = initCtrlDep;
	}

	/**
	 * Iterates over catch blocks.
	 */
	public void visit(CatchList p) throws ParseTreeException {
		java.util.Enumeration it = p.elements();
		while (it.hasMoreElements()) {
			ParseTree elem = (ParseTree) it.nextElement();
			elem.accept(this);
		}
	}

	public void visit(ClassDeclaration p) {
	}

	public void visit(ClassDeclarationList p) {
	}

	public void visit(ClassLiteral p) {
	}

	public void visit(CompilationUnit p) {
	}

	public void visit(ConditionalExpression p) {
	}

	public void visit(ConstructorDeclaration p) {
	}

	public void visit(openjava.ptree.ConstructorInvocation p) {
	}

	public void visit(ContinueStatement p) {
	}

	/**
	 * Resorts to visit(StatementList) add trace statement to do loop.
	 */
	public void visit(DoWhileStatement p) throws ParseTreeException {
		java.util.List initCtrlDep = new java.util.LinkedList(controlDependences);
		StatementList stmts = p.getStatements();
		stmts.accept(this);
		controlDependences = initCtrlDep;
	}

	public void visit(EmptyStatement p) {
	}

	public void visit(ExpressionList p) {
	}

	public void visit(ExpressionStatement p) throws ParseTreeException {
	}

	public void visit(FieldAccess p) {
	}

	public void visit(FieldDeclaration p) {
	}

	/**
	 * Resorts to visit(StatementList) add trace statement to for loop.
	 */
	public void visit(ForStatement p) throws ParseTreeException {
		java.util.List initCtrlDep = new java.util.LinkedList(controlDependences);
		StatementList stmts = p.getStatements();
		stmts.accept(this);
		controlDependences = initCtrlDep;
	}

	/**
	 * Resorts to visit(StatementList) add trace statement to if statement.
	 */
	public void visit(IfStatement p) throws ParseTreeException {
		java.util.List initCtrlDep = new java.util.LinkedList(controlDependences);
		StatementList stmts = p.getStatements();
		stmts.accept(this);
		controlDependences = new java.util.LinkedList(initCtrlDep);
		StatementList elsestmts = p.getElseStatements();
		elsestmts.accept(this);
		controlDependences = initCtrlDep;
	}

	public void visit(InstanceofExpression p) {
	}

	public void visit(LabeledStatement p) {
	}

	public void visit(Literal p) {
	}

	public void visit(MemberDeclarationList p) {
	}

	public void visit(MemberInitializer p) {
	}

	public void visit(MethodCall p) {
	}

	public void visit(MethodDeclaration p) {
	}

	public void visit(ModifierList p) {
	}

	public void visit(Parameter p) {
	}

	public void visit(ParameterList p) {
	}

	public void visit(ReturnStatement p) {
	}

	public void visit(SelfAccess p) {
	}

	/**
	 * Adds trace statement to StatementList.
	 */
	public void visit(StatementList p) throws ParseTreeException {
		branchCounter++;
		Statement traceBranch = instrumentor.makeTraceStatement();
		controlDependences.add(0, new Integer(branchCounter));
		java.util.Enumeration it = p.elements();
		while (it.hasMoreElements()) {
			ParseTree elem = (ParseTree) it.nextElement();
			elem.accept(this);
		}
		p.insertElementAt(traceBranch, 0);
	}

	/**
	 * Resorts to visit(CaseGroupList) to iterate over cases in switch.
	 */
	public void visit(SwitchStatement p) throws ParseTreeException {
		CaseGroupList cases = p.getCaseGroupList();
		cases.accept(this);
	}

	public void visit(SynchronizedStatement p) {
	}

	public void visit(ThrowStatement p) {
	}

	/**
	 * Resorts to visit(StatementList) to add trace statement to each try block.
	 */
	public void visit(TryStatement p) throws ParseTreeException {
		java.util.List initCtrlDep = new java.util.LinkedList(controlDependences);
		StatementList stmts = p.getBody();
		stmts.accept(this);
		controlDependences = new java.util.LinkedList(initCtrlDep);
		CatchList catches = p.getCatchList();
		if (catches != null && !catches.isEmpty())
			catches.accept(this);
		controlDependences = new java.util.LinkedList(initCtrlDep);
		stmts = p.getFinallyBody();
		if (stmts != null && !stmts.isEmpty())
			stmts.accept(this);
		controlDependences = initCtrlDep;
	}

	public void visit(TypeName p) {
	}

	public void visit(UnaryExpression p) {
	}

	public void visit(Variable p) {
	}

	public void visit(VariableDeclaration p) {
	}

	public void visit(VariableDeclarator p) {
	}

	/**
	 * Resorts to visit(StatementList) add trace statement to while loop.
	 */
	public void visit(WhileStatement p) throws ParseTreeException {
		java.util.List initCtrlDep = new java.util.LinkedList(controlDependences);
		StatementList stmts = p.getStatements();
		stmts.accept(this);
		controlDependences = initCtrlDep;
	}

}
