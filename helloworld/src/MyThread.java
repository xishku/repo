import java.io.IOException;
import java.util.Random;

/**
 * Created by ms-sh on 2019/2/7.
 */
public class MyThread extends Thread {

    public void run()
    {
        for (int i = 0; i < 5 ; i++) {
            try {
                Thread.sleep(1000 * (new Random()).nextInt( 20));
                System.out.println(this.getName());
                System.out.println(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //System.out.println(this.getId());
        //System.out.println(this.getName());
        //System.out.println(this.getPriority());
        //System.out.println((new Random()).nextInt( 20));
    }

    public static void main(String [] args)
    {
        for (int i = 0; i < 10; i++) {
            Thread t = new MyThread();
            t.start();
        }
    }
}
