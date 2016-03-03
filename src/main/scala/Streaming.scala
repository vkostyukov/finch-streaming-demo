import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response, Version, Method}
import com.twitter.io.{Buf, Reader}
import com.twitter.util.Future

class Streaming(client: Service[Request, Response]) {

   val writer = Reader.writable()
   val req = Request(Version.Http11, Method.Get, "/", writer)

   client(req).flatMap { rep =>
     def readLoop(r: Reader): Future[Unit] = r.read(Int.MaxValue).flatMap { 
       case Some(buf) => println("Got chunk " + Buf.Utf8.unapply(buf)); readLoop(r)
       case None => Future.Done
     }

     println("Got response " + rep + " with body " + rep.contentString)

     if (rep.isChunked) readLoop(rep.reader)
     else Future.Done
   }

   def apply(message: String): Future[Unit] = writer.write(Buf.Utf8(message))

   def close(): Future[Unit] = writer.close()
}