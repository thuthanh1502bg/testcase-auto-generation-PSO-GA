import junit.framework.TestCase;

public class InsuranceProgramTest extends TestCase {

  public static void main (String[] args) {
    junit.textui.TestRunner.run(InsuranceProgramTest.class);
  }

  @org.junit.jupiter.api.Test
  public void testCase1() {
    InsuranceProgram x19 = new InsuranceProgram(11, 0);
    assertEquals("600",String.valueOf(x19.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase2() {
    InsuranceProgram x35 = new InsuranceProgram(17, 1);
    assertEquals("650",String.valueOf(x35.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase3() {
    InsuranceProgram x49 = new InsuranceProgram(0, 2);
    assertEquals("725",String.valueOf(x49.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase4() {
    InsuranceProgram x85 = new InsuranceProgram(2, 3);
    assertEquals("825",String.valueOf(x85.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase5() {
    InsuranceProgram x103 = new InsuranceProgram(8, 4);
    assertEquals("975",String.valueOf(x103.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase6() {
    InsuranceProgram x125 = new InsuranceProgram(18, 5);
    assertEquals("1175",String.valueOf(x125.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase7() {
    InsuranceProgram x145 = new InsuranceProgram(3, 10);
    assertEquals("600",String.valueOf(x145.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase8() {
    InsuranceProgram x185 = new InsuranceProgram(25, 0);
    assertEquals("500",String.valueOf(x185.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase9() {
    InsuranceProgram x201 = new InsuranceProgram(34, 1);
    assertEquals("550",String.valueOf(x201.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase10() {
    InsuranceProgram x225 = new InsuranceProgram(32, 2);
    assertEquals("625",String.valueOf(x225.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase11() {
    InsuranceProgram x255 = new InsuranceProgram(34, 3);
    assertEquals("725",String.valueOf(x255.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase12() {
    InsuranceProgram x287 = new InsuranceProgram(33, 4);
    assertEquals("875",String.valueOf(x287.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase13() {
    InsuranceProgram x289 = new InsuranceProgram(47, 5);
    assertEquals("1075",String.valueOf(x289.getInsurance()));
  }

  @org.junit.jupiter.api.Test
  public void testCase14() {
    InsuranceProgram x317 = new InsuranceProgram(41, 7);
    assertEquals("500",String.valueOf(x317.getInsurance()));
  }

  }
