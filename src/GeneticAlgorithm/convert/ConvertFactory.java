package GeneticAlgorithm.convert;

public class ConvertFactory {
    private static final ConvertFactory convertFactory= new ConvertFactory();

    public static ConvertFactory getInstance(){
        return convertFactory;
    }

    public boolean[] convertToBooleanArray(Object[] objects){
        boolean[] convertedArray= new boolean[objects.length];
        for(int i=0;i<objects.length;i++){
            convertedArray[i]=(boolean)objects[i];
        }
        return convertedArray;
    }

    public char[] convertToCharArray(Object[] objects){
        char[] convertedArray= new char[objects.length];
        for(int i=0;i<objects.length;i++){
            convertedArray[i]=(char)objects[i];
        }
        return convertedArray;
    }


    public byte[] convertToByteArray(Object[] objects){
        byte[] convertedArray= new byte[objects.length];
        for(int i=0;i<objects.length;i++){
            convertedArray[i]=(byte)objects[i];
        }
        return convertedArray;
    }

    public short[] convertToShortArray(Object[] objects){
        short[] convertedArray= new short[objects.length];
        for(int i=0;i<objects.length;i++){
            convertedArray[i]=(short)objects[i];
        }
        return convertedArray;
    }

    public int[] convertToIntArray(Object[] objects){
        int[] convertedArray= new int[objects.length];
        for(int i=0;i<objects.length;i++){
            convertedArray[i]=(int)objects[i];
        }
        return convertedArray;
    }

    public long[] convertToLongArray(Object[] objects){
        long[] convertedArray= new long[objects.length];
        for(int i=0;i<objects.length;i++){
            convertedArray[i]=(long)objects[i];
        }
        return convertedArray;
    }

    public float[] convertToFloatArray(Object[] objects){
        float[] convertedArray= new float[objects.length];
        for(int i=0;i<objects.length;i++){
            convertedArray[i]=(float)objects[i];
        }
        return convertedArray;
    }

    public double[] convertToDoubleArray(Object[] objects){
        double[] convertedArray= new double[objects.length];
        for(int i=0;i<objects.length;i++){
            convertedArray[i]=(double)objects[i];
        }
        return convertedArray;
    }

}
