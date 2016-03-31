import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;

/**
 * Created by kyle on 9/22/15.
 */
public class ServerThread extends Thread {

    private Server server;
    private Client client;
    private boolean chatting = false;
    private boolean ringing = false;
    private Client partner;

    public ServerThread(Server server, Client client){
        this.server = server;
        this.client = client;
        start();
    }

    public void run(){
        try{
            server.showMessage("server;Accepted Request from " + client.getHost());
            server.showMessage("server;Message Sent to "+client.getHost());
            server.showMessage("server;Asking For Client Name");
            String name = "server";
            client.write("Enter your name");
            name = client.read();
            while(name.equals("server")){
                client.write("Invalid Name");
                client.write("Enter your name");
                name = client.read();
            }
            client.setName(name);
            server.addClient(client);
            server.showMessage("server;" + client.getName() + " connected on " + client.getHost()+":"+client.getPort());
            client.write("Name set to " + client.getName());
            while(true){
                String message = client.read();
                handle(message);
            }
        }catch(EOFException e){
            server.showMessage("server;Connection closed by " + client.getName());
        }catch(IOException e){
            server.showMessage("server;Connection failed for " + client.getName());
        }finally {

        }
    }

    public void handle(String message) throws IOException{
        String[] messages = message.split(";");
        switch(messages[1]){
            case "list":
                client.write(server.sendList());
                break;
            case "talk":
                if(!chatting && !ringing){
                    partner = server.getPartner(messages[2]);
                    if(partner != null){
                        server.showMessage("server;Talk Request Received from " + client.getName());
                        server.showMessage("server;Sending Talk Request to " + partner.getName());
                        partner.write("request;" + client.getName() + "@" + client.getHost());
                        client.write("Ringing " + partner.getName());
                        ringing = true;
                    }else{
                        client.write("Client is busy or not found.");
                    }
                }else if(chatting){
                    client.write("Already chatting");
                }else{
                    client.write("Still ringing " + partner.getName());
                }
                break;
            case "accept":
                partner = server.getPartner(messages[2].split("@")[0]);
                chatting = true;
                server.showMessage("server;Sending Talk initiated message to " + client.getName());
                partner.write("accept;" + partner.getName());
                client.write("Talk connection established with " + partner.getName());
                break;
            case "chatting":
                chatting = true;
            default:
                if(chatting){
                    partner.write(messages[0],messages[1]);
                }else{
                    client.write("Command not found");
                }

        }
    }


}
