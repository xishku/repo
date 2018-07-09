import org.junit.Test;

import java.net.URLEncoder;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by ms-sh on 2017/8/19.
 */
public class SimpleSpiderTest {
    @Test
    public void getContentByUrl() throws Exception {
        String url = "https://club.autohome.com.cn/bbs/thread/60fb0c86b7537e0e/73605236-1.html#pvareaid=102410";
        String result = SimpleSpider.getContentByUrl("", "", url, "gb2312");
        System.out.println(result);
        Set<String> set = SimpleSpider.filterString(result, "src=\"//clubsrc=\"//club(.+?)\\\"");
        for (String str:set
             ) {
            System.out.println(SimpleSpider.getQuotedStr(str));
            //SimpleSpider.downloadFileToPath(SimpleSpider.getQuotedStr(str), "F:\\img");
        }
    }

    @Test
    public void getContentIdedByUrl() throws Exception {
        String url = "http://plm-idea.huawei.com/services/idea/idea/queryIdeaOrder/rank/400/1?themeIds=589&campaignId=2341";
        String loginPara = "actionFlag=loginAuthenticate&lang=en&loginMethod=login&loginPageType=mix&scanedFinPrint=&"
                + "uid=" + URLEncoder.encode("xxxx", "UTF-8")
                + "&password=" + "xxxxx";
        String result = SimpleSpider.getContentByUrl("https://login.huawei.com/login/login.do", loginPara, url, "utf-8");
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
        String result = SimpleSpider.getContentByUrl("", "", url, "gb2312");
        //System.out.println(result);
        Set<String> set = SimpleSpider.filterString(result, "href=\\\"(.+?)\\\"");
        for (String str:set
                ) {
            System.out.println(str);
            String urlFind = SimpleSpider.getQuotedStr(str);
            if (!urlFind.contains(":") && !urlFind.startsWith("//"))
            {
                urlFind = "https://club.autohome.com.cn".concat(urlFind);
            }
            System.out.println(urlFind);
            SimpleSpider.downloadAutohomeImg(urlFind, "gb2312");
        }
    }

    @Test
    public void batchDownloadAutohomeJinxuanImg() throws Exception {
        String url = "http://club.autohome.com.cn/JingXuan/104/";
        for (int i = 0; i < 20 ; i++) {
            SimpleSpider.batchDownloadAutohomeImg(url + (i + 1) );
        }
    }

}
