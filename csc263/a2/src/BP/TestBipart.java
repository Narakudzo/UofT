package BP;

public class TestBipart
{
  /* If no arguments, run each test sequentially, but abort at the first failure.
     If the first argument is i (a number), just run test #i.
     If there is a second argument, any failure message is suppressed. The
       exit code indicates success or failure.
   */
  public static void main(String[] args) {
    Test[] suite = {
      new NoRivalry(), new Triangle(), new Quad(),
      new B2v3(), new Petersen(),
      new LongOddCycle(),
      new LargeYes(), new LargeNo()
    };

    if (args.length == 0) {
      for (int i = 0; i < suite.length; i++) {
        System.out.println("running test " + i);
        suite[i].test();
      }
      System.out.println("finished");
    } else {
      int i = Integer.parseInt(args[0]);
      Test t = suite[i];
      if (args.length == 1) {
        t.test();
      } else {
        try {
          t.test();
        }
        catch (Exception e) {
          System.exit(1);
        }
      }
    }
  }

  public static class Fail extends RuntimeException
  {
    public Fail() { super(); };
    public Fail(boolean correct, boolean yours) {
      super("your answer is " + yours + " but the correct answer is " + correct);
    }
  }

  public static interface Test
  {
    public abstract void test();
  }

  private static void expect(boolean correct, boolean yours) {
    if (correct != yours) {
      throw new Fail(correct, yours);
    }
  }

  public static class NoRivalry implements Test
  {
    public void test() {
      expect(true, new Bipart().bipartable(10, new Rival[0]));
    }
  }

  public static class Triangle implements Test
  {
    public void test() {
      Rival[] delta = {new Rival(3,1), new Rival(2,3), new Rival(1,2)};
      expect(false, new Bipart().bipartable(4, delta));
    }
  }

  public static class Quad implements Test
  {
    public void test() {
      Rival[] q = {new Rival(3,0), new Rival(2,3), new Rival(1,2), new Rival(0,1)};
      expect(true, new Bipart().bipartable(4, q));
    }
  }

  public static class B2v3 implements Test
  {
    public void test() {
      Rival[] r = new Rival[6];
      int n = 0;
      for (int x = 0; x < 2; x++) {
        for (int y = 2; y < 5; y++) {
          r[n] = new Rival(x, y);
          n++;
        }
      }
      expect(true, new Bipart().bipartable(5, r));
    }
  }

  public static class Petersen implements Test
  {
    // http://en.wikipedia.org/wiki/Petersen_graph
    public void test() {
      Rival[] petersen = {
        new Rival(0,1), new Rival(1,2), new Rival(2,3), new Rival(3,4), new Rival(4,0),
        new Rival(5,7), new Rival(6,8), new Rival(7,9), new Rival(8,5), new Rival(9,6),
        new Rival(0,5), new Rival(1,6), new Rival(2,7), new Rival(3,8), new Rival(4,9)
      };
      expect(false, new Bipart().bipartable(10, petersen));
    }
  }

  public static class LongOddCycle implements Test
  {
    public void test() {
      Rival[] r = new Rival[200001];
      for (int i = 0; i < 200000; i++) {
        r[i] = new Rival(i, i+1);
      }
      r[200000] = new Rival(0, 200000);
      expect(false, new Bipart().bipartable(200001, r));
    }
  }

  public static class LargeYes implements Test
  {
    public void test() {
      Rival[] r = new Rival[567000];
      int n = 0;
      for (int x = 0; x < 1000; x++) {
        for (int y = 1000; y < 1700; y++) {
          if (x % 10 != 0 && y % 10 != 0) {
            r[n] = new Rival(x, y);
            n++;
          }
        }
      }
      expect(true, new Bipart().bipartable(1700, r));
    }
  }

  public static class LargeNo implements Test
  {
    public void test() {
      Rival[] r = new Rival[567001];
      int n = 0;
      for (int x = 0; x < 1000; x++) {
        for (int y = 1000; y < 1700; y++) {
          if (x % 10 != 0 && y % 10 != 0) {
            r[n] = new Rival(x, y);
            n++;
          }
        }
      }
      r[n] = new Rival(1215, 1453);
      expect(false, new Bipart().bipartable(1700, r));
    }
  }
}
