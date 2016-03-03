version := "0.11.0-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.11.0-SNAPSHOT" changing()
)

initialCommands in console :=
  """
    |import io.finch._
    |import io.finch.items._
    |import com.twitter.util.{Future, Await}
    |import com.twitter.conversions.time._
    |import com.twitter.concurrent.AsyncStream
    |import com.twitter.io.{Buf, Reader}
    |import com.twitter.finagle.Service
    |import com.twitter.finagle.Http
    |import com.twitter.finagle.http.{Request, Response, Status, Version}
    |implicit val timer = com.twitter.finagle.util.DefaultTimer.twitter
  """.stripMargin
