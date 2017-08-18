import static jdk.nashorn.internal.objects.Global.println;

/**
 * Created by ms-sh on 2017/8/11.
 */
public class calculatorExample {
    public int sumab( int a, int b)
    {
        return a + b;
    }

    public int print2(int lvl)
    {
        long tempNum = 1;
        for (int i = 0; i < lvl ; ++i){

            System.out.println( i + ":"  + tempNum);
            tempNum *= 2;
        }

        return  1;
    }
}
