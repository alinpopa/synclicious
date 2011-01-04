package org.synclicious

class DeliciousClient(private val token: String) {
  import java.io.{InputStream, BufferedReader, InputStreamReader}
  import java.lang.StringBuilder
  import org.apache.http.{HttpResponse, HttpEntity}
  import org.apache.http.client.HttpClient
  import org.apache.http.client.methods.HttpGet
  import org.apache.http.impl.client.DefaultHttpClient

  def getData: String = {
    @scala.annotation.tailrec
    def readInputStream(reader: BufferedReader, stringBuilder: StringBuilder): String = {
      val line = reader.readLine()
      if(line != null){
        readInputStream(reader, stringBuilder.append(line))
      }else{
        stringBuilder.toString
      }
    }

    val httpClient = new DefaultHttpClient()
    val httpGet = new HttpGet("http://feeds.delicious.com/v2/json/tags/alinpopa")
    val response = httpClient.execute(httpGet)
    val entity = response.getEntity()
    val dataToReturn = new StringBuilder
    if(entity != null){
      readInputStream(new BufferedReader(new InputStreamReader(entity.getContent)), new StringBuilder)
    }else{
      ""
    }
  }
}

object DeliciousClient {
  def main(args: Array[String]): Unit = {
    val deliciousClient = new DeliciousClient("123")
    println(deliciousClient.getData)
  }
}

