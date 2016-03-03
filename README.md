# Streaming client

```scala
scala> val c = Http.client.withStreaming(enabled = true).newService(":8081")

scala> val stream = new Streaming(c)
```

# Print each chunk

```scala
scala> val e = asyncBody.mapAsync(as => as.foreach(x => println(Buf.Utf8.unapply(x))))
e: io.finch.Endpoint[Unit] = body
```

# Sum numbers (streaming request)

```scala
scala> val e = asyncBody.mapAsync(as => as.foldLeft(0)((acc, x) => acc + BufTo.int(x)).map(_.toString))
e: io.finch.Endpoint[String] = body
```

# Stream random numbers from server to client (streaming response)

**Pause-less**

```scala
scala> def rand(n: Int): AsyncStream[Int] = if (n == 0) AsyncStream.empty else scala.util.Random.nextInt +:: rand(n - 1)
rand: (n: Int)com.twitter.concurrent.AsyncStream[Int]

scala> val e = Endpoint(Ok(rand(100).map(x => Buf.Utf8(x.toString))))
e: io.finch.Endpoint[com.twitter.concurrent.AsyncStream[com.twitter.io.Buf]] =
```

**Delayed**

```scala
scala> def rand(n: Int): AsyncStream[Int] = if (n == 0) AsyncStream.empty else scala.util.Random.nextInt +:: rand(n - 1)
rand: (n: Int)com.twitter.concurrent.AsyncStream[Int]

scala> val e = Endpoint(Ok(rand(100).mapF(x => Future.sleep(1.second).map(_ => Buf.Utf8(x.toString)))))
e: io.finch.Endpoint[com.twitter.concurrent.AsyncStream[com.twitter.io.Buf]] =
```

# Sum so far (end to end)

```scala
scala> val e = asyncBody.map { as =>
     | var sumSoFar = 0
     | as.map { buf => sumSoFar += BufTo.int(buf); Buf.Utf8(sumSoFar.toString) }
     | }
e: io.finch.Endpoint[com.twitter.concurrent.AsyncStream[com.twitter.io.Buf]] = body
```