public class Pow   {

	public Pow() {
		super();
	}

	public void pow(int x, int y) {
		float z;
		int p;
		if (y < 0) {
			p = 0 - y;
		} else {
			p = y;
		}
		z = 1.0f;
		while (p != 0) {
			z = z * x;
			p = p - 1;

		}
		if (y < 0) {
			z = 1.0f / z;
			System.out.println(z);
		}
	}
	
}
