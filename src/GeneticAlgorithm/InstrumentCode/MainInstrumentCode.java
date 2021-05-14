package GeneticAlgorithm.InstrumentCode;

/**
 * Thực hiện phân tích code
 */
public class MainInstrumentCode {
	public static void main(String[] args) {

		String[] inputs = { "Triangle"};
		// instrument code, phân tích code và tạo chữ kí, path..(.path, .sign, .tgt)
		for (String str : inputs) {
			String[] srcfiles = {InstrumentorConfiguration.relativePath + str + ".oj" };
			openjava.ojc.Main.main(srcfiles);
		}
	}

}
