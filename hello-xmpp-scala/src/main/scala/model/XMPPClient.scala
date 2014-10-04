package model

import java.io.{InputStreamReader, BufferedReader}

import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack._

object XMPPClient extends App {

    val c: XMPPClient = new XMPPClient
    val br: BufferedReader = new BufferedReader(new InputStreamReader(System.in))
    var msg: String = null
    System.out.println("Enter Account Information")
    System.out.print("User Name: ")
    val userName: String = br.readLine
    System.out.print("Password: ")
    val password: String = br.readLine
    c.login(userName, password)
    System.out.print("Conversation Partner's User Name: ")
    val partner: String = br.readLine
    while (!({
      msg = br.readLine; msg
    } == "exit")) {
      c.sendMessage(msg, partner)
    }
    c.disconnect()
    System.exit(0)

  class XMPPClient extends MessageListener {
    var connection: AbstractXMPPConnection = null
    var config: ConnectionConfiguration = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com")


    def login(userName: String, password: String) {
      connection = new XMPPTCPConnection(config)
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

}