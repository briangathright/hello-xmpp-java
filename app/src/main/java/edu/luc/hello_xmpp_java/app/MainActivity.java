package edu.luc.hello_xmpp_java.app;

import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
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

    private ChatManager chatmanager;
    public Chat chat2 = null;
    private AbstractXMPPConnection connection;

    private ArrayList<String> messages = new ArrayList<String>();

    private EditText usernameET;
    private EditText passwordET;
    private EditText recipient;
    private EditText textMessage;
    private ListView listview;

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

        listview = (ListView) this.findViewById(R.id.listMessages);
        setListAdapter();
        textMessage = (EditText) this.findViewById(R.id.chatBoxET);
        recipient = (EditText) this.findViewById(R.id.recipientET);
    }

    private void setListAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listitem, messages);
        listview.setAdapter(adapter);
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
                chatmanager = ChatManager.getInstanceFor(connection);
                chatmanager.addChatListener(new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        if (chat2 == null) {
                            chat2 = chat;
                        }
                        if (!createdLocally) {
                            final String from = chat2.getParticipant();
                            chat2.addMessageListener(new ChatMessageListener() {
                                @Override
                                public void processMessage(Chat chat, final Message message) {
                                    if (message.getBody() != null) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                messages.add(from + ": " + message.getBody());
                                                setListAdapter();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        t.start();
    }


    public void onSend(View view) {

        String to = recipient.getText().toString();
        String text = textMessage.getText().toString();
        Log.i("hello-xmpp-java", "Sending text " + text + " to " + to);

        if(chat2 == null) {
            chat2 = chatmanager.createChat(to, new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, final Message message) {
                    if (message.getBody() != null) {
                        final String from = chat.getParticipant();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messages.add(from + ": " + message.getBody());
                                setListAdapter();
                            }
                        });
                    }
                }
            });
        }

        Message msg = new Message(to, Message.Type.chat);
        msg.setBody(text);
        if (connection != null) {
            try {
                connection.sendPacket(msg);
                messages.add("You: " + text);
                setListAdapter();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
        textMessage.setText("");
    }
}
