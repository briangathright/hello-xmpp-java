package model

import java.io.{InputStreamReader, BufferedReader}
import javax.net.ssl.HostnameVerifier

import org.apache.http.conn.ssl.SSLSocketFactory
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.sasl.SASLMechanism
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack._

class XMPPClient extends MessageListener {

    var config: ConnectionConfiguration = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com")
    var connection: AbstractXMPPConnection = new XMPPTCPConnection(config)

    def login(userName: String, password: String) {
      //connection = new XMPPTCPConnection(config)
      config.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)

      connection.connect()
      connection.login(userName, password)
    }


    def sendMessage(message: String, to: String) {
      val chat: Chat = ChatManager.getInstanceFor(connection).createChat(to, this)
      chat.sendMessage(message)
    }

    def disconnect() {
      connection.disconnect()
    }

    def processMessage(chat: Chat, message: Message) {
      val isChat: Boolean = message.getType eq Message.Type.chat
      if (isChat) {
        if (message.getBody != null) {
          System.out.println(chat.getParticipant + ": " + message.getBody)
        }
      }
    }
  }
