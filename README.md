# TestingGrounds

This is where the java implementation of Smack currently resides. Right now you can log in as "awallluc@gmail.com" with password "androidwall" and choose your gmail as the chat partner. Currently, it can talk to google talk, but two of this client can't talk to eachother (not sure why). Google Talk partner can also respond back. 

You will notice when you login that tons of red text and warnings will pop up (this is what I was telling you about). They are not fatal, but they are very annoying and I haven't figured out how to suppress them. Basically Google Talk sends back packets that don't conform to Smack or XMPP so its telling us this, but we can still talk to it. 

# hello-xmpp-scala 

Scala implementation of Smack 4.1 on Android

Status: Issues with Proguard not allowing dynamic assignment of resources, can't proceed.

# hello-xmpp-java

Java implementation of Smack 4.1 on Android

Status: Can receive and send messages to other emulators and pc's. Will fluff out message printing so it shows more than just last message. 

