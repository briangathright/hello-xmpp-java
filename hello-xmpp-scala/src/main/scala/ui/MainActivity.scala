package edu.luc.etl.cs313.scala.hello.xmpp
package ui

import android.app.Activity
import android.os.Bundle
import android.util.Log


/**
 * The main Android activity, which provides the required lifecycle methods.
 * By mixing in the reactive view behaviors, this class serves as the Adapter
 * in the Model-View-Adapter pattern. It connects the Android GUI view with the
 * reactive model.
 */
class MainActivity extends Activity {

  private def TAG = "xmpp-android-activity"

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    Log.i(TAG, "onCreate")
    // inject the (implicit) dependency on the view
    setContentView(R.layout.main)
  }

  override def onStart() {
    super.onStart()
    Log.i(TAG, "onStart")
  }
}

