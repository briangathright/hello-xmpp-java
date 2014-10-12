package edu.luc.etl.cs313.scala.hello.xmpp
package ui

import java.io.{InputStreamReader, BufferedReader}
import java.util

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.{EditText, Button}
import model.XMPPClient
import org.jivesoftware.smack.SmackException.ConnectionException
import org.jivesoftware.smack.android.AndroidSmackInitializer
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.util.dns.HostAddress
import org.jivesoftware.smack.{AbstractXMPPConnection, ConnectionConfiguration}


/**
 * The main Android activity, which provides the required lifecycle methods.
 * By mixing in the reactive view behaviors, this class serves as the Adapter
 * in the Model-View-Adapter pattern. It connects the Android GUI view with the
 * reactive model.
 */
class MainActivity extends Activity with TypedActivity {

  private def TAG = "xmpp-android-activity"
  private def send = findView(TR.button_send)
  private def edit = findView(TR.editText)
  private val client: XMPPClient = new XMPPClient

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    Log.i(TAG, "onCreate")
    // inject the (implicit) dependency on the view
    setContentView(R.layout.main)
    connect()
  }

  override def onStart() {
    super.onStart()
    Log.i(TAG, "onStart")
  }

  def connect() {
    val t : Thread = new Thread(new Runnable {
      override def run(): Unit = {
        val userName: String = "awallluc@gmail.com"
        val password: String = "androidwall"
        try {
          client.login(userName, password)
        }
        catch {
          case ex: ConnectionException => {
            val hosts : util.List[HostAddress] = ex.getFailedAddresses
            Log.e("Error", hosts.listIterator().next().getException.toString)
          }
        }
      }

    })
    t.start()
  }

  def onSend(view : View) {
     val msg : String = edit.getText.toString
     client.sendMessage(msg,"briangathright@gmail.com")
  }
}

