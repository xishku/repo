import java.io.IOException;
import java.util.Random;

import static java.lang.String.valueOf;

/**
 * Created by ms-sh on 2019/2/7.
 */
public class MyThread extends Thread {

    public void run()
    {

        HwTest.bigLoop();

        for (int i = 0; i < 10 ; i++) {
            try {
                //System.out.println(this.getName());
                int sleepTime = 1 * (new Random()).nextInt( 2);
                Thread.sleep(sleepTime);
                //System.out.println(valueOf(this.getPriority()).concat("        sleep:").concat(valueOf(sleepTime)).concat("        ").concat(this.getName()).concat(":").concat(valueOf(i)));
                //System.out.println(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //System.out.println(this.getId());
        System.out.println(this.getName());
        //System.out.println(this.getPriority());
        //System.out.println((new Random()).nextInt( 20));
    }

    public static void main(String [] args)
    {

        for (int i = 0; i < 2; i++) {
            Thread t = new MyThread();
            t.setPriority( 1 + (i % 9));
            t.start();
        }
    }
}
