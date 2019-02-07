import java.io.IOException;

/**
 * Created by ms-sh on 2019/2/7.
 */
public class MyThread extends Thread {

    public void run()
    {
        sleep(1000 * ((int)(1+Math.random()) mod 10)));
        System.out.println(this.getId());
        System.out.println(this.getName());
        System.out.println(this.getPriority());
        System.out.println(this.getThreadGroup());
    }

    public static void main(String [] args)
    {
        for (int i = 0; i < 10; i++) {
            Thread t = new MyThread();
            t.run();
        }
    }
}
