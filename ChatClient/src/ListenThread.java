import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by kyle on 9/22/15.
 */
public class ListenThread extends Thread {

    private ChatClient client;
    private Socket server;

    public ListenThread(ChatClient client) {
        this.client = client;
        this.server = server;
        start();
    }

    public void run(){
        try{
            while(true){
                String message = client.getInputStream().readUTF();
                client.showMessage(message);
            }
        }catch(EOFException e){
            client.showMessage("Connection to " + client.getHostName() + " lost");
        }catch(IOException e){
            client.showMessage("Connection to " + client.getHostName() + " failed");
        }
    }
}
