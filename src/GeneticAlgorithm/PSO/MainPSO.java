package GeneticAlgorithm.PSO;

import java.io.IOException;
import java.util.Random;

import GeneticAlgorithm.GA.Population;
import GeneticAlgorithm.GA.TestGenerator;

public class MainPSO {
	
	static Random randomGenerator = new Random();
    static String[] inputs = {"Triangle"};
    static float coverRatio = 0f;
    static int numberGen = 0;
	
	public static void main(String[] args) throws InterruptedException, IOException {
        // tạo testcase
        generateTestcaseWithPSO("Triangle");
        // tổng số lần generate để tạo được testcase
        System.out.println("number of gen: " + numberGen);

    }
	
	public static void generateTestcaseWithPSO(String classUndertest) {
		TestGenerator.printParameters();
		long startTime = System.currentTimeMillis();
		
	}
}
