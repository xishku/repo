import sun.font.TrueTypeFont;

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
        final String revLeftNumStr = (new StringBuilder(m_BigNumStr)).reverse().toString();
        final String revRightNumStr = (new StringBuilder(otherNum.m_BigNumStr)).reverse().toString();
        int rstNum[] = new int[len];
        for ( int i = 0 ; i < len; ++i )
        {
            rstNum[i] = 0;
        }


        for(int i = 0; i <  revLeftNumStr.length(); ++i)
        {
            char leftChar = revLeftNumStr.charAt(i);
            if (leftChar <= '9' && leftChar >= '0')
            {
                for(int j = 0; j < revRightNumStr.length(); ++j)
                {
                    char rightChar = revRightNumStr.charAt(j);
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

        for ( int i = 0; i < len - 1 ; ++i )
        {
            System.out.println("now cal No.".concat(Integer.toString(i)));
            rstNum[i + 1] += rstNum[i] / 10;
            rstNum[i] = rstNum[i] % 10;
        }


        String rstStr = "";
        for ( int i = len - 1; i >= 0; --i )
        {
            System.out.println(rstNum[i]);
            rstStr = rstStr.concat(Integer.toString(rstNum[i]));
        }

        while (!rstStr.isEmpty())
        {
            if (rstStr.startsWith("0"))
            {
                rstStr = rstStr.substring(1, rstStr.length());
            }
            else
            {
                break;
            }
        }

        if (rstStr.isEmpty())
        {
            rstStr = "0";
        }

        System.out.println(rstStr);
        return new BigNumber(rstStr);

    }

}
