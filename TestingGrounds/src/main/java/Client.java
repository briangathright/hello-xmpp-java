import java.util.*;
import java.io.*;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;


public class Client implements MessageListener
{
    AbstractXMPPConnection connection;
    ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");

    public static void main(String args[]) throws XMPPException, IOException, SmackException {

        Client c = new Client();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String msg;


        System.out.println("Enter Account Information");
        System.out.print("User Name: ");
        String userName = br.readLine();
        System.out.print("Password: ");
        String password = br.readLine();

        c.login(userName, password);

        System.out.print("Conversation Partner's User Name: ");

        String partner = br.readLine();

        while( !(msg=br.readLine()).equals("exit")) {
            c.sendMessage(msg, partner);
        }

        c.disconnect();
        System.exit(0);
    }

    //login and start connection
    public void login(String userName, String password) throws XMPPException, IOException, SmackException {

        //create connection based off the configuration given here (we assume we will be using google talk)
        connection = new XMPPTCPConnection(config);

        //start the connection and login in with given account
        connection.connect();
        connection.login(userName, password);
    }

    //gets the ChatManager for the current connection and sends the message
    public void sendMessage(String message, String to) throws XMPPException, SmackException.NotConnectedException {
        Chat chat = ChatManager.getInstanceFor(connection).createChat(to, this);
        chat.sendMessage(message);
    }

    //terminate the current connection
    public void disconnect() throws SmackException.NotConnectedException {
        connection.disconnect();
    }

    //this function fires when we receive messages
    public void processMessage(Chat chat, Message message)
    {
        boolean isChat = (message.getType() == Message.Type.chat);
        if (isChat) {
            //GTalk likes to randomly send null messages with the real message not sure why
            // so we filter them out
            if (message.getBody() != null) {
                System.out.println(chat.getParticipant() + ": " + message.getBody());
            }
        }
    }
}
