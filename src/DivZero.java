

public class DivZero
{

    public DivZero()
    {
        super();
        trace.add( new Integer( 1 ) );
    }

    public int divZero( int a )
    {
        trace.add( new Integer( 2 ) );
        int b = Math.abs( a ) - 8;
        int c = b - 2;
        int d = a - 8;
        int num = c + b;
        ErrorZero e = new ErrorZero();
        int den = e.methodAux( d, b );
        int result = num / den;
        System.out.println( result );
        return result;
    }

     
    static java.util.Set trace = new java.util.HashSet();

    
    public static void newTrace()
    {
        trace = new java.util.HashSet();
    }

    
    public static java.util.Set getTrace()
    {
        return trace;
    }

}
