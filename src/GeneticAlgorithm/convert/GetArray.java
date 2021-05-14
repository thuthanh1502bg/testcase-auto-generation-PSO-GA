package GeneticAlgorithm.convert;

import java.lang.reflect.Array;

public class GetArray<T> {
    public <T> T[] newArray(Class<T> type, int size){
        T[] theArray = (T[]) Array.newInstance(type, size);
        return theArray;
    }
}
