import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ms-sh on 2018/12/27.
 */
public class BigNumberTest {
    @Test
    public void multipleBigNumer() throws Exception {
        BigNumber num1 = new BigNumber("11");
        BigNumber num2 = new BigNumber("11");
        BigNumber num3 = num1.MultipleBigNumer(num2);
        System.out.println(num3.getNumber());
    }

}