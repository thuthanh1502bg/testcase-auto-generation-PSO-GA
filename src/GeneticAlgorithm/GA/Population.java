
package GeneticAlgorithm.GA;

import java.io.IOException;
import java.util.*;

public class Population {
	/**
	 * Used to randomly select individuals for new population.
	 */
	static Random randomGenerator = new Random();
	/**
	 * Target hiện tại đang xét của population
	 */
	static Set curTarget;

	/**
	 * @return the curTarget
	 */
	public static Set getCurTarget() {
		return curTarget;
	}

	/**
	 * @param curTarget
	 *            the curTarget to set
	 */
	public static void setCurTarget(Set curTarget) {
		Population.curTarget = curTarget;
	}

	/**
	 * các branch mở rộng ( khi gọi đến method khác)
	 */
	static List<Set> extendTarget = new LinkedList<Set>();;

	/**
	 * @return the extendTarget
	 */
	public static List<Set> getExtendTarget() {
		return extendTarget;
	}

	/**
	 * @param extendTarget
	 *            the extendTarget to set
	 */
	public static void setExtendTarget(List<Set> extendTarget) {
		Population.extendTarget = extendTarget;
	}

	/**
	 * Target hiện tại đang xét của population
	 */
	static Set preTarget;

	/**
	 * Method đang test
	 */
	static int idMethodUnderTest;

	/**
	 * @return the idMethodUnderTest
	 */
	public int getIdMethodUnderTest() {
		return idMethodUnderTest;
	}

	/**
	 * @param idMethodUnderTest
	 *            the idMethodUnderTest to set
	 */
	public void setIdMethodUnderTest(int idMethodUnderTest) {
		this.idMethodUnderTest = idMethodUnderTest;
	}

	/**
	 * list các cá thể
	 *
	 * List<Chromosome>
	 */
	List individuals;

	/**
	 * ChromosomeFormer chịu trách nhiệm tạo ra từng cá thể đơn lẻ và sự tiến hóa /
	 * tái hợp của nó.
	 */
	static ChromosomeFormer chromosomeFormer;

	/**
	 * Thông số chính của thuật toán di truyền: số lượng cá thể (nhiễm sắc thể)
	 * trong quần thể
	 */
	public static int populationSize = CommonParameter.populationSize;

	/**
	 * khởi tạo population
	 */
	public Population(List id) {
		individuals = id;
	}

	/**
	 * tạo mới ChromosomeFormer.
	 *
	 * @param signFile
	 *            Tập tin có chữ ký phương thức. từ đó tạo ra chromosome
	 */
	public static ChromosomeFormer setChromosomeFormer(String signFile) {
		chromosomeFormer = new ChromosomeFormer();
		chromosomeFormer.addDefaultConstructor();
//		if(signFile.contains("Triangle")) {
//			chromosomeFormer.readSignatures("..\\Final_AutomationTesting\\src\\Point.sign");
//			chromosomeFormer.readSignatures("..\\Final_AutomationTesting\\src\\Scale.sign");
//		}
//		else if(signFile.contains("BinarySearch")){
//			chromosomeFormer.readSignatures("..\\Final_AutomationTesting\\src\\MyInt.sign");
//			chromosomeFormer.readSignatures("..\\Final_AutomationTesting\\src\\Point.sign");
//		}
		chromosomeFormer.readSignatures(signFile);
		return chromosomeFormer;
	}

	/**
	 * @param chromosomeFormer
	 *            the chromosomeFormer sẽ set
	 */
	public static void setChromosomeFormer(ChromosomeFormer chromosomeFormer) {
		Population.chromosomeFormer = chromosomeFormer;
	}

	/**
	 * Khởi tạo population ban đầu
	 * 
	 * @return population chứa các cá thể
	 * @throws IOException
	 */
	public static Population generateRandomPopulation() throws IOException {
		List individs = new LinkedList();
		chromosomeFormer.idMethodUnderTest = idMethodUnderTest;
		for (int j = 0; j < Population.populationSize; j++) {

			chromosomeFormer.buildNewChromosome();
			individs.add(chromosomeFormer.getChromosome());
			chromosomeFormer.fitness = 0;
			chromosomeFormer.caculateApproachLevel(curTarget);
		}
		return new Population(individs);
	}

	/**
	 * lựa chọn quần thể con
	 * 
	 * @return quần thể
	 */
	public Population selection() {
		int numberSelection = (int) (populationSize * CommonParameter.cumulativeProbability);
		List newIndividuals = new LinkedList();
		for (int i = 0; i < numberSelection; i++) {
			Chromosome id = (Chromosome) (individuals.get(i));
			chromosomeFormer.setCurrentChromosome(id);
			newIndividuals.add(chromosomeFormer.getChromosome());
		}
		return new Population(newIndividuals);

	}

