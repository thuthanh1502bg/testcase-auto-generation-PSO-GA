import junit.framework.TestCase;

public class DivZeroTest extends TestCase {

  public static void main (String[] args) {
    junit.textui.TestRunner.run(DivZeroTest.class);
  }

  @org.junit.jupiter.api.Test
  public void testCase1() {
    DivZero x601 = new DivZero();
    assertEquals("0",String.valueOf(x601.divZero(56914)));
  }

  @org.junit.jupiter.api.Test
  public void testCase2() {
    DivZero x1801 = new DivZero();
    x1801.divZero(-36271);
    System.out.println("OK");
  }

  }
