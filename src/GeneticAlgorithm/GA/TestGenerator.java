package GeneticAlgorithm.GA;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestGenerator {
	/**
	 * Junit file
	 *
	 */
	String junitFile = null;

	/**
	 * Branch sẽ được cover( method và các statement). Thuộc tính bao gồm các đối
	 * tượng Target
	 */
	List targets = new LinkedList();

	/**
	 * List các testcase final
	 */
	List testCases = null;

	/**
	 * Control dependences associated to each target.
	 * 
	 *
	 * target: BranchTarget -> Set<nodes: BranchTarget>
	 */
	Map paths = new HashMap();

	/**
	 * File list target
	 */
	static String targetFile;

	/**
	 * File list path
	 */
	static String pathsFile;

	/**
	 * Đọc các file .path, .tgt...
	 */
	public TestGenerator() {
		readTarget();
		readPaths();

	}

	/**
	 * In các configuration.
	 */
	public static void printParameters() {
		System.out.println("populationSize: " + CommonParameter.populationSize);
		System.out.println("maxAttemptsPerTarget: " + CommonParameter.maxLoop);

	}

	/**
	 * Đọc target từ file .tgt Format file .tgt:
	 *
	 * <pre>
	 * BinaryTree.search(Comparable): 5, 6, 7, 8, 9
	 * </pre>
	 */
	public void readTarget() {
		try {
			String s;
			Pattern p = Pattern.compile("([^\\s]+)\\s*:\\s*(.*)");
			BufferedReader in = new BufferedReader(new FileReader(targetFile));
			while ((s = in.readLine()) != null) {
				Matcher m = p.matcher(s);
				if (!m.find())
					continue;
				String method = m.group(1);
				MethodTarget tgt = new MethodTarget(method);
				String[] branches = m.group(2).split(",");
				for (int i = 0; i < branches.length; i++) {
					int n = Integer.parseInt(branches[i].trim());
					tgt.addBranch(n);
				}
				targets.add(tgt);

			}
		} catch (NumberFormatException e) {
			System.err.println("Wrong format file: " + targetFile);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO error: " + targetFile);
			System.exit(1);
		}
	}

	/**
	 * Đọc các path (file .path) Format of paths.txt:
	 *
	 * <pre>
	 * 10: 4 5 6
	 * 11: 4 5 6 10
	 * 12: 4 5 6 10
	 * </pre>
	 */
	public void readPaths() {
		try {
			String s;
			BufferedReader in = new BufferedReader(new FileReader(pathsFile));
			while ((s = in.readLine()) != null) {
				String r = s.substring(0, s.indexOf(":"));
				int tgt = Integer.parseInt(r);
				r = s.substring(s.indexOf(":") + 1);
				StringTokenizer tok = new StringTokenizer(r);
				ArrayList<BranchTarget> pathPoints = new ArrayList<BranchTarget>();
				while (tok.hasMoreTokens()) {
					int n = Integer.parseInt(tok.nextToken());
					pathPoints.add(new BranchTarget(n));

					Collections.sort(pathPoints);
				}
				paths.put(new BranchTarget(tgt), pathPoints);
			}
			String[] list = new String[paths.size()];
			List arrayKey = new LinkedList();
			arrayKey.addAll(paths.keySet());
			List arrayPaths = new LinkedList();
			arrayPaths.addAll(paths.values());
			for (int i = 0; i < paths.size(); i++) {
				String temp = arrayPaths.get(i).toString().replace("[", "").replaceAll("]", "");
				if (temp.length() < 1) {
					list[i] = " " + arrayKey.get(i).toString();
				} else {
					list[i] = " " + arrayPaths.get(i).toString().replace("[", "").replaceAll("]", "") + ", "
							+ arrayKey.get(i).toString();
				}

			}

			for (int i = 0; i < arrayPaths.size() - 1; i++) {
				for (int j = i + 1; j < arrayPaths.size(); j++) {
					if ((list[j]).contains(list[i] + ",")) {
						paths.remove(new BranchTarget(Integer.parseInt(arrayKey.get(i).toString())));
					} else if ((list[i]).contains(list[j] + ",")) {
						paths.remove(new BranchTarget(Integer.parseInt(arrayKey.get(j).toString())));
					}
				}
			}
		} catch (NumberFormatException e) {
			System.err.println("Wrong format file: " + pathsFile);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO error: " + pathsFile);
			System.exit(1);
		}
	}

	/**
	 * Lấy ra các target
	 */
	public List getAllTargets() {
		List targetsToCover = new LinkedList();
		Iterator i = targets.iterator();
		while (i.hasNext()) {
			Target tgt = (Target) i.next();
			targetsToCover.addAll(tgt.getSubTargets());
		}
		return targetsToCover;
	}

	/**
	 * Lấy ra các branch chuẩn từ tập các path
	 * 
	 * @return List<Set> tập hợp các branch có
	 */
	public List<Set> getBranchSetFromPaths() {
		List valuePaths = new LinkedList();
		valuePaths.addAll(paths.values());
		List keyPaths = new LinkedList();
		keyPaths.addAll(paths.keySet());

		List<Set> newSet = new LinkedList<>();
		for (int i = 0; i < valuePaths.size(); i++) {
			Set<String> temp = new HashSet<>();
			if (valuePaths.get(i).toString().equals("[]")) {
				temp.add(keyPaths.get(i) + "");
			} else {
				temp.add((valuePaths.get(i) + ", " + keyPaths.get(i)).replace("[", "").replaceAll("]", ""));
			}
			newSet.add(temp);
		}
		return newSet;
	}

	/**
	 * Lấy ra các target
	 */
	public List<String> getBranchWithMethod() {
		List targetsToCover = new LinkedList();
		Iterator i = targets.iterator();
		while (i.hasNext()) {
			Target tgt = (Target) i.next();
			targetsToCover.add(tgt.getSubTargets().toString());
		}
		return targetsToCover;
	}

	/**
	 * Minimize số lượng testcase
	 * 
	 * @param pop
	 *            quần thể chứa các testcase
	 * @return
	 */
	public Population minimizeTestCases(Population pop) {
		String target = "";
		for (String s : MainGA.inputs) {
			TestGenerator.targetFile = CommonParameter.relativePath + s + ".tgt";
			TestGenerator.pathsFile = CommonParameter.relativePath + s + ".path";
			TestGenerator testGenerator = new TestGenerator();
			target += testGenerator.getAllTargets().toString();
		}
		int count = 0;
		for (int j = 0; j < pop.individuals.size(); j++) {
			Pattern pattern = Pattern.compile("[0-9]+");
			String temp = "";

			int low = count;
			Chromosome id = (Chromosome) pop.individuals.get(j);
			for (int i = 0; i < id.getCoveredTarget().toString().length(); i++) {
				Matcher matcher = pattern.matcher(id.getCoveredTarget().toString().charAt(i) + "");
				if (matcher.matches()) {
					temp += id.getCoveredTarget().toString().charAt(i) + "";
				} else {
					if (temp.equals("") == true) {
						continue;
					} else {
						if (temp.equals("") == false) {
							int t = 0;
							if (target.contains("[" + temp + ","))
								t = 1;
							if (target.contains(" " + temp + ","))
								t = 2;
							if (target.contains(" " + temp + "]"))
								t = 3;
							if (target.contains("[" + temp + "]"))
								t = 4;
							switch (t) {
							case 1:
							case 2:
							case 3:
							case 4:
								count++;
								String str = target.substring(0, target.indexOf(temp))
										+ target.substring(target.indexOf(temp) + temp.length());
								target = str;
								break;
							}
						}
					}
					temp = "";
				}
			}
			int up = count;
			if (up == low)
				pop.individuals.remove(j);

		}

		return pop;
	}

	/**
	 * Generates Junit test class.
	 */
	public void printJunitFileFirst(Population pop) {

		try {
			System.out.println(junitFile);
			if (junitFile == null)
				return;
			String junitClass = junitFile.substring(0, junitFile.indexOf("."));
			PrintStream out = new PrintStream(new FileOutputStream(CommonParameter.relativePath + junitFile));
			out.println("import junit.framework.*;");

			out.println();
			out.println("public class " + junitClass + " extends TestCase {");
			out.println();
			out.println("  public static void main (String[] args) {");
			out.println("    junit.textui.TestRunner.run(" + junitClass + ".class);");
			out.println("  }");
			out.println();
			int n = 0;
			Chromosome id = null;
			for (int i = 0; i < pop.individuals.size(); i++) {
				id = (Chromosome) pop.individuals.get(i);
				n++;
				out.println("  @org.junit.jupiter.api.Test");
				out.println("  public void testCase" + n + "() {");
				out.print(id.toCode());
				out.println("  }");
				out.println();
			}
			out.println("  }");
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("cannot create file: " + junitFile);
			System.exit(1);
		}
	}

}
