package edu.luc.hello_xmpp_java.app;

import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;

import android.widget.TextView;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    public static final String HOST = "talk.google.com";
    public static final int PORT = 5222;
    public static final String SERVICE = "gmail.com";

    public static String username;
    public static String password;

    private AbstractXMPPConnection connection;

    private EditText usernameET;
    private EditText passwordET;
    private EditText recipient;
    private EditText textMessage;
    private TextView messagesTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        usernameET = (EditText) this.findViewById(R.id.usernameET);
        passwordET = (EditText) this.findViewById(R.id.passwordET);
    }

    public void onLogin(View view) {
        username = usernameET.getText().toString();
        password = passwordET.getText().toString();

        connect();

        setContentView(R.layout.activity_main);

        messagesTV = (TextView) this.findViewById(R.id.messagesTV);
        textMessage = (EditText) this.findViewById(R.id.chatBoxET);
        recipient = (EditText) this.findViewById(R.id.recipientET);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            connection.disconnect();
        } catch (Exception e) {

        }
    }


    public void connect() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE);
                connection = new XMPPTCPConnection(connConfig);
                try {
                    connection.connect();
                } catch (XMPPException ex) {
                    Log.e("Error",  "Failed to connect to " + connection.getHost());
                    Log.e("Error", ex.toString());
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    connection.login(username, password);
                } catch (XMPPException ex) {
                    Log.e("Error", "Failed to log in as " +  username);
                    Log.e("Error", ex.toString());
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


    public void onSend(View view) {
        ChatManager chatmanager = ChatManager.getInstanceFor(connection);

        Chat newChat = chatmanager.createChat(recipient.getText().toString(), new MessageListener() {
            @Override
            public void processMessage(Chat chat, final Message message) {
                if (message.getBody() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messagesTV.setText(message.getBody());
                        }
                    });
                }
            }
        });

        String to = recipient.getText().toString();
        String text = textMessage.getText().toString();
        Log.i("hello-xmpp-java", "Sending text " + text + " to " + to);

        Message msg = new Message(to, Message.Type.chat);
        msg.setBody(text);
        if (connection != null) {
            try {
                connection.sendPacket(msg);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
        textMessage.setText("");
    }
}
