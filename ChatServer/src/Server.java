

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by kyle on 9/22/15.
 */
public class Server {

    private String host;
    private int port;
    private ServerSocket serverSocket;
    private String serverName = "Kali-Chat";
    private ArrayList<Client> clients;

    public Server(String host, String port) {

        this.host = host;
        this.port = Integer.parseInt(port);
        this.clients = new ArrayList();

        try{
            this.serverSocket = new ServerSocket(this.port);
            showMessage("server;Server Listening on port " + this.serverSocket.getLocalPort());
        }catch(IOException e){
            showMessage("server;Initial connection failed");
            System.exit(1);
        }
        listen();
    }

    private void listen(){
        while(true){
            try{
                Client newClient = new Client(serverSocket.accept());
                new ServerThread(this,newClient);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

//    public String getServerName(){
//        return this.serverName;
//    }

//    Enumeration getOutputStreams() {
//        return douts.elements();
//    }

//    public void messageAll(String message) {
//        synchronized (douts) {
//            for (Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {
//                DataOutputStream dout = (DataOutputStream)e.nextElement();
//                try {
//                    dout.writeUTF(message);
//                } catch( IOException ie ) { System.out.println( ie ); }
//            }
//        }
//    }

    public void showMessage(String message){
        String[] messages = message.split(";");
        System.out.println("<" + messages[0] + "> " + messages[1]);
    }

    public void addClient(Client client){
        clients.add(client);
    }

    public Client getPartner(String name){
        for(int i = 0; i < clients.size();i++) {
           if(clients.get(i).getName().equals(name) && !clients.get(i).isBusy()){
                return clients.get(i);
           }
        }
        return null;
    }

    public String sendList(){
        String message = "Server List";
        for(int i = 0; i < clients.size();i++) {
            message += ("\n<"+i+"> "+clients.get(i).getName());
        }


        return message;
    }

    public static void main(String[] args){
        new Server(args[0],args[1]);
    }

}
