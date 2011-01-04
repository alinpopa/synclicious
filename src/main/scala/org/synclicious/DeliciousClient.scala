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
    def getEntity(httpResponse: HttpResponse): Option[HttpEntity] = {
      val entity = httpResponse.getEntity
      if(entity != null) Some(entity) else None
    }
    val response = new DefaultHttpClient().execute{
      new HttpGet("http://feeds.delicious.com/v2/json/tags/alinpopa")
    }
    getEntity(response) match {
      case Some(entity) => readInputStream(new BufferedReader(new InputStreamReader(entity.getContent)), new StringBuilder)
      case None => ""
    }
  }
}

object DeliciousClient {
  import com.google.gson.{Gson, GsonBuilder, JsonDeserializer, JsonElement, JsonDeserializationContext}
  import com.google.gson.reflect.TypeToken
  import java.lang.reflect.Type
  import scala.collection.JavaConverters._
  import scala.collection.mutable.Set
  import java.util.{Set => JSet}
  import java.util.Map.{Entry => JMapEntry}

  private class MapJsonDeserializer extends JsonDeserializer[Map[String,Int]] {
    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Map[String,Int] = {
      val jsonObject = json.getAsJsonObject.entrySet
      @scala.annotation.tailrec
      def createMap(json: Set[JMapEntry[String,JsonElement]], mapCollector: Map[String,Int]): Map[String,Int] = {
        if(json.isEmpty) mapCollector else createMap(json.tail, mapCollector + (json.head.getKey -> json.head.getValue.getAsInt))
      }
      createMap(jsonObject.asScala, Map[String,Int]())
    }
  }
  
  def main(args: Array[String]): Unit = {
    val deliciousClient = new DeliciousClient("123")
    val gson = new GsonBuilder().registerTypeAdapter(classOf[Map[String,Int]], new MapJsonDeserializer).create
    val mapType = new TypeToken[Map[String,Int]]{}.getType
    println(gson.fromJson(deliciousClient.getData, mapType).asInstanceOf[Map[String,Int]].size)
  }
}

