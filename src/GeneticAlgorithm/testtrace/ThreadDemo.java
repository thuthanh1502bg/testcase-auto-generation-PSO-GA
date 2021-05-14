package GeneticAlgorithm.testtrace;

public class ThreadDemo {

    public static void main(String[] args) {
        function();
    }

    public static void function() {
        new ThreadDemo().function2();
    }

    public void function2() {
        System.out.println(Thread.currentThread().getStackTrace()[3].getClassName());
    }
}
