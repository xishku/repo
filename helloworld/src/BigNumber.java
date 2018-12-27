/**
 * Created by ms-sh on 2018/12/27.
 */
public class BigNumber {
    private String m_BigNumStr;

    public BigNumber(String num)
    {
        m_BigNumStr = num;
    }

    public String getNumber()
    {
        return m_BigNumStr;
    }

    //multiple this.m_BigNumStr with otherNum.m_BigNumStr
    public BigNumber MultipleBigNumer(BigNumber otherNum)
    {
        final int len = m_BigNumStr.length() + otherNum.m_BigNumStr.length();
        int rstNum[] = new int[len];
        for ( int i = 0 ; i < len; ++i )
        {
            rstNum[i] = 0;
        }


        for(int i =  m_BigNumStr.length() - 1; i >= 0 ; --i)
        {
            char leftChar = m_BigNumStr.charAt(i);
            if (leftChar <= '9' && leftChar >= '0')
            {
                for(int j = otherNum.m_BigNumStr.length() - 1; j >= 0; --j)
                {
                    char rightChar = otherNum.m_BigNumStr.charAt(j);
                    if ( rightChar <= '9' && rightChar >= '0')
                    {
                        rstNum[i + j] += (leftChar - '0') * (rightChar - '0');
                    }
                    else
                    {
                        System.out.println("error character in string ".concat(m_BigNumStr));
                        return otherNum;
                    }

                }

            }
            else
            {
                System.out.println("error character in string ".concat(m_BigNumStr));
                return this;
            }
        }

        for ( int i = len - 1 ; i > 0; --i )
        {
            System.out.println("now cal No.".concat(Integer.toString(i)));
            rstNum[i - 1] += rstNum[i] / 10;
            rstNum[i] = rstNum[i] % 10;
        }

        String rstStr = "";
        for ( int i = 0; i < len; ++i )
        {
            System.out.println(rstNum[i]);
            rstStr.concat(Integer.toString(rstNum[i]));
        }

        System.out.println(rstStr);
        return new BigNumber(rstStr);

    }

}
