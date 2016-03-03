import com.twitter.io.Buf
import com.twitter.util.Try

object BufTo {
  def int(buf: Buf): Int =
    Buf.Utf8.unapply(buf).flatMap(s => Try(s.toInt).toOption).getOrElse(0)
}