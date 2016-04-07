import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by kyle on 9/22/15.
 */

public class ListenThread extends Thread {

    private Client client;
    private DataInputStream input;

    public ListenThread(Client client,DataInputStream input) {
        this.client = client;
        this.input = input;
        start();
    }

    public void run(){
        try{
            while(true){
                String message = input.readUTF();
                String[] messages = message.split(";");
                if(messages[0].equals("server")){
                    switch(messages[1]){
                        case "request":
                            client.show("server;Talk request from " + messages[2] + ". Respond with \"accept " + messages[2] + "\"");
                            break;
                        case "accept":
                            client.show("server;Talk connection established with " + messages[2]);
                            client.write("chatting","server");
                            break;
                        case "chatting":
                            break;
                        default:
                            client.show(message);
                    }
                }else{
                    client.show(message);
                }

            }
        }catch(EOFException e){
            client.show("client;Connection lost");
        }catch(IOException e){
            client.show("client;Connection failed");
        }
    }


}
