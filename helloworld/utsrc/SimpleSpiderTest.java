import org.junit.Test;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.Set;
import static org.junit.Assert.*;
/**
 * Created by ms-sh on 2017/8/19.
 */
public class SimpleSpiderTest {
    private static final String hwuid = "abc";
    private static final String hwpswd = "def";
    private static final String proxyIP = "172.19.64.37";
    private static final int proxyPort = 8080;
    @Test
    public void getContentByUrl() throws Exception {
        String url = "https://club.autohome.com.cn/jingxuan/104";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
        SimpleSpider spider = new SimpleSpider(proxy);
        String result = spider.getContentByUrl(url, "gb2312");
        System.out.println(result);
        Set<String> set = SimpleSpider.filterString(result, "src=\"//clubsrc=\"//club(.+?)\\\"");
        for (String str:set
             ) {
            System.out.println(SimpleSpider.getQuotedStr(str));
            //SimpleSpider.downloadFileToPath(SimpleSpider.getQuotedStr(str), "F:\\img");
        }
    }

    @Test
    public void getContentByUrlWithoutProxy() throws Exception {
        String url = "https://club.autohome.com.cn/jingxuan/104";
        SimpleSpider spider = new SimpleSpider();
        String result = spider.getContentByUrl(url, "gb2312");
        //System.out.println(result);
        Set<String> set = spider.filterString(result, "src=\"//clubsrc=\"//club(.+?)\\\"");
        for (String str:set
        ) {
            System.out.println(spider.getQuotedStr(str));
            //spider.downloadFileToPath(spider.getQuotedStr(str), "E:\\img");
        }
    }

    @Test
    public void getContentIdedByUrl() throws Exception {
        String url = "http://plm-idea.huawei.com/services/idea/idea/queryIdeaOrder/rank/1/1?campaignId=3721";
        String loginPara = "actionFlag=loginAuthenticate&lang=en&loginMethod=login&loginPageType=mix&scanedFinPrint=&"
                + "uid=" + URLEncoder.encode(hwuid, "UTF-8")
                + "&password=" + hwpswd;
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
        SimpleSpiderLogin spider = new SimpleSpiderLogin(proxy, "https://login.huawei.com/login/login.do", loginPara);
        String result = spider.getContentByUrl(url, "utf-8");
        System.out.println(result);
        Set<String> set = spider.filterString(result, "src=\"//clubsrc=\"//club(.+?)\\\"");
        for (String str:set
                ) {
            System.out.println(spider.getQuotedStr(str));
            //SimpleSpider.downloadFileToPath(SimpleSpider.getQuotedStr(str), "F:\\img");
        }
    }

    @Test
    public void getContentHiByUrl() throws Exception {
        String url = "http://3ms.huawei.com/km/blogs/details/5599853?l=zh-cn";
        String loginPara = "actionFlag=loginAuthenticate&lang=en&loginMethod=login&loginPageType=mix&scanedFinPrint=&"
                + "uid=" + URLEncoder.encode(hwuid, "UTF-8")
                + "&password=" + hwpswd;
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
        SimpleSpiderLogin spider = new SimpleSpiderLogin(proxy, "https://login.huawei.com/login/login.do", loginPara);
        String result = spider.getContentByUrl(url, "utf-8");
        System.out.println(result);
        Set<String> set = spider.filterString(result, "src=\"//clubsrc=\"//club(.+?)\\\"");
        for (String str:set
                ) {
            System.out.println(spider.getQuotedStr(str));
            //SimpleSpider.downloadFileToPath(SimpleSpider.getQuotedStr(str), "F:\\img");
        }
    }


    @Test
    public void getContentXinshengdByUrl() throws Exception {
        String url = "http://support.huawei.com/carrier/navi?coltype=software#col=software&from=product&detailId=PBI1-23046337&path=PBI1-7851894/PBI1-7854702/PBI1-7275896/PBI1-21103986/PBI1-21044913";
        String loginPara = "actionFlag=loginAuthenticate&lang=en&loginMethod=login&loginPageType=mix&scanedFinPrint=&"
                + "uid=" + URLEncoder.encode(hwuid, "UTF-8")
                + "&password=" + hwpswd;
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
        SimpleSpider spider = new SimpleSpider(proxy);
        String result = spider.getContentByUrl(url, "utf-8");
        System.out.println(result);
        Set<String> set = SimpleSpider.filterString(result, "src=\"//clubsrc=\"//club(.+?)\\\"");
        for (String str:set
                ) {
            System.out.println(SimpleSpider.getQuotedStr(str));
            //SimpleSpider.downloadFileToPath(SimpleSpider.getQuotedStr(str), "F:\\img");
        }
    }


    @Test
    public void batchDownloadAutohomeImg() throws Exception {
        String url = "https://club.autohome.com.cn/jingxuan/104";
        //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
        SimpleSpider spider = new SimpleSpider();
        String result = spider.getContentByUrl(url, "gb2312");
        //System.out.println(result);
        Set<String> set = spider.filterString(result, "href=\\\"(.+?)\\\"");
        for (String str:set
                ) {
            System.out.println(str);
            String urlFind = spider.getQuotedStr(str);
            if (!urlFind.contains(":") && !urlFind.startsWith("//"))
            {
                System.out.println("inserted");
                urlFind = "https://club.autohome.com.cn".concat(urlFind);
            }
            System.out.println(urlFind);
            spider.downloadAutohomeImg(urlFind, "gb2312");
        }
    }

    @Test
    public void batchDownloadAutohomeJinxuanImg() throws Exception {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
        SimpleSpider spider = new SimpleSpider();
        String url = "http://club.autohome.com.cn/JingXuan/104/";
        for (int i = 0; i < 20 ; i++) {
            spider.batchDownloadAutohomeImg(url + (i + 1) );
        }
    }

}
