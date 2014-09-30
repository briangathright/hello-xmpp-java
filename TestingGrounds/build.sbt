name := "TestingGrounds"

version := "1.0"

libraryDependencies ++= Seq(
  //"jivesoftware" % "smack" % "3.0.4",
  //"jivesoftware" % "smackx" % "3.0.4"
  "org.igniterealtime.smack" % "smack-tcp" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-sasl-provided" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-sasl-javax" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-resolver-minidns" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-resolver-javax" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-resolver-dnsjava" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-legacy" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-jingle-old" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-java7" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-extensions" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-experimental" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-debug-slf4j" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-debug" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-core" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-compression-jzlib" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-bosh" % "4.1.0-alpha1",
  "org.igniterealtime.smack" % "smack-android" % "4.1.0-alpha1"
)
