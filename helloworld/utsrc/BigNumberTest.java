import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ms-sh on 2018/12/27.
 */
public class BigNumberTest {
    @Test
    public void multipleBigNumer() throws Exception {
        BigNumber num1 = new BigNumber("999999999999999999");
        BigNumber num2 = new BigNumber("999999999999999999");
        BigNumber num3 = num1.MultipleBigNumer(num2);
        System.out.println(num3.getNumber());
        assertEquals("999999999999999998000000000000000001", num3.getNumber());
        assertEquals("0",(new BigNumber("0")).MultipleBigNumer(new BigNumber("100")).getNumber());
        assertEquals("9801",(new BigNumber("99")).MultipleBigNumer(new BigNumber("99")).getNumber());

        System.out.println((new BigNumber("256")).MultipleBigNumer(new BigNumber("256")).getNumber());
    }

}