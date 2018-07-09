import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class SimpleSpider{

    public static String getContentByUrl(String loginUrl, String loginPara, String url, String charsetStr) {
        BufferedReader in = null;
        String result = "";
        try
        {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.19.64.37", 8080));
            HttpURLConnection connection = getLoginedConnection(loginUrl, loginPara, url, proxy);

            connection.connect();

            System.out.println(connection.getURL().toString());
            String contentTypeStr = connection.getContentType();
            System.out.println(connection.getContentEncoding());
            String charsetName = getCharsetByContentTypeStr(contentTypeStr);


            if (charsetName.isEmpty()){
                charsetName = charsetStr;
            }

            if (null != connection.getContentEncoding() && connection.getContentEncoding().equals("gzip")){
                System.out.println("path gzip");
                in = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream()), charsetName));
            }
            else
            {
                System.out.println("not zip");
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
            System.out.println("finally");
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

    private static HttpURLConnection getLoginedConnection(String loginUrl, String loginPara, String targetUrl, Proxy proxy) throws IOException {
        HttpURLConnection connection;

        if (loginUrl.isEmpty()){
            URL realUrl = new URL(targetUrl);
            connection = (HttpURLConnection)realUrl.openConnection(proxy);
            return connection;
        }

        URL logUrl = new URL(loginUrl);
        connection = (HttpURLConnection)logUrl.openConnection(proxy);
        connection.setFollowRedirects(false);
        connection.setInstanceFollowRedirects(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (compatible; MSIE 6.0; Windows NT)");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        PrintStream send = new PrintStream(connection.getOutputStream());
        send.print(loginPara);
        send.close();

        String cookies = getCookies(connection);
        System.out.println(cookies);
        connection.disconnect();

        URL tarURL = new URL(targetUrl);
        connection = (HttpURLConnection) tarURL.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT)");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        connection.setRequestProperty("Cookie", cookies);
        connection.setDoInput(true);

        return connection;
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
        int pos = input.indexOf("\"");
        String tmpStr = input.substring(pos);
        return tmpStr.replaceAll("\"", "");
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

        System.out.println("downloadAutohomeImg:" + url);
        String result = SimpleSpider.getContentByUrl("","", url, charsetStr);
        Set<String> set = SimpleSpider.filterString(result, "src9=\\\"(.+?)\\\"");
        for (String str:set
                ) {
            System.out.println(str);
            SimpleSpider.downloadFileToPath(SimpleSpider.getQuotedStr(str), "F:\\img");
        }
    }

    public static void batchDownloadAutohomeImg(String url) throws Exception {
        System.out.println("process:" + url);
        String result = SimpleSpider.getContentByUrl("", "", url, "gb2312");
        //System.out.println(result);
        Set<String> set = SimpleSpider.filterString(result, "href=\\\"(.+?)\\\"");
        for (String str:set
                ) {
            String urlFind = SimpleSpider.getQuotedStr(str);
            //System.out.println(urlFind);
            SimpleSpider.downloadAutohomeImg(urlFind, "gb2312");
        }
    }

    public static String getCookies(HttpURLConnection conn) {
        StringBuffer cookies = new StringBuffer();
        String headName;
        for (int i = 7; (headName = conn.getHeaderField(i)) != null; i++) {
            StringTokenizer st = new StringTokenizer(headName, "; ");
            while (st.hasMoreTokens()) {
                cookies.append(st.nextToken() + "; ");
            }
        }
        return cookies.toString();
    }
}
