package edu.luc.etl.cs313.scala.hello.xmpp
package ui

import java.io.{InputStreamReader, BufferedReader}

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.{EditText, Button}
import model.XMPPClient


/**
 * The main Android activity, which provides the required lifecycle methods.
 * By mixing in the reactive view behaviors, this class serves as the Adapter
 * in the Model-View-Adapter pattern. It connects the Android GUI view with the
 * reactive model.
 */
class MainActivity extends Activity {

  private def TAG = "xmpp-android-activity"
  private val send : Button = findViewById(R.id.button_send).asInstanceOf[Button]
  private val edit : EditText = findViewById(R.id.editText).asInstanceOf[EditText]
  private val client: XMPPClient = new XMPPClient

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    Log.i(TAG, "onCreate")
    // inject the (implicit) dependency on the view
    setContentView(R.layout.main)
    val userName: String = "awallluc"
    val password: String = "androidwall"
    client.login(userName, password)
  }

  override def onStart() {
    super.onStart()
    Log.i(TAG, "onStart")
  }

  def onSend(): Unit = {
     val msg : String = edit.getText.asInstanceOf[String]
     client.sendMessage(msg,"briangathright@gmail.com")
  }
}

