/**
 * Created by ms-sh on 2017/8/19.
 */
public class HanoiTower {

    public void printHanoidTowerSteps(int lvl, String src, String dst, String other)
    {
        if (1 == lvl) {
            System.out.println("mov " + lvl + " " + src + "->" + dst);
        }
        else
        {
            printHanoidTowerSteps(lvl - 1, src, other, dst);
            System.out.println("mov " + lvl + " " + src + "->" + dst);
            printHanoidTowerSteps(lvl - 1, other, dst, src);
        }
    }
}
