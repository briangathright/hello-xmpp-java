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

  final val HOST: String = "talk.google.com"
  final val PORT: Int = 5222
  final val SERVICE: String = "gmail.com"

  val username: String = "awallluc@gmail.com"
  val password: String = "androidwall"
  private var chatmanager: ChatManager = null
  var chat2: Chat = null
  var connection: AbstractXMPPConnection = null

  private def TAG = "xmpp-android-activity"
  private def send = findView(TR.button_send)
  private def edit = findView(TR.editText)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    Log.i(TAG, "onCreate")
    // inject the (implicit) dependency on the view
    setContentView(R.layout.main)

    val connConfig: ConnectionConfiguration = new ConnectionConfiguration(HOST, PORT, SERVICE)
    connection = new XMPPTCPConnection(connConfig)

    connect()
  }


  override def onStart() {
    super.onStart()
    Log.i(TAG, "onStart")
  }

  def connect() {
    val t: Thread = new Thread(new Runnable {
      override def run(): Unit = {
        val connConfig: ConnectionConfiguration = new ConnectionConfiguration(HOST, PORT, SERVICE)
        connection = new XMPPTCPConnection(connConfig)
        try {
          Log.i("Connecting...", "Connectings...")
          connection.connect()
        }
        catch {
          case ex: XMPPException => {
            Log.e("Error", "Failed to connect to " + connection.getHost)
            Log.e("Error", ex.toString)
          }
          case e: SmackException => {
            e.printStackTrace()
          }
        }
        try {
          connection.login(username, password)
        }
        catch {
          case ex: XMPPException => {
            Log.e("Error", "Failed to log in as " + username)
            Log.e("Error", ex.toString)
          }
          case e: SmackException.NotConnectedException => {
            e.printStackTrace()
          }
          case e: SmackException => {
            e.printStackTrace()
          }
        }
        chatmanager = ChatManager.getInstanceFor(connection)
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
      }
    })
    t.start()
  }

  def onSend(view: View) {
    val to: String = "briangathright@gmail.com"
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