	/**
	 * lai ghép
	 * 
	 * @throws IOException
	 */
	public void crossover() throws IOException {
		int x = (int) (populationSize * CommonParameter.cumulativeProbability / 2);
		for (int k = 0; k < x; k = k + 2) {
			Chromosome id1 = (Chromosome) (individuals.get(k));
			System.out.println("chromosome---------------------");
			System.out.println(id1.toString());
			Chromosome id2 = (Chromosome) (individuals.get(k + 1));
			System.out.println(id2.toString());
			if (id1.getArrayOfActualValues() == null || id2.getArrayOfActualValues() == null) {
				return;
			} else {
				String[] chromValue1 = id1.getArrayOfActualValues();
				System.out.println(Arrays.toString(chromValue1));
				System.out.println(chromValue1.length);
				String[] chromValue2 = id2.getArrayOfActualValues();
				System.out.println(Arrays.toString(chromValue2));
				System.out.println(chromValue2.length);

				if (chromValue1.length == 1 || chromValue2.length == 1 || chromValue1.length != chromValue2.length) {
					mutationOneChromosome();
					break;
				} else {
					int indexValue = 1 + randomGenerator.nextInt(chromValue1.length - 1);
					for (int i = indexValue; i < chromValue1.length; i++) {
						String temp = chromValue1[i];
						chromValue1[i] = chromValue2[i];
						chromValue2[i] = temp;
					}
//					Chromosome offspring1 = (Chromosome) (individuals.get(populationSize - 1 - k));
					System.out.println(chromosomeFormer.getChromosome().toString());
					Chromosome offspring1 = id1;
					System.out.println(offspring1.toString());
					offspring1.setInputValue(new ArrayList<String>(Arrays.asList(chromValue1)));
					System.out.println("pk+ "+chromosomeFormer.getChromosome().toString());
					chromosomeFormer.setCurrentChromosome(offspring1);
					try {
						chromosomeFormer.fitness = 0;
						chromosomeFormer.caculateApproachLevel(curTarget);
					}catch (Exception e){
						e.printStackTrace();
					}

//					Chromosome offspring2 = (Chromosome) (individuals.get(populationSize - 1 - k - 1));
					Chromosome offspring2 = id2;
					offspring2.setInputValue(new ArrayList<String>(Arrays.asList(chromValue2)));
					chromosomeFormer.setCurrentChromosome(offspring2);
					try {
						chromosomeFormer.fitness = 0;
						chromosomeFormer.caculateApproachLevel(curTarget);
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 * đột biến cá thể bất kì trong quần thể được chọn
	 * 
	 * @throws IOException
	 */
	public void mutation() throws IOException {
		int x = (int) (populationSize * CommonParameter.mutationProbability);
		for (int i = 0; i < x; i++) {
			int rd = randomGenerator.nextInt(populationSize);
			Chromosome id = (Chromosome) (individuals.get(rd));

			System.out.println(id.toString());
			id.mutation();
			System.out.println(id.toString());
			chromosomeFormer.setCurrentChromosome(id);
			chromosomeFormer.fitness = 0;
			chromosomeFormer.caculateApproachLevel(curTarget);
		}

	}

	/**
	 * đột biến 1 cá thể
	 * 
	 * @throws IOException
	 */
	public void mutationOneChromosome() throws IOException {
		int rd = randomGenerator.nextInt(populationSize);
		Chromosome id = (Chromosome) (individuals.get(rd));
		id.mutation();
		chromosomeFormer.setCurrentChromosome(id);
		chromosomeFormer.fitness = 0;
		chromosomeFormer.caculateApproachLevel(curTarget);
	}

	/**
	 * Lấy fitness cao nhất của chromosome
	 * 
	 * @return
	 */
	public double getFittest() {
		Chromosome id1 = (Chromosome) (individuals.get(0));
		chromosomeFormer.setCurrentChromosome(id1);
		return chromosomeFormer.fitness;
	}

	/**
	 * Tạo population chứa các chromosome sẽ mang đi tạo testcase
	 * 
	 * @return population chứa các cá thể
	 * @throws IOException
	 */
	public Population generateDestinationPopulation() throws IOException {
		List newIndividuals = new LinkedList();
		Chromosome id = (Chromosome) (individuals.get(0));
		chromosomeFormer.setCurrentChromosome(id);

		newIndividuals.add(chromosomeFormer.getChromosome());
		return new Population(newIndividuals);
	}

	/**
	 * Add thêm chromosome cho Destination Population
	 * 
	 * @param pop
	 *            Destination Population đã có
	 * @return
	 * @throws IOException
	 */
	public Population addDestinationPopulation(Population pop) throws IOException {
		Population newPopulation = new Population(pop.individuals);
		Chromosome id = (Chromosome) (individuals.get(0));
		chromosomeFormer.setCurrentChromosome(id);

		newPopulation.individuals.add(id);

		return newPopulation;
	}

	/**
	 * 
	 */
	public String toString() {
		String s = "";
		Iterator i = individuals.iterator();
		while (i.hasNext()) {
			Chromosome id = (Chromosome) i.next();
			s += id.toString() + "\n";
		}
		return s;
	}

}
