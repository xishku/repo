import java.net.*;
import java.io.*;

//cmd
//javac ServerSocketPaul
//java ServerSocketPaul 1234
//learn from http://www.runoob.com/java/java-networking.html
public class ServerSocketPaul extends Thread
{
    private ServerSocket m_socket;

    public ServerSocketPaul(int port) throws IOException
    {
        m_socket = new ServerSocket(port);
        m_socket.setSoTimeout(100000);
    }

    public void run()
    {
        while(true)
        {
            try
            {
                Socket server = m_socket.accept();
                System.out.println("remote address:" + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("thanks for connecting to " + server.getLocalSocketAddress() + "\n Goodbye!\n");
                server.close();
            }catch(SocketTimeoutException s)
            {
                System.out.println("time out!");
                break;
            }catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }
    public static void main(String [] args)
    {
        int port = Integer.parseInt(args[0]);
        try
        {
            Thread t = new ServerSocketPaul(port);
            t.run();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}