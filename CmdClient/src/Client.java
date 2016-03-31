import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Kyle Walter on 3/30/2016.
 */
public class Client {

    private String host;
    private int port;
    private Socket connection;
    private ListenThread listenThread;
    private Scanner sc;
    private DataInputStream input;
    private DataOutputStream output;
    private String name;

    public Client(String host,int port){
        this.host = host;
        this.port = port;
        this.sc = new Scanner(System.in);
        name = "";
        try{
            Socket connection = new Socket(host,port);
            input = new DataInputStream(connection.getInputStream());
            output = new DataOutputStream(connection.getOutputStream());
            show(input.readUTF());
            name = sc.nextLine();
            output.writeUTF(name);
            show(input.readUTF());
        }catch(IOException e){
            e.printStackTrace();
        }
        listenThread = new ListenThread(this,input);
        run();
    }

    public void run(){
        try{
            while(true){
                write(sc.nextLine());
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void write(String message) throws IOException{
        write(message,name);
    }

    public void write(String message,String user) throws IOException{
        if(message.length() > 0) {
            String[] messages = message.split(" ");
            if(messages.length > 1){
                output.writeUTF(user + ";" + messages[0] + ";" + messages[1]);
            }else{
                output.writeUTF(user + ";" + message);
            }
            if(!messages[1].equals("chatting")) show(user + ";" + message);
        }else {
            show(user + ";No Message");
        }
    }

    public void show(String message){
        String[] messages = message.split(";");
        System.out.println("<"+messages[0]+"> "+messages[1]);
    }

    public static void main(String[] args){
        new Client("localhost",8123);
    }



}
