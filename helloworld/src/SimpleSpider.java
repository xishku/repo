import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
}