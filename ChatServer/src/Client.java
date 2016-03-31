import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Kyle Walter on 3/30/2016.
 */
public class Client {

    private String name;
    private Socket client;
    private DataInputStream input;
    private DataOutputStream output;
    private int port;
    private String host;
    private boolean busy = false;

    public Client(Socket client){
        this.client = client;
        try{
            input = new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());
            port = client.getPort();
            host = client.getInetAddress().toString();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void write(String message) throws IOException{
        output.writeUTF("server;"+message);
    }

    public void write(String name,String message) throws IOException{
        output.writeUTF(name+";"+message);
    }

    public String read()throws IOException{
        return input.readUTF();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getPort(){
        return port;
    }

    public String getHost(){
        return host;
    }

    public boolean isBusy(){
        return busy;
    }

}
