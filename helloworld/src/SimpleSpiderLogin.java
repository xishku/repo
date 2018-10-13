import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
public class SimpleSpiderLogin extends SimpleSpider {

    private String m_loginUrl;
    private String m_loginPara;
    public SimpleSpiderLogin(Proxy proxy, String loginUrl, String loginPara)
    {
        super(proxy);
        m_loginUrl = loginUrl;
        m_loginPara = loginPara;
    }

    public SimpleSpiderLogin(String loginUrl, String loginPara)
    {
        super();
        m_loginUrl = loginUrl;
        m_loginPara = loginPara;
    }

    protected HttpURLConnection getUrlConnection(String targetUrl) throws IOException {
        HttpURLConnection connection;
        if (m_loginUrl.isEmpty()){
            return super.getUrlConnection(targetUrl);
        }

        URL logUrl = new URL(m_loginUrl);
        connection = (HttpURLConnection)logUrl.openConnection(getProxy());
        connection.setFollowRedirects(false);
        connection.setInstanceFollowRedirects(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (compatible; MSIE 6.0; Windows NT)");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        PrintStream send = new PrintStream(connection.getOutputStream());
        send.print(m_loginPara);
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

}
