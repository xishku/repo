import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by ms-sh on 2017/8/19.
 */
public class SimpleSpiderTest {
    @Test
    public void getContentByUrl() throws Exception {
        String url = "http://www.huawei.com/";
        String result = SimpleSpider.getContentByUrl(url);
        //System.out.println(result);
        Set<String> set = SimpleSpider.filterString(result, "data-big-img=\\\"(.+?)\\\"");
        for (String str:set
             ) {
            SimpleSpider.downloadFileToPath(SimpleSpider.getQuotedStr(str), "E:\\img");
        }
    }



    @Test
    public void batchDownloadImg() throws Exception {
        String url = "http://club.autohome.com.cn/jingxuan/104";
        String result = SimpleSpider.getContentByUrl(url);
        System.out.println(result);
        Set<String> set = SimpleSpider.filterString(result, "href=\\\"(.+?)\\\"");
        for (String str:set
                ) {
            String urlFind = SimpleSpider.getQuotedStr(str);
            SimpleSpider.downloadAutohomeImg(urlFind);
        }
    }

}