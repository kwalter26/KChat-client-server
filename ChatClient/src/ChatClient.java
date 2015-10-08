import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

/**
 * Created by kyle on 9/22/15.
 */
public class ChatClient {

    private JTextField usernameTextField;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JButton connectButton;
    private JButton leaveButton;
    private JTextField messageTextField;
    private JButton sendButton;
    private JTextArea chatTextArea;
    private JLabel hostLabel;
    private JLabel portLabel;
    private JLabel usernameLabel;
    private JPanel chatClientPanel;
    private JScrollPane chatScrollPane;

    private Socket connection;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    private String host;
    private int port;
    private String hostName;
    private String userName;


    public ChatClient() {

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isValid()){
                    host = hostTextField.getText();
                    port = Integer.parseInt(portTextField.getText());
                    connect();
                }
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String message = userName + ";" + messageTextField.getText();
                    outputStream.writeUTF(message);
                    showMessage(message);
                    messageTextField.setText("");
                }catch (IOException ioe){

                }
            }
        });
    }

    private boolean isValid(){
        boolean answer = true;
        String message = "Invalid ";
        if(hostTextField.getText().equals("")){
            message += "host ";
            answer = false;
        }
        if(!portTextField.getText().equals("")){
            try{
                Integer.parseInt(portTextField.getText());
            }catch(NumberFormatException nfe){
                if(!answer){
                    message += "& ";
                }
                message += "port ";
                answer = false;
            }
        }else{
            if(!answer){
                message += "& ";
            }
            message += "port ";
            answer = false;
        }
        if(!answer) showMessage(message);
        return answer;
    }

    private void connect(){
        try{
            connection = new Socket(host,port);
            inputStream = new DataInputStream(connection.getInputStream());
            hostName = inputStream.readUTF();
            userName = usernameTextField.getText();
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.flush();
            outputStream.writeUTF(userName);
            showMessage("Connected to " + hostName);
            new ListenThread(this);
        }catch (IOException ioe){
            showMessage("Could not connect to host");
        }
    }

    private void listen(){

    }

    public void showMessage(String message){
        String[] messages = message.split(";");
        chatTextArea.append("<"+messages[0] +"> "+messages[1] + "\n");
    }

    public DataInputStream getInputStream(){
        return this.inputStream;
    }

    public String getHostName(){
        return this.hostName;
    }
    public String getUserName() { return this.userName; }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ChatClient");
        frame.setContentPane(new ChatClient().chatClientPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }




}
