package GeneticAlgorithm.GA;

//import sun.security.util.ArrayUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainGA {
    static Random randomGenerator = new Random();
    static String[] inputs = {"Triangle"};
    static float coverRatio = 0f;
    static int numberGen = 0;

    /**
     * Main chính
     *
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        // tạo testcase
        generateTestcaseWithGA("Triangle");
        // tổng số lần generate để tạo được testcase
        System.out.println("number of gen: " + numberGen);

    }

    /**
     * tạo các testcase bằng cách áp dụng GA
     *
     * @param classUnderTest class đang tạo unit test
     * @throws IOException
     */
    public static void generateTestcaseWithGA(String classUnderTest) throws IOException {
        TestGenerator.printParameters();

        long startTime = System.currentTimeMillis();
        Population destinationPop = null;
        // Lấy ra các branchTarget
        TestGenerator.pathsFile = CommonParameter.relativePath + classUnderTest + ".path";
        TestGenerator.targetFile = CommonParameter.relativePath + classUnderTest + ".tgt";
        TestGenerator testGenerator = new TestGenerator();

        List<Set> listBranchTarget = testGenerator.getBranchSetFromPaths();

        List<Set> listBranchTargetCovered = new LinkedList<>();
        List<Set> listBranchTargetNotCovered = new LinkedList<>();

        List listTarget = testGenerator.getBranchWithMethod();
        String[] branchWithMethod = getBranchForMethod(listTarget, listBranchTarget);

        int numberOfTestcase = 0;
        boolean covered = false;

        String signFile = CommonParameter.relativePath + classUnderTest + ".sign";
        ChromosomeFormer chromosomeFormer= Population.setChromosomeFormer(signFile);

        //ChromosomeFormer is used to collect all methods and constructor of CUT ( main purpose)
        ChromosomeFormer c = new ChromosomeFormer();

        c.setConstructors(chromosomeFormer.getConstructors());
        c.readSignatures(signFile);
        c.classUnderTest = classUnderTest;
        c.buildNewChromosome();
        String[] listMethod = c.methods.values().toString().split(",");

        for (int method = 0; method < listMethod.length; method++) {
            Population.idMethodUnderTest = method;
            System.out.println(Population.chromosomeFormer);
            String[] idBranchString = branchWithMethod[method + (listTarget.size() - listMethod.length)].split(",");
            int[] idBranch = new int[idBranchString.length];
            for (int k = 0; k < idBranchString.length; k++) {
                idBranch[k] = Integer.parseInt(idBranchString[k]);
            }
            for (int i = idBranch[0]; i <= idBranch[idBranchString.length - 1]; i++) {
                // Với mỗi target, xây dựng quần thể ban đầu và thực hiên thuật toán GA
                Population.setCurTarget(listBranchTarget.get(i));

                String string = String.join(", ", listBranchTarget.get(i));
                String[] target = (String.join(", ", listBranchTarget.get(i))).split(",");
                int curFittestTarget = target.length;

                // Khởi tạo quẩn thể và tính fitness cho từng chromosome
                Population initPop = Population.generateRandomPopulation();
                // Sắp xếp các cá thể theo thứ tự fitness giảm dần
                Collections.sort(initPop.individuals);

                if (Population.getExtendTarget().size() > 0) {
                    for (int j = 0; j < Population.getExtendTarget().size(); j++) {
                        Population.preTarget = Population.getCurTarget();
                        Population.setCurTarget(Population.getExtendTarget().get(j));
                        // tính tại giá trị fitness
                        initPop = Population.generateRandomPopulation();
                        // Sắp xếp các cá thể theo thứ tự fitness giảm dần
                        Collections.sort(initPop.individuals);
                        target = Population.curTarget.toString().split(",");
                        curFittestTarget = target.length;
                        Population selectionPop = initPop.selection();
                        int generationCount = 1;
                        // thực hiện lai ghép và đột biến khi chưa được cover hoặc chưa đạt mức vòng lặp
                        // tối đa
                        while (initPop.getFittest() < curFittestTarget && generationCount < CommonParameter.maxLoop) {
                            try {
                                int x = randomGenerator.nextInt(100);
                                if (x < 50) {

                                    initPop.crossover();
                                } else {
                                    initPop.mutation();
                                }
                                generationCount++;
                                Collections.sort(initPop.individuals);
                                System.out.println("Generation: " + generationCount + " Fittest: " + initPop.getFittest());
                            }catch(Exception ex){
                                System.out.println(ex.getMessage());
                                ex.printStackTrace();
                            }
                        }

                        Population.curTarget = Population.preTarget;
                        Population.preTarget = null;
                        if (generationCount < CommonParameter.maxLoop) {
                            numberGen += generationCount;
                            covered = true;
                            numberOfTestcase++;
                            // add testcase phù hợp vào quần thể đích
                            if (numberOfTestcase == 1) {
                                destinationPop = initPop.generateDestinationPopulation();
                            } else {
                                destinationPop = initPop.addDestinationPopulation(destinationPop);
                            }
                        }
                        if (j < Population.getExtendTarget().size() - 1) {
                            initPop = Population.generateRandomPopulation();
                            Collections.sort(initPop.individuals);
                        }

                    }
                    // clear extendTarget đi
                    Population.extendTarget.clear();
                } else {
                    Population selectionPop = initPop.selection();
                    int generationCount = 1;
                    while (initPop.getFittest() < curFittestTarget && generationCount < CommonParameter.maxLoop) {
                        int x = randomGenerator.nextInt(100);
                        if (x < 50) {
                            initPop.crossover();
                        } else {
                            initPop.mutation();
                        }
                        generationCount++;
                        Collections.sort(initPop.individuals);
                        System.out.println("Generation: " + generationCount + " Fittest: " + initPop.getFittest());

                    }
                    if (generationCount < CommonParameter.maxLoop) {
                        numberGen += generationCount;
                        covered = true;
                        numberOfTestcase++;
                        if (numberOfTestcase == 1) {
                            destinationPop = initPop.generateDestinationPopulation();
                        } else {
                            destinationPop = initPop.addDestinationPopulation(destinationPop);
                        }
                    }

                }
                if (covered) {
                    listBranchTargetCovered.add(listBranchTarget.get(i));
                } else
                    listBranchTargetNotCovered.add(listBranchTarget.get(i));
                covered = false;

            }
        }
        long time = 0;
        time = System.currentTimeMillis() - startTime;
        if (listBranchTargetCovered.size() != 0) {
            for (int i = 0; i < destinationPop.individuals.size(); i++) {
                Chromosome id = (Chromosome) destinationPop.individuals.get(i);
                System.out.println(id.toString());
                System.out.println(id.getCoveredTarget());
            }
            testGenerator.junitFile = classUnderTest + "Test.java";
//            testGenerator.printJunitFileFirst(destinationPop);
            System.out.println(classUnderTest+"Test.java");
        } else {
            System.out.println("Không tạo được testcase");
        }

        coverRatio = coverRatio + (float) listBranchTargetCovered.size()
                / (listBranchTargetCovered.size() + listBranchTargetNotCovered.size()) * 100;

        testGenerator.toString();
        System.out.println(testGenerator);
        System.out.println("===============================");
        System.out.println("Tổng thời gian tạo testcase:" + (float) time / 1000 + "s");
        System.out.println("Tỉ lệ cover: " + listBranchTargetCovered.size() + "/"
                + (listBranchTargetCovered.size() + listBranchTargetNotCovered.size()) + " ~ " + coverRatio + "%");
        System.out.println("==========================================");
        System.out.println(listBranchTarget);
        System.out.println("==========================================");
        System.out.println(listBranchTargetCovered);
        System.out.println("==========================================");
        System.out.println(listBranchTargetNotCovered);
    }

    /**
     * lấy ra các id branch ứng với từng method
     *
     * @param listTarget       list các target của các method
     * @param listBranchTarget list các branch
     * @return String[] ptử thứ i trong mảng chứa các branch ứng với method thứ i
     */
    public static String[] getBranchForMethod(List listTarget, List<Set> listBranchTarget) {
        String[] branchWithMethod = new String[listTarget.size()];

        for (int i = 0; i < listTarget.size(); i++) {
            branchWithMethod[i] = "";
            for (int j = 0; j < listBranchTarget.size(); j++) {
                Pattern pattern = Pattern.compile("[0-9]+");
                String temp = "";
                for (int k = 0; k < listBranchTarget.get(j).toString().length(); k++) {
                    Matcher matcher = pattern.matcher(listBranchTarget.get(j).toString().charAt(k) + "");
                    if (matcher.matches()) {
                        temp += listBranchTarget.get(j).toString().charAt(k) + "";
                    } else {
                        if (temp.equals("") == true) {
                            continue;
                        } else
                            break;
                    }
                }
                if (!temp.equals("")) {
                    if (listTarget.get(i).toString().equals("[" + temp + "]")) {
                        branchWithMethod[i] += Integer.toString(j) + ",";
                        break;
                    } else if (listTarget.get(i).toString().contains("[" + temp + ",")) {
                        branchWithMethod[i] += Integer.toString(j) + ",";
                    }
                }

            }
        }
        return branchWithMethod;
    }

    /**
     * Lấy ra các branch target khi intergration test
     *
     * @param subClassUnderTest class integrate
     * @param subTrace          đường dẫn extends
     * @return
     * @throws IOException
     */
    public static List<String> generateExtendTarget(String subClassUnderTest, Collection subTrace) throws IOException {
        List<String> target = new LinkedList();

        // Lấy ra các branchTarget
        TestGenerator.pathsFile = CommonParameter.relativePath + subClassUnderTest + ".path";
        TestGenerator.targetFile = CommonParameter.relativePath + subClassUnderTest + ".tgt";

        TestGenerator testGenerator = new TestGenerator();

        List<Set> listBranchTarget = testGenerator.getBranchSetFromPaths();
        List listTarget = testGenerator.getBranchWithMethod();

        String subTraceString = subTrace.toString();
        target = getTargetFromCoverPath(listTarget, subTraceString);
        List<String> extendTarget = new LinkedList();
        for (int i = 0; i < target.size(); i++) {
            String[] temp = getBranchForTarget(target.get(i), listBranchTarget);
            for (String s : temp) {
                extendTarget.add(s);
            }
        }
        return extendTarget;

    }

    /**
     * Lấy các các target method có intergrate
     *
     * @param listTarget
     * @param subTraceString
     * @return
     */
    public static List<String> getTargetFromCoverPath(List listTarget, String subTraceString) {
        List<String> target = new LinkedList();
        for (int i = 1; i < listTarget.size(); i++) {
            Pattern pattern = Pattern.compile("[0-9]+");
            String temp = "";
            String subTarget = "";
            for (int k = 0; k < subTraceString.length(); k++) {
                Matcher matcher = pattern.matcher(subTraceString.charAt(k) + "");
                if (matcher.matches()) {
                    temp += subTraceString.charAt(k) + "";
                } else {
                    if (!temp.equals("")) {
                        if (listTarget.get(i).toString().equals("[" + temp + "]")
                                || listTarget.get(i).toString().contains("[" + temp + ",")
                                || listTarget.get(i).toString().contains(", " + temp + ",")
                                || listTarget.get(i).toString().contains(", " + temp + "]")) {
                            subTarget = listTarget.get(i).toString();
                            target.add(subTarget);
                        }
                        temp = "";
                        if (!subTarget.equals(""))
                            break;
                        else
                            continue;
                    } else
                        continue;
                }
            }
        }
        return target;
    }

    /**
     * Lấy các branch target
     *
     * @param target
     * @param listBranchTarget
     * @return
     */
    public static String[] getBranchForTarget(String target, List<Set> listBranchTarget) {
        String[] branchWithTarget = new String[target.split(",").length];
        int i = 0;
        for (int j = 0; j < listBranchTarget.size(); j++) {
            Pattern pattern = Pattern.compile("[0-9]+");
            String temp = "";
            for (int k = 0; k < listBranchTarget.get(j).toString().length(); k++) {
                Matcher matcher = pattern.matcher(listBranchTarget.get(j).toString().charAt(k) + "");
                if (matcher.matches()) {
                    temp += listBranchTarget.get(j).toString().charAt(k) + "";
                } else {
                    if (temp.equals("") == true) {
                        continue;
                    } else
                        break;
                }
            }
            if (!temp.equals("")) {
                if (target.equals("[" + temp + "]") || target.contains("[" + temp + ",")
                        || target.contains(" " + temp + ",") || target.contains(" " + temp + "]")) {
                    branchWithTarget[i] = listBranchTarget.get(j).toString();
                    i++;
                }
            }
        }
        return branchWithTarget;
    }
}
