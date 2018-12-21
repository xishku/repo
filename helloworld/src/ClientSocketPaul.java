import java.net.*;
import java.io.*;

import static java.lang.Thread.sleep;


//cmd:
//javac ClientSocketPual.java
//java ClientSocketPaul localhost 1234
//learn from http://www.runoob.com/java/java-networking.html
public class ClientSocketPaul
{
    public static void main(String [] args)
    {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        try
        {
            while(true) {
                System.out.println("connect to host: " + serverName + ", port: " + port);
                Socket client = new Socket(serverName, port);
                System.out.println("remote address: " + client.getRemoteSocketAddress());
                OutputStream outToServer = client.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);

                out.writeUTF("Hello from " + client.getLocalSocketAddress());
                InputStream inFromServer = client.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);
                System.out.println("response from server: " + in.readUTF());
                client.close();

                sleep(3000);
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}