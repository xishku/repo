import java.util.Set;

public class PageHtmlHelper {
    private String myHtml;

    public PageHtmlHelper(String src)
    {
        myHtml = src;
    }

    public void showPageContent()
    {
        System.out.println(this.getTitle());
        System.out.println(this.getTotalPrice());
        System.out.println(this.getArea());
        System.out.println(this.getPrice());
    }

    public String getTitle(){
        Set<String> set = RegularUtils.filterString(myHtml, "<title>(.+?)</title>");
        String titleStr = "";
        for (String str:set
        ) {
            System.out.println(str);
            titleStr = str;
            break;
        }

        return titleStr;
    }

    public String getArea(){
        return getValueByKey("area");
    }

    public String getPrice(){
        return getValueByKey("price");
    }

    public String getTotalPrice(){
        return getValueByKey("totalPrice");
    }

    public String getValueByKey(String KeyWord){
        Set<String> set = RegularUtils.filterString(myHtml, KeyWord.concat(":(.+?),"));
        String dataStr = "";
        for (String str:set
        ) {
            //System.out.println(str);
            dataStr = str;
            break;
        }

        return getQuotedData(dataStr);
    }

    public static String getQuotedData(String src)
    {
        //System.out.println(src);

        int pos = src.indexOf("'");
        String tmpStr;
        if (pos > 0) {
            int len = src.length();

            tmpStr = src.substring(pos + 1, len - 1);
        }
        else
        {
            tmpStr = src;
            return tmpStr;
        }

        String newStr = tmpStr;
        //System.out.println(newStr);
        pos = newStr.indexOf("'");
        if (pos > 0) {
            tmpStr = newStr.substring(0, pos);
        }
        else
        {
            tmpStr = newStr;
        }

        return tmpStr;
    }

}
