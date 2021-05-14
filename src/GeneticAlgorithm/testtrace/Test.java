package GeneticAlgorithm.testtrace;

public class Test {
    public static void main(String[] args) {
        System.out.println();
        demo();
        StackTraceElement[] a= Thread.currentThread().getStackTrace();
        demo();
        for (StackTraceElement b:a
             ) {
            System.out.println(b.getMethodName());
            System.out.println(b.getClassName());
            System.out.println(b.getFileName());
            System.out.println(b.getClass());
        }
        demo();
    }
    public static void demo(){
        ClassA A = new ClassA();


    }
}
