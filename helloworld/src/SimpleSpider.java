import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class SimpleSpider{

    public static String getContentByUrl(String url, String charsetStr) {
        BufferedReader in = null;
        String result = "";
        try
        {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.connect();

            String contentTypeStr = connection.getContentType();
            //System.out.println(connection.getContentEncoding());
            String charsetName = getCharsetByContentTypeStr(contentTypeStr);

            if (charsetName.isEmpty()){
                charsetName = charsetStr;
            }

            if (null != connection.getContentEncoding() && connection.getContentEncoding().equals("gzip")){
                //System.out.println("path gzip");
                in = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream()), charsetName));
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charsetName));
            }

            String line;

            while ((line = in.readLine()) != null)
            {
               result += line + "\n";
            }
        } catch (Exception e)
        {
            System.out.println("get exception！" + e);
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            } catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }

        return result;
    }

    private static String getCharsetByContentTypeStr(String contentTypeStr) {
        int pos = contentTypeStr.indexOf("charset=");
        if (pos > 0) {
            String encodingStr = contentTypeStr.substring(pos + 8);
            System.out.println(encodingStr);
            return encodingStr;
        }
        return "";
    }

    public static Set<String> filterString(String src, String filter){
        Set<String> set = new HashSet<String>();
        Pattern pattern = Pattern.compile(filter);
        Matcher matcher = pattern.matcher(src);

        while (matcher.find()){
            set.add(matcher.group(0));
        }

        return set;
    }

    public static void downloadFileToPath(String urlImg, String path){
        try {

            System.out.println("download:" + urlImg);
            URL realUrl = new URL(urlImg);
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(5 * 1000);
            InputStream is = connection.getInputStream();

            byte[] bs = new byte[1024];
            int len;
            File sf = new File(path);
            if (!sf.exists()) {
                sf.mkdirs();
            }

            OutputStream os = new FileOutputStream(sf.getPath() + "\\" + Calendar.getInstance().getTimeInMillis() + ".jpg");

            try {
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
            }
            finally {
                os.close();
                is.close();
            }

        }catch (Exception e)
            {
                System.out.println("get exception！" + e);
                e.printStackTrace();
            }
        }

    public static String getQuotedStr(String input){
        int pos = input.indexOf("http://");
        if (pos > 0)
        {
            String tempStr = input.substring(pos);
            int qpos = tempStr.indexOf("\"");
            if (qpos > 0){
                tempStr = tempStr.substring(0, qpos);
            }

            return tempStr;
        }
        else
        {
            return input;
        }

    }


    public static String uncompress(ByteArrayInputStream in,String charset) {
        try {
            GZIPInputStream gInputStream = new GZIPInputStream(in);
            byte[] by = new byte[1024];
            StringBuffer strBuffer = new StringBuffer();
            int len = 0;
            while ((len = gInputStream.read(by)) != -1) {
                strBuffer.append( new String(by, 0, len,charset) );
            }
            return strBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void downloadAutohomeImg(String url, String charsetStr) throws Exception {
        if (url.isEmpty()){
            return;
        }

        System.out.println("download:" + url);
        String result = SimpleSpider.getContentByUrl(url, charsetStr);
        Set<String> set = SimpleSpider.filterString(result, "src9=\\\"(.+?)\\\"");
        for (String str:set
                ) {
            System.out.println(str);
            SimpleSpider.downloadFileToPath(SimpleSpider.getQuotedStr(str), "E:\\img");
        }
    }

    public static void batchDownloadAutohomeImg(String url) throws Exception {
        System.out.println("process:" + url);
        String result = SimpleSpider.getContentByUrl(url, "gb2312");
        //System.out.println(result);
        Set<String> set = SimpleSpider.filterString(result, "href=\\\"(.+?)\\\"");
        for (String str:set
                ) {
            String urlFind = SimpleSpider.getQuotedStr(str);
            //System.out.println(urlFind);
            SimpleSpider.downloadAutohomeImg(urlFind, "gb2312");
        }
    }
}