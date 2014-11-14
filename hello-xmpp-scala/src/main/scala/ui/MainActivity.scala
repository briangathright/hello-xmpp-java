package edu.luc.etl.cs313.scala.hello.xmpp
package ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.{AbstractXMPPConnection, ConnectionConfiguration}
import org.jivesoftware.smack._

/**
 * The main Android activity, which provides the required lifecycle methods.
 * By mixing in the reactive view behaviors, this class serves as the Adapter
 * in the Model-View-Adapter pattern. It connects the Android GUI view with the
 * reactive model.
 */
class MainActivity extends Activity with TypedActivity {

  val HOST = "talk.google.com"
  val PORT = 5222
  val SERVICE = "gmail.com"

  val username = "awallluc@gmail.com"
  val password = "androidwall"

  private var chatmanager: ChatManager = _
  var chat2: Chat = _
  var connection: AbstractXMPPConnection = _

  private val TAG = "xmpp-android-activity" // FIXME please use this in all log messages

  private def send = findView(TR.button_send)
  private def edit = findView(TR.editText)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    Log.i(TAG, "onCreate")
    // inject the (implicit) dependency on the view
    setContentView(R.layout.main)
  }

  override def onStart() {
    super.onStart()
    Log.i(TAG, "onStart")
    connect()
  }

  def connect() {
    val t: Thread = new Thread(new Runnable {
      override def run(): Unit = {
        Log.i(TAG, "Getting config...")
        val connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE)
        Log.i(TAG, "Getting conn...")
        connection = new XMPPTCPConnection(connConfig)
        try {
          Log.i(TAG, "Connectings...")
          connection.connect()
          Log.i(TAG, "Connection succeded")
        }
        catch {
          case ex: XMPPException => {
            Log.e(TAG, "Failed to connect to " + connection.getHost)
            Log.e(TAG, ex.toString)
          }
          case e: SmackException => {
            e.printStackTrace()
          }
        }
        try {
          connection.login(username, password)
          Log.i(TAG, "logged in successfully")
        }
        catch {
          case ex: XMPPException => {
            Log.e(TAG, "Failed to log in as " + username)
            Log.e(TAG, ex.toString)
          }
          case e: SmackException.NotConnectedException => {
            e.printStackTrace()
          }
          case e: SmackException => {
            e.printStackTrace()
          }
        }
        chatmanager = ChatManager.getInstanceFor(connection)
        Log.i(TAG, "got chat manager")
        chatmanager.addChatListener(new ChatManagerListener {
          def chatCreated(chat: Chat, createdLocally: Boolean) {
            if (chat2 == null) {
              chat2 = chat
            }
            if (!createdLocally) {
              val from: String = chat2.getParticipant
              chat2.addMessageListener(new ChatMessageListener {
                override def processMessage(chat: Chat, message: Message) {
                  if (message.getBody != null) {
                    runOnUiThread(new Runnable {
                      def run() {
                        //messages.add(from + ": " + message.getBody)
                        //setListAdapter
                      }
                    })
                  }
                }
              })
            }
          }
        })
        Log.i(TAG, "attached chat listener")
      }
    })
    t.start()
  }

  def onSend(view: View) {
    val to = "briangathright@gmail.com"
    //val text: String = textMessage.getText.toString
    //Log.i("hello-xmpp-java", "Sending text " + text + " to " + to)
    if (chat2 == null) {
      chat2 = chatmanager.createChat(to, new ChatMessageListener {
        override def processMessage(chat: Chat, message: Message) {
          if (message.getBody != null) {
            val from: String = chat.getParticipant
            runOnUiThread(new Runnable {
              override def run(): Unit = {
                //messages.add(from + ": " + message.getBody)
                //setListAdapter
              }
            })
          }
        }
      })
    }
    val msg: Message = new Message(to, Message.Type.chat)
    msg.setBody("hello")
    if (connection != null) {
      try {
        connection.sendPacket(msg)
        //messages.add("You: " + text)
        //setListAdapter
      }
      catch {
        case e: SmackException.NotConnectedException => {
          e.printStackTrace()
        }
      }
    }
    //textMessage.setText("")
  }
}

