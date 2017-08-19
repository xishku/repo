import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleSpider{

    public static String getContentByUrl(String url) {
        BufferedReader in = null;
        String result = "";
        try
        {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
            URL realUrl = new URL(urlImg);
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(5 * 1000);
            // 输入流
            InputStream is = connection.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf = new File(path);
            if (!sf.exists()) {
                sf.mkdirs();
            }

            Calendar now = Calendar.getInstance();
            System.out.println(now.getTimeInMillis());
            OutputStream os = new FileOutputStream(sf.getPath() + "\\" + now.getTimeInMillis() + ".jpg");

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
}