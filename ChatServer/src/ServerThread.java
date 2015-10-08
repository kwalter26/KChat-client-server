import java.io.*;
import java.net.Socket;

/**
 * Created by kyle on 9/22/15.
 */
public class ServerThread extends Thread {

    private Server server;
    private Socket client;

    public ServerThread(Server server, Socket client){
        this.server = server;
        this.client = client;
        start();
    }

    public void run(){
        DataInputStream din;
        String userName = "";
        try{
            din = new DataInputStream(client.getInputStream());
            userName = din.readUTF();
            server.showMessage("server;" + userName + " connected on " + client.getLocalSocketAddress());
            while(true){
                String message = din.readUTF();
                server.showMessage(message);
                server.messageAll(message);
            }
        }catch(EOFException e){
            server.showMessage("server;Connection closed by " + userName);
        }catch(IOException e){
            server.showMessage("server;Connection failed");
        }finally {

        }
    }

}
