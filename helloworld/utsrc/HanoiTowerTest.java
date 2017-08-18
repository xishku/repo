import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ms-sh on 2017/8/19.
 */
public class HanoiTowerTest {
    @Test
    public void printHanoidTowerSteps() throws Exception {
        HanoiTower ht = new HanoiTower();
        ht.printHanoidTowerSteps(5,"A", "B", "C");
    }

}