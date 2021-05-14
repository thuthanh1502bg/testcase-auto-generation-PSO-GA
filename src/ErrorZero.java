public class ErrorZero
{

    public ErrorZero()
    {
        super();
        trace.add( new Integer( 4 ) );
    }

    public int methodAux( int x, int y )
    {
        trace.add( new Integer( 5 ) );
        if (x > 0) {
            trace.add( new Integer( 6 ) );
            return x + y;
        } else {
            trace.add( new Integer( 7 ) );
            return 0;
        }
    }

    public int soNT( int n )
    {
        trace.add( new Integer( 8 ) );
        for (int i = 2; i <= Math.sqrt( n ); i++) {
            trace.add( new Integer( 9 ) );
            if (n % i == 0) {
                trace.add( new Integer( 10 ) );
                return 1;
            } else {
                trace.add( new Integer( 11 ) );
            }
        }
        return n;
    }

    public int getListNT( int[] a )
    {
        trace.add( new Integer( 12 ) );
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            trace.add( new Integer( 13 ) );
            if (a[i] % 7 == 0) {
                trace.add( new Integer( 14 ) );
                result++;
            } else {
                trace.add( new Integer( 15 ) );
            }
        }
        if (result > 2) {
            trace.add( new Integer( 16 ) );
            return result;
        } else {
            trace.add( new Integer( 17 ) );
            return 0;
        }
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
