import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

/**
 * Created by kyle on 9/22/15.
 */
public class Server {

    private String host;
    private int port;
    private ServerSocket serverSocket;
    private String serverName = "Kali-Chat";
    private Hashtable douts = new Hashtable();

    public Server(String host, String port) {

        this.host = host;
        this.port = Integer.parseInt(port);


        try{
            this.serverSocket = new ServerSocket(this.port);
            showMessage("server","Connection initiated on " + this.serverSocket.getLocalSocketAddress());
        }catch(IOException e){
            showMessage("server","Initial connection failed");
            System.exit(1);
        }
        listen();
    }

    private void listen(){
        while(true){
            try{
                Socket client = serverSocket.accept();
                DataOutputStream dout = new DataOutputStream(client.getOutputStream());
                dout.writeUTF(serverName);
                dout.writeUTF("Welcome to Kali-Chat");
                douts.put(client,dout);
                new ServerThread(this,client);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getServerName(){
        return this.serverName;
    }

    Enumeration getOutputStreams() {
        return douts.elements();
    }

    public void messageAll(String message) {

        synchronized (douts) {
            for (Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {
                DataOutputStream dout = (DataOutputStream)e.nextElement();
                try {
                    dout.writeUTF(message);
                } catch( IOException ie ) { System.out.println( ie ); }
            }
        }
    }

    public void showMessage(String userName,String message){
        System.out.println("<" + userName + "> " + message);
    }

    public static void main(String[] args){
        new Server(args[0],args[1]);
    }

}
