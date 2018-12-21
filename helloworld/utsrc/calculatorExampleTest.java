

import static org.junit.Assert.assertEquals;

/**
 * Created by ms-sh on 2017/8/11.
 */
public class calculatorExampleTest {
    @org.junit.Test
    public void sumab() throws Exception {
        calculatorExample ce = new calculatorExample();
        assertEquals(2, ce.sumab(1, 1));
        //ce.print2(109);
    }
}