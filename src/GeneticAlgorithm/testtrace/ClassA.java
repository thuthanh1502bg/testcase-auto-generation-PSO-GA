package GeneticAlgorithm.testtrace;

public class ClassA {
    private ClassB classB;
    public ClassA() {
    }

    public ClassA(ClassB classB) {
        this.classB = classB;
    }

    public ClassB getClassB() {
        return classB;
    }

    public void setClassB(ClassB classB) {
        this.classB = classB;
    }
}
