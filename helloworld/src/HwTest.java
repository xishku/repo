
import static java.lang.String.valueOf;

public class HwTest {

    public static long testLoop(long len)
    {
        long startTime = System.nanoTime();
        for (long i = 0; i < len; i++) {

        }
        long estimatedTime = System.nanoTime() - startTime;
        //System.out.println(estimatedTime);

        return estimatedTime;
    }

    public static void main(String args[]) {
        bigLoop();

    }

    public static void bigLoop() {
        //tst
        long num = 100;
        System.out.println("Hello World!");
        for (int i = 0; i < 9; i++) {
            num *= 10;
            System.out.println(valueOf(num).concat("         ").concat(valueOf(testLoop(num))));
        }
    }


}
